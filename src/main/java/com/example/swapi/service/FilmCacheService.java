package com.example.swapi.service;

import com.example.swapi.config.SwapiConfig;
import com.example.swapi.exception.ServiceUnavailableException;
import com.example.swapi.feignClient.SwapiClient;
import com.example.swapi.model.Film;
import com.example.swapi.model.SwapiEntity;
import com.example.swapi.model.SwapiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@AllArgsConstructor
@Slf4j
public class FilmCacheService {

  private final SwapiClient swapiClient;
  private final SwapiConfig swapiConfig;
  private static final ConcurrentMap<Integer, Film> cachedFilms = new ConcurrentHashMap<>();

  public void addUniqueFilmsToCacheAsync(SwapiResponse<SwapiEntity> data) {
    List<CompletableFuture<Void>> filmFutures = data.getResults().stream()
      .map(SwapiEntity::getFilms)
      .flatMap(List::stream)
      .distinct()
      .filter(filmId -> !cachedFilms.containsKey(filmId))
      .map(this::getFilmByIdAsync)
      .toList();

    CompletableFuture.allOf(filmFutures.toArray(new CompletableFuture[0])).join();
  }

  private CompletableFuture<Void> getFilmByIdAsync(int id) {
    return CompletableFuture.runAsync(() -> {
      if (cachedFilms.containsKey(id))
        return;
      try {
        Film resourceById = swapiClient.getResourceById(swapiConfig.getFilmPath(), id);
        cachedFilms.put(id, resourceById);
      } catch (Exception e) {
        throw new ServiceUnavailableException("Unable to fetch data for the film with id " + id);
      }
    });
  }

  public String getFilmTitleById(int id) {
    Film film = cachedFilms.get(id);
    return (film != null) ? film.getTitle() : "Unknown";
  }
}
