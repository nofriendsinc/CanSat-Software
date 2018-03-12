#include <ArduinoSTL.h>
#include <Wire.h> // Must include Wire library for I2C
#include <SFE_MMA8452Q.h> // Includes the SFE_MMA8452Q library

/*
  Software serial multple serial test

 Receives from the hardware serial, sends to software serial.
 Receives from software serial, sends to hardware serial.

 The circuit:
 * RX is digital pin 10 (connect to TX of other device) ------------------------------>>>>> Connect TX of XBee
 * TX is digital pin 11 (connect to RX of other device) ------------------------------>>>>> Connect RX of XBee

 Note:
 Not all pins on the Mega and Mega 2560 support change interrupts,
 so only the following can be used for RX:
 10, 11, 12, 13, 50, 51, 52, 53, 62, 63, 64, 65, 66, 67, 68, 69

 Not all pins on the Leonardo and Micro support change interrupts,
 so only the following can be used for RX:
 8, 9, 10, 11, 14 (MISO), 15 (SCK), 16 (MOSI).

 created back in the mists of time
 modified 25 May 2012
 by Tom Igoe
 based on Mikal Hart's example

 This example code is in the public domain.

 */

MMA8452Q accel; // Default MMA8452Q object create. (Address = 0x1D)
bool canGo = true;
int pack = 1;
int state = 2;

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
  float mag = sqrt(sq(accel.cx) + sq(accel.cy) + sq(accel.cz));
  float theta = atan(accel.cy / accel.cx);
  float atheta = atan(accel.cx / accel.cy);
  float phi = acos(accel.cz / mag);

  //Serial.println(Serial.read());

  switch (state)  {
    case 0:
      if(Serial.read() == 104 && !canGo)  {
        Serial.println("Received");
        //state = 3;
      }
      break;
    case 1:
    if(Serial.read() == 104 && !canGo)  {
      Serial.read();
    }
    if(Serial.read() != 104 && !canGo)  {
      state = 0;
    }
    break;
    case 3:  
      if(Serial.read() == 108 & canGo)  {
        canGo = true;
        state = 2;
      }
      break;
    case 2:
      if(canGo)  {
        Serial.println(millis() / 1000.0);
        Serial.println(pack);
        Serial.println(theta);
        Serial.println(atheta);
        Serial.println(phi);
        pack += 1;
      }
      break;
  }
}

float toDegree(float rad) {
  return (rad * 180) / 2;
}

