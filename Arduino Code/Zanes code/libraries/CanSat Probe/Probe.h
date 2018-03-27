#ifndef Probe_h
#define Probe_h

#include <Wire.h>
#include <SPI.h>
#include <SoftwareSerial.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#include <Adafruit_GPS.h>
#include <TimerOne.h>
#include <SparkFun_MMA8452Q.h>
#include <EEPROM_Helper.h>
#include <RTClib.h>
#include "Arduino.h"


#define RANGE5V 1023
#define LOGRANGE 3.3

#define R1 1000.0  //voltage divider R1 value in ohms
#define R2 1330.0   //R2 value in ohms
#define VOLT_DIV_RATIO (R2 / (R1 + R2))  //voltage divider result for battery voltage input

#define LAUNCHING 0  //Launch ascent
#define FALLING 1    //CanSat released from rocket
#define ABHSDEPLOY 2  //ABHS arms deployed, CanSat descending
#define RELEASED 3   //Probe released from ABHS
#define LANDED 4     //Probe touchdown

#define STATE_ADDR 0  //index in EEPROM of state value
#define PACKET_ADDR 1  //index in EEPROM of packet count value
#define INITIALTIME_ADDR 3  //index in EEPROM of initial time value

#define CMD_RELEASEARMS 'r'  //when in falling state, forces a call to the releaseArms() function
#define CMD_RELEASEPROBE 't'  //when in falling state, forces a call to the releaseProbe() function
#define CMD_BUZZER 'b'  //sounds buzzer for 3 seconds
#define CMD_RESET 'z'  //zeros out mission time, packet count, and state
#define CMD_LAND 'l'  //forces a call to endMission() function
#define CMD_NEXT_STATE 'n'  //increments the state
#define CMD_PREV_STATE 'p'  //decrements the state

#define BUZZ_FREQ 262 //Hz
#define BUZZ_DUR 5000 //millis

#define RELEASE_TIME_LIMIT 10  //seconds
#define EMERGENCY_BUZZER_TIME_LIMIT 250//seconds

#define LIGHT_THRESH 60 //lux
#define RELEASE_ALT 300 //meters

#define SEALEVELPRESSURE 1013.25

#define TELEM_FILE "Telem.csv"  //name of file on sd card to save telemetry to

#define DECIMAL_PLACES 2  //number of digits after decimal for floating point numbers

#define GPSECHO false;


const byte radRX = 9;  //Software serial RX pin for xbee (digital pin 9)
const byte radTX = 10;  //Software serial TX pin for xbee (digital pin 10)

const byte gpsRX = 11; //Software serial RX pin for GPS (digital pin 11)
const byte gpsTX = 12; //Software serial TX pin for GPS (digital pin 12)


class Probe {
public:
  Probe(SoftwareSerial* radio, SoftwareSerial* gps);  //constructor
	void init();

  float temperature;  //C
	float altitude;  //meters
	float pressure;  //hPa
	float lux;  //light level
	float battVoltage;  //volts
  float gpsHour;  //gps time
  float gpsMin;
  float gpsSec;
  float gpsLat; //gps latitude
  float gpsLon; //gps longitude
  float gpsAlt; //gps altitude
  float gpsSats; //satellites connected to gps
  float tiltX;  //accel X direction
  float tiltY;  //accel Y direction
  float tiltZ;  //Accel Z direction


	uint16_t missionTime;  //seconds elapsed

	long initialTime;  //start time of the mission in epoch unix time

  bool timeSet;  //flag for checking if the initial time has been set
	bool transmitFlag;  //used for 1Hz transmit interrupt
	bool cmdFlag;  //used for RX external interrupt
	bool forceProbeRelease;
	bool releasingArms;  //used for controlling the nichrome burn timer
  bool releasingProbe;  //used for controlling nichrome burn timer
	bool lastTwo;  //flag for sending the last two seconds of data

  uint8_t state;  //launching(0), released(1), landed(2)
	uint8_t releaseCount;  //holds the seconds the nichrome has been on
	uint8_t lastTwoCount;  //holds the seconds for the last two data packets
	uint16_t emergencyCount;

  int lightPin = A0;  //analog input pin for the light sensor
	int battPin = A1;  //analog pin for voltage divider input
	int magnetPin = 6;  //digital pin 6
	int buzzPin = 5;  //digital pin 5
	int armReleasePin = 4;  //digital pin for nichrome to release ABHS arms
  int probeReleasePin = 7; //digital pin for nichrome to release abhs from probe

  uint16_t packetCount;  //number of packets sent

  String packet;  //holds the entire packet to be sent to GS
	char command;  //holds command char recieved from GS

  //File flightData;
	void updateTelem();  //gather latest sensor readings
	void setBME280Data();  //sets temp and pressure attributes
  void setGPSData();  //sets GPS coordinates
  void setTiltData(); //sets tilt values
	void setLux();  //sets the lux attribute
	void setMissionTime();  //set the elapsed time in seconds
	void setVoltage();  //set the battVoltage attribute
	void processCommand();
	void setState(uint8_t val);
	void saveEEPROMData();
	void saveTelem();
	void releaseArms();
  void releaseProbe();
	void createPacket();
	void transmitTelem();
	void resetSaveData();
	void buzz(int dur, bool infinate);
	void checkFallingCondition();
	void checkProbeReleaseCondition();
  void checkLandCondition();
	void endMission();

  Adafruit_BME280 bme;
  MMA8452Q tilt;
  RTC_DS3231 rtc;

	SoftwareSerial* xbee;
  SoftwareSerial* gps;


};

#endif
