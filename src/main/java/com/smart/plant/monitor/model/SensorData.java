package com.smart.plant.monitor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Document
public class SensorData {

  @Id
  private String id;
  private String deviceId;

  private Date timestamp;

  private Float temperature;

  private Float humidity;

  private Float lightStrength;

  private Float soilMoisture;

  private boolean published;

  public SensorData() {

  }

  public SensorData(String deviceId, Date timestamp, Float temperature, Float humidity, Float lightStrength, Float soilMoisture, boolean published) {
    this.id = deviceId + timestamp.getTime();
    this.deviceId = deviceId;
    this.timestamp = timestamp;
    this.temperature = temperature;
    this.humidity = humidity;
    this.lightStrength = lightStrength;
    this.soilMoisture = soilMoisture;
    this.published = published;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public Float getTemperature() {
    return temperature;
  }

  public void setTemperature(Float temperature) {
    this.temperature = temperature;
  }

  public Float getHumidity() {
    return humidity;
  }

  public void setHumidity(Float humidity) {
    this.humidity = humidity;
  }

  public Float getLightStrength() {
    return lightStrength;
  }

  public void setLightStrength(Float lightStrength) {
    this.lightStrength = lightStrength;
  }

  public Float getSoilMoisture() {
    return soilMoisture;
  }

  public void setSoilMoisture(Float soilMoisture) {
    this.soilMoisture = soilMoisture;
  }

  public boolean isPublished() {
    return published;
  }

  public void setPublished(boolean published) {
    this.published = published;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SensorData that = (SensorData) o;
    return Objects.equals(deviceId, that.deviceId) && Objects.equals(timestamp, that.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceId, timestamp);
  }

  @Override
  public String toString() {
    return "SensorData{" +
            "id='" + id + '\'' +
            ", deviceId='" + deviceId + '\'' +
            ", timestamp=" + timestamp +
            ", temperature=" + temperature +
            ", humidity=" + humidity +
            ", lightStrength=" + lightStrength +
            ", soilMoisture=" + soilMoisture +
            '}';
  }
}
