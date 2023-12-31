package com.smart.plant.monitor.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;

import com.smart.plant.monitor.dto.DeviceDto;
import com.smart.plant.monitor.dto.UserDto;
import com.smart.plant.monitor.repository.SensorDataRepository;
import com.smart.plant.monitor.util.PlantStatusDeterminer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.smart.plant.monitor.model.SensorData;

import org.w3c.dom.ls.LSOutput;
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

    public Mono<UserDto> getPlantStatusForUser(Integer userId) {

    Calendar cal = Calendar.getInstance(); // creates calendar
    cal.setTime(new Date());               // sets calendar time/date
    cal.add(Calendar.HOUR, -12);      // adds one hour
    cal.getTime();
        Flux<SensorData> data = sensorDataRepository.findByDeviceIdAndTimestampAfter("D1", cal.getTime());


       return Mono.from(data.collectList().flatMap(dataAll -> {
            List<SensorData> data1 = new ArrayList<>();
            for (SensorData sd : dataAll) {
                data1.add(sd);
            }

            return Mono.just(this.getSensorData(data1));
        }));

    }

    public Mono<UserDto> getPlantStatusForDevice(String  device) {

       Mono<SensorData> data = sensorDataRepository.findTopByOrderByIdDesc();
       return data.map(x -> this.getSensorData(x));
    }

    private UserDto getSensorData(List<SensorData> in) {
        SensorData last = null;
        Float soil = 0f;
        Float temp = 0f;
        Float hum = 0f;
        Float light = 0f;
        int count = 0;
        for (SensorData sd : in) {
            soil += sd.getSoilMoisture();
            soil += sd.getSoilMoisture();
            temp += sd.getTemperature();
            hum += sd.getHumidity();
            light += sd.getLightStrength();
            count++;
            last = sd;
        }

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("RashmiW");

        DeviceDto summary = new DeviceDto();
        summary.setAvgHumidity(BigDecimal.valueOf(hum / count). setScale(2, BigDecimal. ROUND_HALF_DOWN).floatValue());
        summary.setAvgTemp(BigDecimal.valueOf(temp / count). setScale(2, BigDecimal. ROUND_HALF_DOWN).floatValue());
        summary.setAvgSoilMoisture(BigDecimal.valueOf(soil / count). setScale(2, BigDecimal. ROUND_HALF_DOWN).floatValue());
        summary.setAvgLight(BigDecimal.valueOf(light / count). setScale(2, BigDecimal. ROUND_HALF_DOWN).floatValue());
        summary.setDescription("Flowering Plant");
        summary.setId("Plant D1");
        summary.setName("Plant D1");

        PlantStatusDeterminer determiner = new PlantStatusDeterminer();
        Pair<String, Integer>  d1 = determiner.determinePlantStatus(last, "Flowering Plant");

        summary.setCondition(d1.getSecond());
        summary.setStatus(d1.getFirst());

        userDto.getDevices().add(summary);
        return userDto;
    }

    private UserDto getSensorData(SensorData in) {


        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("RashmiW");

        DeviceDto summary = new DeviceDto();
        summary.setAvgHumidity(BigDecimal.valueOf(in.getHumidity()). setScale(2, BigDecimal. ROUND_HALF_DOWN).floatValue());
        summary.setAvgTemp(BigDecimal.valueOf(in.getTemperature()). setScale(2, BigDecimal. ROUND_HALF_DOWN).floatValue());
        summary.setAvgSoilMoisture(BigDecimal.valueOf(in.getSoilMoisture()). setScale(2, BigDecimal. ROUND_HALF_DOWN).floatValue());
        summary.setAvgLight(BigDecimal.valueOf(in.getLightStrength()). setScale(2, BigDecimal. ROUND_HALF_DOWN).floatValue());
        summary.setDescription("Flowering Plant");
        summary.setId("Plant D1");
        summary.setName("Plant D1");

        PlantStatusDeterminer determiner = new PlantStatusDeterminer();
        Pair<String, Integer>  d1 = determiner.determinePlantStatus(in, "Flowering Plant");

        summary.setCondition(d1.getSecond());
        summary.setStatus(d1.getFirst());

        userDto.getDevices().add(summary);
        return userDto;
    }
}
