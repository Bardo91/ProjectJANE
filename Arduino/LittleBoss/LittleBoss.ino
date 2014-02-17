#include <Arduino.h>


// Define pin constants
#define pinLightInput 24
#define pinLightOutput 2

#define pinFanInput 26
#define pinFanOutput 3

#define pinPCInput 22
#define pinPCOutput 23


// State variables
int light = -1;
int fan = -1;
int PC = -1;

boolean lightState = false;
boolean fanState = false;
boolean PCState = false;

// Counter to send connection checker
int counterCKR = 0;

void setup(){
  // Initialize serial connections
  Serial3.begin(9600);
  Serial.begin(9600);

  // Light related pin declarations
  pinMode(INPUT, pinLightInput);

  // Fan related pin declarations  
  pinMode(INPUT, pinFanInput);

  // PC related pin declarations
  pinMode(INPUT, pinPCInput);
  pinMode(OUTPUT, pinPCOutput);

  // Initial switcher state

  light = digitalRead(pinLightInput);

  fan = digitalRead(pinFanInput);

  PC = digitalRead(pinPCInput);


}

void loop(){
  //Sendsignal to the smartphone to check the connection
  if (counterCKR > 20){
    Serial3.write("10LittleBoss"); 
    counterCKR = 0;
  }else{
    counterCKR = counterCKR + 1;
  }
    
  
  // Check switchers. Act by changes
  if(digitalRead(pinLightInput) != light){
    light = digitalRead(pinLightInput);
    lightState = !lightState;
  }
  if(digitalRead(pinFanInput) != fan){
    fan = digitalRead(pinFanInput);
    fanState = !fanState;
  }
  if(digitalRead(pinPCInput) == HIGH){
    PCState = HIGH;
  }
  delay(100);
  // Check Bluetooth Serial port
  if(Serial3.available()){
    delay(30);
    char valor = '-1';
    int n = 0;
    int unit = 1; // multiple of 10
    String command = "";
    // Obtain the string length
    valor = Serial3.read();
    while(valor >= '0' && valor <= '9'){
      n = n*unit + (valor - '0');
      unit = unit*10;
      valor = Serial3.read();
    }
    // Read the whole string
    for(int i = 0 ; i < n  ; i ++){
      command = command + valor;
      valor = Serial3.read();
    }
    
    // Compare the string
    if(command == "encender ordenador"){
      PCState = true;
    }
    else if(command == "apagar ordenador"){
      PCState = false;
    }
    else if(command == "encender luz"){
      lightState = true;
    }
    else if (command == "apagar luz"){
      lightState = false;
    }
    else if (command == "encender ventilador"){
      fanState = true;
    }
    else if (command == "apagar ventilador"){
      fanState = false;
    }
    else if(command == "apagar todo"){
      lightState = false;
      fanState = false;
      PCState = false;
    }
    else if(command == "encender luz y ventilador" || command == "encender ventilador y luz"){
      lightState = true;
      fanState = true;
    }
    else if(command == "apagar luz y ventilador" || command == "apagar ventilador y luz"){
      lightState = false;
      fanState = false;
    }
    else if(command == "encender luz y apagar ventilador" || command == "apagar ventilador y encender luz"){
      lightState = true;
      fanState = false;
    }else if(command == "encender ventilador y apagar luz" || command == "apagar luz y encender ventilador"){
      lightState = false;
      fanState = true;
    }else if(command == "cambiar luz"){
      lightState = !lightState;
    }else if(command == "cambiar ventilador"){
      fanState = !fanState;
    }else if(command == "cambiar pc"){
      PCState = !PCState;
    }
  }
  
  // Check Sensors


  // Enable actuators
  if(lightState){ //"Negative logic"
    analogWrite(pinLightOutput, 0);
  }
  else{
    analogWrite(pinLightOutput, 255);
  }
  if(fanState){ //"Negative logic"
    analogWrite(pinFanOutput, 0);
  }
  else{
    analogWrite(pinFanOutput, 255);
  }
  if(PCState){
    //Serial.println("Boton pc pulsado");
    digitalWrite(pinPCOutput, HIGH);
    delay(700);
    digitalWrite(pinPCOutput, LOW);
    PCState = false;
  }

  //Serial.print("LightState = ");
  //Serial.println(lightState);
  //Serial.print("FanState = ");
  //Serial.println(fanState);
  //Serial.print("PCState = ");
  //Serial.println(PCState);
  
  //delay(1000);

}



