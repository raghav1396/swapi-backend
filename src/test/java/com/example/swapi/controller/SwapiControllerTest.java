package com.example.swapi.controller;

import com.example.swapi.exception.NotFoundException;
import com.example.swapi.model.ClientResponse;
import com.example.swapi.model.EntityClientResponse;
import com.example.swapi.service.SwapiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SwapiControllerTest {

  @Mock
  private SwapiService swapiService;

  @InjectMocks
  private SwapiController swapiController;

  private ClientResponse<EntityClientResponse> mockClientResponse;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    // Initialize the mock response
    mockClientResponse = new ClientResponse<>();
    EntityClientResponse entityClientResponse = EntityClientResponse.builder().build();
    mockClientResponse.setData(List.of(entityClientResponse));
    mockClientResponse.setCount(1);
  }


  @Test
  void testGetEntity_ValidType_ShouldReturnOk() {
    String type = "people";
    String name = "Luke Skywalker";
    Integer pageNumber = 1;

    when(swapiService.getResource(anyString(), anyString(), anyInt())).thenReturn(mockClientResponse);

    ResponseEntity<ClientResponse<EntityClientResponse>> response = swapiController.getEntity(type, name, pageNumber);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(swapiService, times(1)).getResource(type, name, pageNumber);
  }

  @Test
  void testGetEntity_InvalidType_ShouldThrowNotFoundException() {
    String invalidType = "invalidType";
    String name = "Luke Skywalker";
    Integer pageNumber = 1;

    assertThrows(NotFoundException.class, () ->
      swapiController.getEntity(invalidType, name, pageNumber));

    verify(swapiService, never()).getResource(anyString(), anyString(), anyInt());
  }

  @Test
  void testGetEntity_ServiceReturnsNull_ShouldReturnOkWithNullBody() {
    String type = "species";
    String name = "Yoda";
    Integer pageNumber = 1;

    when(swapiService.getResource(anyString(), anyString(), anyInt())).thenReturn(null);

    ResponseEntity<ClientResponse<EntityClientResponse>> response = swapiController.getEntity(type, name, pageNumber);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNull(response.getBody());
    verify(swapiService, times(1)).getResource(type, name, pageNumber);
  }
}
