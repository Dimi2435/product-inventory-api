package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when there is a conflict with a product operation, such as attempting to create
 * a product with a duplicate SKU.
 */
public class ProductConflictException extends ProductException {

  public ProductConflictException(String message) {
    super(
        message,
        HttpStatus.CONFLICT,
        "PRODUCT_CONFLICT",
        "A conflict occurred while processing the product operation");
  }

  public ProductConflictException(Long id) {
    this("A product with ID " + id + " already exists.");
  }
}
