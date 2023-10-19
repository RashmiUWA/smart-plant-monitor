package com.smart.plant.monitor.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.smart.plant.monitor.model.SensorData;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Repository
public interface SensorDataRepository extends ReactiveMongoRepository<SensorData, String> {
  Flux<SensorData> findByPublished(boolean published);

  Flux<SensorData> findByDeviceIdAndTimestampAfter(String deviceId, Date timestamp);

  Mono<SensorData> findTopByOrderByIdDesc();

}
