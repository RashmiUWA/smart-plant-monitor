package com.smart.plant.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class SmartPlantMonitorApplication {

  public static void main(String[] args) {
    SpringApplication.run(SmartPlantMonitorApplication.class, args);
  }

}
