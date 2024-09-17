package com.example.swapi.feignClient;


import com.example.swapi.model.Film;
import com.example.swapi.model.SwapiEntity;
import com.example.swapi.model.SwapiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${swapi-client.name}",
  url = "${swapi-client.base}"
)
public interface SwapiClient {

  @GetMapping(value = "{path}", produces = MediaType.APPLICATION_JSON_VALUE)
  SwapiResponse<SwapiEntity> getResource(
    @PathVariable(value = "path") String path,
    @RequestParam(value = "search") String name,
    @RequestParam(value = "page") Integer page
  );

  @GetMapping(value = "{path}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  Film getResourceById(
    @PathVariable(value = "path") String path,
    @PathVariable(value = "id") int id
  );


}
