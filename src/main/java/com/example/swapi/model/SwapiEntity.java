package com.example.swapi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@JsonDeserialize(using = SwapiEntityDeserializer.class)
public class SwapiEntity {
  private String name;
  private int id;
  private List<Integer> films;
}
