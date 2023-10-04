package com.smart.plant.monitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.smart.plant.monitor.model.SensorData;
import com.smart.plant.monitor.service.SensorDataService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class SPMController {

  @Autowired
  SensorDataService sensorDataService;

  @GetMapping("/data")
  @ResponseStatus(HttpStatus.OK)
  public Flux<SensorData> getAllSensorData(@RequestParam(required = false) String title) {
      return sensorDataService.findAll();
  }

  @GetMapping("/data/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<SensorData> getSensorDataById(@PathVariable("id") String id) {
    return sensorDataService.findById(id);
  }

  @PostMapping("/data")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<SensorData> createSensorData(@RequestBody SensorData sensorData) {
    return sensorDataService.save(sensorData);
  }

  @PutMapping("/data/{id}")

  @ResponseStatus(HttpStatus.OK)
  public Mono<SensorData> updateSensorData(@PathVariable("id") String id, @RequestBody SensorData sensorData) {
    return sensorDataService.update(id, sensorData);
  }

  @DeleteMapping("/data/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteSensorData(@PathVariable("id") String id) {
    return sensorDataService.deleteById(id);
  }

  @DeleteMapping("/data")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteAllSensorDatas() {
    return sensorDataService.deleteAll();
  }

  @GetMapping("/data/published")
  @ResponseStatus(HttpStatus.OK)
  public Flux<SensorData> findByPublished() {
    return sensorDataService.findByPublished(true);
  }

  @GetMapping("/dummy")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<SensorData> createDummyData() {
    SensorData dummy = new SensorData();
    dummy.setDeviceId("D1");
    dummy.setTimestamp(new Date());
    dummy.setId("D1"+dummy.getTimestamp().getTime());
    dummy.setTemperature(22.5f);
    dummy.setHumidity(80.0f);
    dummy.setLightStrength(245f);
    dummy.setSoilMoisture(10f);
    return sensorDataService.save(dummy);
  }
}
