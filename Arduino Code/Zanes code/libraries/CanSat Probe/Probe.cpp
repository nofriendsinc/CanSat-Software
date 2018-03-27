#include "Probe.h"
#include "Arduino.h"

void(*resetFunc)(void) = 0;

SoftwareSerial gpsSerialPort(11,12);

Adafruit_GPS GPS(&gpsSerialPort);

Probe::Probe(SoftwareSerial* radio, SoftwareSerial* gps) {  //constructor implementation
	xbee = radio;  //initialize the radio
	xbee->begin(9600);

  GPS.begin(9600);

	GPS.sendCommand(PMTK_SET_NMEA_UPDATE_1HZ);

	tilt.init();

	init();
}

void Probe::init() {
  //init attribute values
	this->setState(LAUNCHING);
	this->temperature = 0.0;
	this->pressure = 0.0;
	this->lux = 0.0;
	this->battVoltage = 0.0;
  this->gpsHour = 0.0;
	this->gpsMin = 0.0;
	this->gpsSec = 0.0;
  this->gpsLon = 0.0;
  this->gpsLat = 0.0;
  this->gpsSats = 0.0;
  this->tiltX = 0.0;
  this->tiltY = 0.0;
  this->tiltZ = 0.0;
	this->transmitFlag = false;
	this->cmdFlag = false;
	this->forceProbeRelease = false;
	this->releasingArms = false;
  this->releasingProbe = false;
	this->lastTwo = false;
	this->releaseCount = 0;
	this->lastTwoCount = 0;
	this->emergencyCount = 0;


	//init by reading from EEPROM
	EEPROM_read(STATE_ADDR, state);
	EEPROM_read(PACKET_ADDR, packetCount);
	EEPROM_read(INITIALTIME_ADDR, initialTime);
}

void Probe::updateTelem() {
	//gather latest sensor readings
	this->setBME280Data();
	this->setLux();
	this->setMissionTime();
	this->setVoltage();
  this->setGPSData();
  this->setTiltData();
}

void Probe::setBME280Data() {
  this->temperature = bme.readTemperature();
  this->pressure = bme.readPressure();
  this->altitude = bme.readAltitude(SEALEVELPRESSURE);
}

void Probe::setLux() {
  int rawLux = analogRead(this->lightPin);  //get raw voltage input
	float logLux = rawLux * LOGRANGE / RANGE5V;
	this->lux = pow(10, logLux);
}

void Probe::setMissionTime() {
	if (this->initialTime == 0) {  //if initial time has not been set
		this->initialTime = rtc.now().unixtime();  //set it to current time
	}

	DateTime currentTime = rtc.now();  //get the current time
	this->missionTime = currentTime.unixtime() - this->initialTime;
}

void Probe::setVoltage() {
	int input = analogRead(this->battPin);
	float rawVoltage = (input * LOGRANGE) / RANGE5V;
	this->battVoltage = rawVoltage / VOLT_DIV_RATIO;
}

void Probe::setGPSData() {
	char c = GPS.read();
	if(GPS.newNMEAreceived()){
		GPS.parse(GPS.lastNMEA());
	}
	this->gpsAlt = GPS.altitude / 1000;
	this->gpsLat = GPS.latitude;
	this->gpsLon = GPS.longitude;
	this->gpsSats = GPS.satellites;

	this->gpsHour = GPS.hour;
	this->gpsMin = GPS.minute;
	this->gpsSec = GPS.seconds;

}

void Probe::setTiltData() {
	tilt.read();
	this->tiltX = tilt.x;
	this->tiltY = tilt.y;
	this->tiltZ = tilt.z;
}

void Probe::processCommand() {
	if (this->xbee->available()) {
		this->command = this->xbee->read();
	}

	if (this->command == CMD_RELEASEPROBE && this->state == ABHSDEPLOY) {
		this->forceProbeRelease = true;
	}
	else if (this->command == CMD_RESET) {
		this->xbee->println("resetting data...");
		this->emergencyCount = 0;
		this->resetSaveData();
		resetFunc();
	}
	else if (this->command == CMD_BUZZER) {
		this->buzz(BUZZ_DUR, false);  //sound buzzer for specified amount of time
	}
	else if (this->command == CMD_LAND) {
		this->endMission();  //set state for last time, stop timer interrupt, and sound buzzer
	}
	else if (this->command == CMD_NEXT_STATE) {
		this->state++;
	}
	else if (this->command == CMD_PREV_STATE) {
		if (this->state >= 1) {  //only if not on first state
			this->state--;
		}
	}

	this->command = NULL;  //band-aid for rx every second bug
}

