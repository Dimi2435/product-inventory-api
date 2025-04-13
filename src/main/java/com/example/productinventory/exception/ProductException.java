package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception class for all product-related exceptions. Provides a consistent structure for
 * error handling across the application.
 */
public abstract class ProductException extends RuntimeException {
  private final HttpStatus status;
  private final String errorCode;
  private final String details;

  protected ProductException(String message, HttpStatus status, String errorCode, String details) {
    super(message);
    this.status = status;
    this.errorCode = errorCode;
    this.details = details;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getDetails() {
    return details;
  }
}
