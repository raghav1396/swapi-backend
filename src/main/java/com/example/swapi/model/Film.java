package com.example.swapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
  private ArrayList<String> characters;
  private Date created;
  private String director;
  private Date edited;
  private int episode_id;
  private String opening_crawl;
  private ArrayList<String> planets;
  private String producer;
  private String release_date;
  private ArrayList<String> species;
  private ArrayList<String> starships;
  private String title;
  private String url;
  private ArrayList<String> vehicles;
}
