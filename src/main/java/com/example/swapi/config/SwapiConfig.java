package com.example.swapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "swapi-client")
@Getter
@Setter
public class SwapiConfig {
  private String name;
  private String base;
  private String peoplePath;
  private String filmPath;
  private String starshipPath;
  private String speciePath;
  private String planetPath;
  private String vehiclePath;
}
