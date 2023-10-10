package com.smart.plant.monitor.dto;

public class DeviceDto {
    private String name;
    private String id;
    private String description;
    private String status;
    private Integer condition;
    private Float avgTemp;
    private Float avgHumidity;
    private Float avgLight;
    private Float avgSoilMoisture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public Float getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(Float avgTemp) {
        this.avgTemp = avgTemp;
    }

    public Float getAvgHumidity() {
        return avgHumidity;
    }

    public void setAvgHumidity(Float avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    public Float getAvgLight() {
        return avgLight;
    }

    public void setAvgLight(Float avgLight) {
        this.avgLight = avgLight;
    }

    public Float getAvgSoilMoisture() {
        return avgSoilMoisture;
    }

    public void setAvgSoilMoisture(Float avgSoilMoisture) {
        this.avgSoilMoisture = avgSoilMoisture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
