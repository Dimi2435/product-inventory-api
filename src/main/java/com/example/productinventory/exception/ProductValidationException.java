package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when product validation fails. This could be due to invalid data, constraints
 * violation, or business rules.
 */
public class ProductValidationException extends ProductException {

  public ProductValidationException(String message) {
    super(
        message,
        HttpStatus.BAD_REQUEST,
        "PRODUCT_VALIDATION_ERROR",
        "The product data failed validation");
  }

  public ProductValidationException(String message, String details) {
    super(message, HttpStatus.BAD_REQUEST, "PRODUCT_VALIDATION_ERROR", details);
  }
}
