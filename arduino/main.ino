// Include Libraries
#include "Arduino.h"
#include <DHT11.h>
#include "LDR.h"
#include "SoilMoisture.h"
#include <LiquidCrystal.h>
#include <ArduinoJson.h>

// Pin Definitions
#define DHT_PIN_DATA 13
#define LDR_PIN_SIG 36
#define SOILMOISTURE_5V_PIN_SIG 39
#define LCD_PIN_RS 32
#define LCD_PIN_E 33
#define LCD_PIN_DB4 23
#define LCD_PIN_DB5 4
#define LCD_PIN_DB6 22
#define LCD_PIN_DB7 21

// Create an instance of the class
DHT11 dht11(DHT_PIN_DATA);
LDR ldr(LDR_PIN_SIG);
SoilMoisture soilMoisture_5v(SOILMOISTURE_5V_PIN_SIG);
LiquidCrystal lcd(LCD_PIN_RS, LCD_PIN_E, LCD_PIN_DB4, LCD_PIN_DB5, LCD_PIN_DB6, LCD_PIN_DB7);

#include <WiFi.h>
#include <HTTPClient.h>

const char* ssid = "Jono Pixal";
const char* password = "iamaleech";
String serverAddress = "http://192.168.193.40:8080";
const long interval = 1000 *30; //* 60; // 30 minutes in milliseconds
unsigned long previousMillis = 0;
float temperatureSum = 0;
float humSum = 0;
int ldrSum = 0;
int moistureSum = 0;
int loopCount = 0;

void connectToWiFi() {
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("Connected to WiFi");
}

void sendSensorData(String jsonString) {
  // Initialize the HTTP client
  HTTPClient http;

  String endpoint = "/api/data"; 
  String url = serverAddress + endpoint;

  // Make the POST request
  http.begin(url);
  http.addHeader("Content-Type", "application/json");

  int httpResponseCode = http.POST(jsonString);

  if (httpResponseCode > 0) {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    String response = http.getString(); 
    Serial.println(response);
  } 
  else {
    Serial.print("Error sending POST request. HTTP Response code: ");
    Serial.println(httpResponseCode);
  }
  http.end();
}


void setup()
{
    // Initialize serial communication at 9600 baud.
  Serial.begin(9600);
  Serial.println("Beginning Program");    // Initialize the LCD with 16 columns and 2 rows.
  lcd.begin(16, 2);
  connectToWiFi();

}


void loop()
{
  
    // Read the humidity from the sensor.
    float humidity = dht11.readHumidity();

    // Read the temperature from the sensor.
    float temperature = dht11.readTemperature();
    // Read current light strength data
    int ldrSample = ldr.read();
    ldrSample = map(ldrSample, 0, 4500, 0, 100);


    // Read soil moisture data
    int soilMoisture_5vVal = soilMoisture_5v.read();
    soilMoisture_5vVal = map(soilMoisture_5vVal, 1200, 3400, 100, 0);

    // If the temperature and humidity readings were successful, print them to the serial monitor and LCD.
    if (temperature != -1 && humidity != -1)
    {
        // Print to the serial monitor.
        Serial.print("Temperature: ");
        Serial.print(temperature);
        Serial.println(" C");

        Serial.print("Humidity: ");
        Serial.print(humidity);
        Serial.println(" %");

        Serial.print(F("Light Strength: "));
        Serial.print(ldrSample);
        Serial.println(" %");

        Serial.print(F(", Soil Moisture: "));
        Serial.println(soilMoisture_5vVal);
        
        // Display "OK" on the LCD.
        lcd.clear();
        lcd.setCursor(0, 0);
        lcd.print("Temp: ");
        lcd.print(temperature);
        lcd.print(" C");
        lcd.setCursor(0, 1);
        lcd.print("Hum: ");
        lcd.print(humidity);
        lcd.print(" %");
        delay(3000);
        lcd.clear();
        lcd.setCursor(0, 0);
        lcd.print("Light: ");
        lcd.print(ldrSample);
        lcd.println(" %         ");
        lcd.setCursor(0, 1);
        lcd.print("Moisture: ");
        lcd.print(soilMoisture_5vVal);
    }
    else
    {
        // If the temperature or humidity reading failed, print an error message to the serial monitor.

        Serial.println("Error reading data");

        // Clear the LCD and print an error message.
        lcd.clear();
        lcd.print("Error reading data");
    }

    // Wait for 2 seconds before the next reading.
    delay(6000);
    unsigned long currentMillis = millis();

  if (currentMillis - previousMillis >= interval) { //average this ammount
    previousMillis = currentMillis;

    temperature = temperatureSum/loopCount;
    humidity = humSum/loopCount;
    ldrSample = round(ldrSum/loopCount); 
    soilMoisture_5vVal = round(moistureSum/loopCount); 

    String deviceId = "D1";

    StaticJsonDocument<400> jsonDoc;
    JsonObject jsonItem = jsonDoc.to<JsonObject>();

    // Add fields to the JSON object
    jsonItem["deviceId"] = deviceId;
    jsonItem["timestamp"] = currentMillis;
    jsonItem["temperature"] = temperature;
    jsonItem["humidity"] = humidity;
    jsonItem["lightStrength"] = ldrSample;
    jsonItem["soilMoisture"] = soilMoisture_5vVal;
    jsonItem["published"] = false;

    // Serialize the JSON document
    String jsonString;
    serializeJson(jsonDoc, jsonString);

    // Print the JSON string
    sendSensorData(jsonString);
    Serial.println(jsonString);
    temperatureSum = 0;
    humSum = 0;
    ldrSum = 0;
    moistureSum = 0;
 
  }
  else {
    temperatureSum += temperature;
    humSum += humidity ;
    ldrSum += ldrSample;
    moistureSum += soilMoisture_5vVal;   
    loopCount += 1; 
  }


}

