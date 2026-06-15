package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<Map<String, String>> handleDuplicateEmail(DuplicateEmailException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(InvalidReferenceException.class)
  public ResponseEntity<Map<String, String>> handleInvalidReference(InvalidReferenceException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("message", e.getMessage()));
  }
}