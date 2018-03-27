/*
 Name:		Probe.ino
 Created:	2/21/2018 2:14:21 AM
 Author:	zane
*/

#include <Probe.h>
#include <Adafruit_GPS.h>

SoftwareSerial xbee(9, 10);  //software serial port for the xbee
SoftwareSerial gpsSerial(11, 12);  //software serial port for the gps

Probe probe = Probe(&xbee, &gpsSerial);

void setup(){
  
  pinMode(probe.armReleasePin, OUTPUT);
	digitalWrite(probe.armReleasePin, 0);

  pinMode(probe.probeReleasePin, OUTPUT);
	digitalWrite(probe.probeReleasePin, 0);

  Serial.begin(9600);


  
  analogReference(DEFAULT);  //reference range 0V - 5V

  if (!probe.bme.begin()) {
		Serial.print("BMP failed");
		while (1);
	}

	if (!probe.rtc.begin()) {
    Serial.println("Couldn't find RTC");
    while (1);
  }

  if (probe.rtc.lostPower()) {
    Serial.println("RTC lost power, lets set the time!");
    probe.rtc.adjust(DateTime(F(__DATE__), F(__TIME__)));
  }

  Timer1.initialize();  //Timer1 will be used to send a packet every second
	Timer1.attachInterrupt(sendPacket); //Interrupt using a timer to send a packet every second
	pinMode(probe.battPin, INPUT);  //set the voltage input
	pinMode(probe.armReleasePin, OUTPUT);  //set the digital output of the release pin
  pinMode(probe.probeReleasePin, OUTPUT);  //set the digital output of the release pin
  pinMode(probe.lightPin, INPUT);
	attachInterrupt(digitalPinToInterrupt(radRX), processCommand, RISING);  //initialize an interrupt for D2
}

void loop() {

	switch (probe.state) {
	case LAUNCHING:
		probe.checkFallingCondition();
		probe.updateTelem();
		break;

	case FALLING:
    probe.releaseArms();
    break;

  case ABHSDEPLOY:
    probe.checkProbeReleaseCondition();
    probe.updateTelem();
    break;

	case RELEASED:
    probe.checkLandCondition();
		break;
	case LANDED:
		probe.endMission();
		break;
	}

	if (probe.cmdFlag) {
		probe.processCommand();
		probe.cmdFlag = false;
	}


	if (probe.transmitFlag){
		//probe.xbee->print("LUX: ");
		//probe.xbee->println(probe.lux);
		probe.createPacket();
		probe.saveTelem();
		probe.transmitTelem();
	  probe.transmitFlag = false;
	}

	if (probe.emergencyCount > EMERGENCY_BUZZER_TIME_LIMIT) {
		probe.setState(LANDED);
	}

}

void processCommand() {
	probe.cmdFlag = true;

	if (probe.state == LANDED) {
		probe.processCommand();
		probe.cmdFlag = false;
	}
}

void sendPacket() {
	probe.emergencyCount++;
	probe.transmitFlag = true;
}
