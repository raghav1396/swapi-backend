package com.example.swapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Map<String, Object>> handleResourceBadRequestException(
    BadRequestException ex, WebRequest request) {

    log.error("Resource not found exception: {}", ex.getMessage());
    Map<String, Object> errorDetails = new HashMap<>();
    errorDetails.put("timestamp", LocalDateTime.now());
    errorDetails.put("message", ex.getMessage());
    errorDetails.put("status", HttpStatus.BAD_REQUEST.value());

    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(
    BadRequestException ex, WebRequest request) {

    log.error("Resource not found exception: {}", ex.getMessage());
    Map<String, Object> errorDetails = new HashMap<>();
    errorDetails.put("timestamp", LocalDateTime.now());
    errorDetails.put("message", ex.getMessage());
    errorDetails.put("status", HttpStatus.NOT_FOUND.value());

    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGlobalException(
    Exception ex, WebRequest request) {

    log.error("An error occurred: {}", ex.getMessage());
    Map<String, Object> errorDetails = new HashMap<>();
    errorDetails.put("timestamp", LocalDateTime.now());
    errorDetails.put("message", "An unexpected error occurred");
    errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

