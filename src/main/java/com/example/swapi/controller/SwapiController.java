package com.example.swapi.controller;

import com.example.swapi.exception.NotFoundException;
import com.example.swapi.model.ClientResponse;
import com.example.swapi.model.EntityClientResponse;
import com.example.swapi.service.SwapiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class SwapiController {

  private final SwapiService swapiService;

  private static final Set<String> availablePaths = Set.of("people", "starships", "vehicles", "species", "planets");

  @Operation(summary = "Get details based on type and id",
    description = "Fetches details for the specified entity type and it's name",
    parameters = {
      @Parameter(
        name = "type",
        in = ParameterIn.PATH,
        description = "The type of entity",
        schema = @Schema(
          allowableValues = {"people", "starships", "vehicles", "species", "planets"}
        )
      )
    })
  @GetMapping(value = "{type}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ClientResponse<EntityClientResponse>> getEntity(
    @PathVariable("type") String type,
    @RequestParam("name") String name,
    @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber
  ) {
    if (!availablePaths.contains(type)) {
      throw new NotFoundException(String.format("type can only be one of %s, while value %s was provided", availablePaths, type));
    }
    log.info("Request received for {} with name {}", type, name);
    ClientResponse<EntityClientResponse> apiResponse = swapiService.getResource(type, name, pageNumber);
    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
  }
}
