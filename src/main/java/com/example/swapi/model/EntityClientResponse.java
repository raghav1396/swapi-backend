package com.example.swapi.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EntityClientResponse {
  private String name;
  private int id;
  private List<String> films;
}
