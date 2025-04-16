package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when there is a conflict with a product operation, such as attempting to create
 * a product with a duplicate SKU.
 */
public class ProductConflictException extends ProductException {

  /**
   * Constructs a ProductConflictException with the specified message.
   *
   * @param message the detail message explaining the reason for the conflict
   */
  public ProductConflictException(String message) {
    super(
        message,
        HttpStatus.CONFLICT,
        "PRODUCT_CONFLICT",
        "A conflict occurred while processing the product operation");
  }

  /**
   * Constructs a ProductConflictException for a product with the specified ID.
   *
   * @param id the ID of the product that caused the conflict
   */
  public ProductConflictException(Long id) {
    this("A product with ID " + id + " already exists.");
  }
}
