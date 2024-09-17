package com.example.swapi.service;

import com.example.swapi.feignClient.SwapiClient;
import com.example.swapi.model.ClientResponse;
import com.example.swapi.model.EntityClientResponse;
import com.example.swapi.model.SwapiEntity;
import com.example.swapi.model.SwapiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SwapiServiceTest {

  @Mock
  private SwapiClient swapiClient;

  @Mock
  private FilmCacheService filmCacheService;

  @InjectMocks
  private SwapiService swapiService;

  private SwapiResponse<SwapiEntity> mockSwapiResponse;
  private SwapiEntity mockSwapiEntity;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    // Set up mock SwapiEntity
    mockSwapiEntity = SwapiEntity.builder().build();
    mockSwapiEntity.setId(1);
    mockSwapiEntity.setName("Luke Skywalker");
    mockSwapiEntity.setFilms(List.of(1));

    // Set up mock SwapiResponse
    mockSwapiResponse = new SwapiResponse<>();
    mockSwapiResponse.setResults(List.of(mockSwapiEntity));
    mockSwapiResponse.setCount(1);
  }

  @Test
  void testGetResource_ValidResponse_ShouldReturnClientResponse() {
    // Arrange
    String path = "people";
    String name = "Luke Skywalker";
    Integer page = 1;
    String filmTitle = "A New Hope";

    when(swapiClient.getResource(anyString(), anyString(), anyInt())).thenReturn(mockSwapiResponse);
    when(filmCacheService.getFilmTitleById(anyInt())).thenReturn(filmTitle);

    // Act
    ClientResponse<EntityClientResponse> result = swapiService.getResource(path, name, page);

    // Assert
    assertEquals(1, result.getCount());
    assertEquals(1, result.getData().size());
    assertEquals("Luke Skywalker", result.getData().get(0).getName());
    assertEquals(List.of(filmTitle), result.getData().get(0).getFilms());

    verify(swapiClient, times(1)).getResource(path, name, page);
    verify(filmCacheService, times(1)).addUniqueFilmsToCacheAsync(mockSwapiResponse);
    verify(filmCacheService, times(1)).getFilmTitleById(1);
  }

  @Test
  void testGetResource_NoFilms_ShouldReturnEmptyFilmList() {
    // Arrange
    String path = "people";
    String name = "Luke Skywalker";
    Integer page = 1;

    mockSwapiEntity.setFilms(Collections.emptyList());

    when(swapiClient.getResource(anyString(), anyString(), anyInt())).thenReturn(mockSwapiResponse);

    // Act
    ClientResponse<EntityClientResponse> result = swapiService.getResource(path, name, page);

    // Assert
    assertEquals(1, result.getCount());
    assertEquals(1, result.getData().size());
    assertEquals("Luke Skywalker", result.getData().get(0).getName());
    assertEquals(Collections.emptyList(), result.getData().get(0).getFilms());

    verify(swapiClient, times(1)).getResource(path, name, page);
    verify(filmCacheService, times(1)).addUniqueFilmsToCacheAsync(mockSwapiResponse);
    verify(filmCacheService, never()).getFilmTitleById(anyInt());
  }

  @Test
  void testGetResource_EmptyResponse_ShouldReturnEmptyClientResponse() {
    // Arrange
    String path = "people";
    String name = "Unknown";
    Integer page = 1;

    // Empty result setup
    mockSwapiResponse.setResults(Collections.emptyList());
    mockSwapiResponse.setCount(0);

    when(swapiClient.getResource(anyString(), anyString(), anyInt())).thenReturn(mockSwapiResponse);

    // Act
    ClientResponse<EntityClientResponse> result = swapiService.getResource(path, name, page);

    // Assert
    assertEquals(0, result.getCount());
    assertEquals(0, result.getData().size());

    verify(swapiClient, times(1)).getResource(path, name, page);
    verify(filmCacheService, times(1)).addUniqueFilmsToCacheAsync(mockSwapiResponse);
  }

  @Test
  void testGetResource_WithMultipleFilms_ShouldMapAllFilms() {
    // Arrange
    String path = "people";
    String name = "Luke Skywalker";
    Integer page = 1;
    List<Integer> films = List.of(1, 2);

    mockSwapiEntity.setFilms(films);

    when(swapiClient.getResource(anyString(), anyString(), anyInt())).thenReturn(mockSwapiResponse);
    when(filmCacheService.getFilmTitleById(1)).thenReturn("A New Hope");
    when(filmCacheService.getFilmTitleById(2)).thenReturn("The Empire Strikes Back");

    // Act
    ClientResponse<EntityClientResponse> result = swapiService.getResource(path, name, page);

    // Assert
    assertEquals(1, result.getCount());
    assertEquals(1, result.getData().size());
    assertEquals("Luke Skywalker", result.getData().get(0).getName());
    assertEquals(List.of("A New Hope", "The Empire Strikes Back"), result.getData().get(0).getFilms());

    verify(swapiClient, times(1)).getResource(path, name, page);
    verify(filmCacheService, times(1)).addUniqueFilmsToCacheAsync(mockSwapiResponse);
    verify(filmCacheService, times(1)).getFilmTitleById(1);
    verify(filmCacheService, times(1)).getFilmTitleById(1);
  }
}
