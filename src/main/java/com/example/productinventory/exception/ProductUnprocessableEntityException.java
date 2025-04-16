package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a product cannot be processed due to validation errors or other issues.
 * This exception indicates that the request was well-formed but could not be followed due to
 * semantic errors.
 */
public class ProductUnprocessableEntityException extends ProductException {
  /**
   * Constructs a ProductUnprocessableEntityException with the specified message.
   *
   * @param message the detail message explaining the reason for the unprocessable entity
   */
  public ProductUnprocessableEntityException(String message) {
    super(message, HttpStatus.UNPROCESSABLE_ENTITY, "PRODUCT_UNPROCESSABLE_ENTITY", message);
  }
}
