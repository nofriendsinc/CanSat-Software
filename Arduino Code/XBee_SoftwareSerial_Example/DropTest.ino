#include <ArduinoSTL.h>
#include <Wire.h> // Must include Wire library for I2C
#include <SFE_MMA8452Q.h> // Includes the SFE_MMA8452Q library

MMA8452Q accel; // Default MMA8452Q object create. (Address = 0x1D)
int prep = 0;
int arm = 1;
int deploy = 2;
int state = prep;

void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  accel.init(); // Default init: +/-2g and 800Hz ODR
  Serial.flush();
}

void loop() { // run over and over  
  accel.read(); // Update acceleromter data


  switch (state)  {
    case prep:
      
      break;
    case arm:
      
      break;
    case deploy:

      break;
  }
}
