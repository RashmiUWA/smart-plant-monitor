package com.smart.plant.monitor.util;

import com.smart.plant.monitor.model.SensorData;
import org.springframework.data.util.Pair;

public class PlantStatusDeterminer {
    public Pair<String, Integer> determinePlantStatus(SensorData sensorData, String plantType) {
        // Extract sensor data values
        float temperature = sensorData.getTemperature();
        float lightStrength = sensorData.getLightStrength();
        float humidity = sensorData.getHumidity();
        float soilMoisture = sensorData.getSoilMoisture();

        // Check if it's nighttime (from 6pm to 6am)
        boolean isNight = false;
        int hour = sensorData.getTimestamp().getHours();
        if ((hour >= 18 || hour < 6) && lightStrength == 0) {
            isNight = true;
        }

        // Determine the status based on the specified plant type
        return getStatusForPlantType(isNight, plantType, temperature, lightStrength, humidity, soilMoisture);

    }

    private Pair<String, Integer> getStatusForPlantType(Boolean isNight, String plantType, float temperature, float lightStrength, float humidity, float soilMoisture) {
        Range temperatureRange;
        Range lightStrengthRange;
        Range humidityRange;
        Range soilMoistureRange;

        switch (plantType) {
            case "Foliage Plant":
                if (isNight) {
                    temperatureRange = new Range(16, 20); // Adjusted temperature range for night
                    lightStrengthRange = new Range(0, 900); // Adjusted light strength range for night
                } else {
                    temperatureRange = new Range(21, 26);
                    lightStrengthRange = new Range(200, 900);
                }

                humidityRange = new Range(40, 60);
                soilMoistureRange = new Range(400, 600);
                break;
            case "Flowering Plant":
                if (isNight) {
                    temperatureRange = new Range(13, 16); // Adjusted temperature range for night
                    lightStrengthRange = new Range(0, 900); // Adjusted light strength range for night
                } else {
                    temperatureRange = new Range(21, 26);
                    lightStrengthRange = new Range(200, 900);
                }

                humidityRange = new Range(40, 60);
                soilMoistureRange = new Range(400, 600);
                break;
            case "Succulent Plant":
                temperatureRange = new Range(4, 27);
                lightStrengthRange = new Range(100, 300);
                humidityRange = new Range(10, 60);
                soilMoistureRange = new Range(200, 400);
                break;
            case "Cactus":
                temperatureRange = new Range(4, 27);
                lightStrengthRange = new Range(150, 500);
                humidityRange = new Range(10, 60);
                soilMoistureRange = new Range(200, 400);
                break;
            default:
                return Pair.of("Unknown Plant",0);
        }

        if (temperatureRange.isWithinRange(temperature)) {
            if (lightStrengthRange.isWithinRange(lightStrength)) {
                if (humidityRange.isWithinRange(humidity)) {
                    if (soilMoistureRange.isWithinRange(soilMoisture)) {
                        return Pair.of("Good",5);
                    } else if (soilMoisture < soilMoistureRange.getMin()) {
                        return Pair.of("Thirsty",3);
                    } else {
                        return Pair.of("Drown",1);
                    }
                } else if (humidity < humidityRange.getMin()) {
                    return Pair.of("Dry",2);
                } else {
                    return Pair.of("Humid",4);
                }
            } else if (lightStrength < lightStrengthRange.getMin()) {
                return Pair.of("Weak",1);
            } else {
                return Pair.of("Strong",2);
            }
        } else if (temperature < temperatureRange.getMin()) {
            return Pair.of("Cold",4);
        } else {
            return Pair.of("Hot",3);
        }
    }

    private static class Range {
        private float min;
        private float max;

        public Range(float min, float max) {
            this.min = min;
            this.max = max;
        }

        public boolean isWithinRange(float value) {
            return value >= min && value <= max;
        }

        public float getMin() {
            return min;
        }

        public void setMin(float min) {
            this.min = min;
        }

        public float getMax() {
            return max;
        }

        public void setMax(float max) {
            this.max = max;
        }
    }
}
