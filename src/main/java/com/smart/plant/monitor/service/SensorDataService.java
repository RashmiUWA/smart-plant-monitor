package com.smart.plant.monitor.service;

import java.util.Optional;

import com.smart.plant.monitor.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.plant.monitor.model.SensorData;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SensorDataService {

  @Autowired
  SensorDataRepository sensorDataRepository;

  public Flux<SensorData> findAll() {
    return sensorDataRepository.findAll();
  }

  public Mono<SensorData> findById(String id) {
    return sensorDataRepository.findById(id);
  }

  public Mono<SensorData> save(SensorData sensorData) {
    return sensorDataRepository.save(sensorData);
  }

  public Mono<SensorData> update(String id, SensorData sensorData) {
    return sensorDataRepository.findById(id).map(Optional::of).defaultIfEmpty(Optional.empty())
        .flatMap(optionalSensorData -> {
          if (optionalSensorData.isPresent()) {
            sensorData.setId(id);
            return sensorDataRepository.save(sensorData);
          }

          return Mono.empty();
        });
  }

  public Mono<Void> deleteById(String id) {
    return sensorDataRepository.deleteById(id);
  }

  public Mono<Void> deleteAll() {
    return sensorDataRepository.deleteAll();
  }

  public Flux<SensorData> findByPublished(boolean isPublished) {
    return sensorDataRepository.findByPublished(isPublished);
  }
}