void Probe::releaseArms() {
  uint16_t startTime = this->missionTime;

  this->releasingArms = true;  //set the counter flag to true

  while(startTime-missionTime < 3){
	 digitalWrite(this->armReleasePin, 1);  //turn on the Nichrome
  }

	digitalWrite(this->armReleasePin, 0);  //turn off the Nichrome
	this->releasingArms = false;  //stop counting
	this->releaseCount = 0;  //reset counter for in-the-loop retesting (no MCU reset required)

  this->setState(ABHSDEPLOY);
}

void Probe::releaseProbe() {
  uint16_t startTime = this->missionTime;

  this->releasingProbe = true;  //set the counter flag to true

  while(startTime-missionTime < 3){
	 digitalWrite(this->probeReleasePin, 1);  //turn on the Nichrome
  }

	digitalWrite(this->probeReleasePin, 0);  //turn off the Nichrome
	this->releasingProbe = false;  //stop counting
	this->releaseCount = 0;  //reset counter for in-the-loop retesting (no MCU reset required)
}

void Probe::createPacket() {
	this->packet = String("<5554>," +
						  String(this->missionTime) +
						  ">,<" +
						  String(this->packetCount) +
							">,<" +
						  String(this->altitude, DECIMAL_PLACES) +
						  ">,<" +
              String(this->pressure, DECIMAL_PLACES) +
 							">,<" +
						  String(this->temperature, DECIMAL_PLACES) +
							">,<" +
						  String(this->battVoltage, DECIMAL_PLACES) +
						  ">,<" +
              String(this->gpsHour, DECIMAL_PLACES) + String(this->gpsMin, DECIMAL_PLACES) + String(this->gpsSec, DECIMAL_PLACES) +
						  ">,<" +
              String(this->gpsLat, DECIMAL_PLACES) +
						  ">,<" +
              String(this->gpsLon, DECIMAL_PLACES) +
						  ">,<" +
              String(this->gpsAlt, DECIMAL_PLACES) +
						  ">,<" +
              String(this->gpsSats, DECIMAL_PLACES) +
						  ">,<" +
              String(this->tiltX, DECIMAL_PLACES) +
						  ">,<" +
              String(this->tiltY, DECIMAL_PLACES) +
						  ">,<" +
              String(this->tiltZ, DECIMAL_PLACES) +
						  ">,<" +
						  String(this->state) + ">\r");
}

void Probe::transmitTelem() {
	this->packetCount++;  //increment the packet count
	this->xbee->println(this->packet);  //send the packet to GS
	this->saveEEPROMData();  //save data to eeprom
}

void Probe::setState(uint8_t val) {
	this->state = val;
}

void Probe::saveEEPROMData() {
	EEPROM_write(STATE_ADDR, this->state);
	EEPROM_write(PACKET_ADDR, this->packetCount);
	EEPROM_write(INITIALTIME_ADDR, this->initialTime);
}

void Probe::saveTelem() {
	/*
	this->flightData = SD.open(TELEM_FILE, FILE_WRITE);
	if (this->flightData) {
		this->flightData.println(this->packet);
		this->flightData.close();
	}
	*/
}

void Probe::resetSaveData() {
	this->state = 0;
	this->packetCount = 0;
	this->initialTime = 0;

	EEPROM_write(STATE_ADDR, this->state);
	EEPROM_write(PACKET_ADDR, this->packetCount);
	EEPROM_write(INITIALTIME_ADDR, this->initialTime);

	this->init();
}

void Probe::buzz(int dur, bool infinate) {  //dur in millis
	if (infinate) {  //sound buzzer forever
		tone(buzzPin, BUZZ_FREQ);
		while (1);
	}

	tone(buzzPin, BUZZ_FREQ, dur);  //sound buzzer for specified duration
}

void Probe::checkFallingCondition() {
	if (this->lux >= LIGHT_THRESH) {  //cansat ejected from rocket
		this->setState(FALLING);  //cansat now falling
	}
}



void Probe::checkProbeReleaseCondition() {
	if (this->altitude <= RELEASE_ALT || this->forceProbeRelease) {  //cansat has fallen to nominal probe release altitude
		this->releaseProbe();  //burn the nichrome to release the probe
		this->setState(RELEASED);  //probe+parachute should be released and falling at this point
		this->forceProbeRelease = false;
	}
}

void Probe::checkLandCondition() {
  if(this->altitude <= 20){
    this->setState(LANDED);
  }
}

void Probe::endMission() {
	this->saveEEPROMData();
	Timer1.stop();  //stop timer so no more timer interrupts occur
	this->buzz(0, true);  //sound buzzer forever
	while (1);  //this line will never get reached, but just in case
}
