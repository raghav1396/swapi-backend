package com.example.swapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class SwapiResponse<T>{
  int count;
  String next;
  String previous;
  List<T> results;
}
