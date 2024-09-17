package com.example.swapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

public class Starship {
  @JsonProperty("MGLT")
  public String mGLT;
  public String cargo_capacity;
  public String consumables;
  public String cost_in_credits;
  public Date created;
  public String crew;
  public Date edited;
  public String hyperdrive_rating;
  public String length;
  public String manufacturer;
  public String max_atmosphering_speed;
  public String model;
  public String name;
  public String passengers;
  public ArrayList<String> films;
  public ArrayList<Object> pilots;
  public String starship_class;
  public String url;
}
