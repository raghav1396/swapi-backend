package com.example.swapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientResponse<T> {
  private int count;
  private List<T> data;
  private String next;
  private String previous;
}
