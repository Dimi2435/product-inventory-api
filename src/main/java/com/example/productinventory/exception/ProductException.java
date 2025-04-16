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

  /**
   * Constructs a ProductException with the specified parameters.
   *
   * @param message the detail message explaining the reason for the exception
   * @param status the HTTP status associated with the exception
   * @param errorCode the error code for the exception
   * @param details additional details about the exception
   */
  protected ProductException(String message, HttpStatus status, String errorCode, String details) {
    super(message);
    this.status = status;
    this.errorCode = errorCode;
    this.details = details;
  }

  /**
   * Returns the HTTP status associated with the exception.
   *
   * @return the HTTP status
   */
  public HttpStatus getStatus() {
    return status;
  }

  /**
   * Returns the error code for the exception.
   *
   * @return the error code
   */
  public String getErrorCode() {
    return errorCode;
  }

  /**
   * Returns additional details about the exception.
   *
   * @return the details of the exception
   */
  public String getDetails() {
    return details;
  }
}
