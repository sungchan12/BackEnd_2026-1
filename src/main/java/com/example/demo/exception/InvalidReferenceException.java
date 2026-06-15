package com.example.demo.exception;

public class InvalidReferenceException extends RuntimeException {
  public InvalidReferenceException(String message) {
    super(message);
  }
}