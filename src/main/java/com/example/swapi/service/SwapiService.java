package com.example.swapi.service;

import com.example.swapi.feignClient.SwapiClient;
import com.example.swapi.model.ClientResponse;
import com.example.swapi.model.EntityClientResponse;
import com.example.swapi.model.SwapiEntity;
import com.example.swapi.model.SwapiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SwapiService {

  private final SwapiClient swapiClient;
  private final FilmCacheService filmCacheService;

  public ClientResponse<EntityClientResponse> getResource(String path, String name, Integer page) {
    SwapiResponse<SwapiEntity> data = swapiClient.getResource(path, name, page);
    filmCacheService.addUniqueFilmsToCacheAsync(data);
    var clientResponseData = data.getResults()
      .stream()
      .map(resource ->
        EntityClientResponse.builder()
          .id(resource.getId())
          .name(resource.getName())
          .films(resource.getFilms().stream()
            .map(filmCacheService::getFilmTitleById)
            .toList())
          .build())
      .toList();

    return new ClientResponse<>(
      data.getCount(),
      clientResponseData,
      mapUrl(path, data.getNext()),
      mapUrl(path, data.getPrevious())
    );
  }

  private String mapUrl(String type, String url) {
    if (url == null) return null;
    String[] params = url.substring(url.indexOf("?")).split("&");
    String pageNumber = "0";
    String searchTerm = "";

    for (String param : params) {
      String[] kvSplit = param.split("=");
      String name = kvSplit[0];
      String value = kvSplit.length > 1 ? kvSplit[1] : "";
      if (name.equals("search")) searchTerm = value;
      else if (name.equals("page")) pageNumber = value;
    }

    return String.format("/api/v1/%s?name=%s&page=%s", type, searchTerm, pageNumber);
  }
}
