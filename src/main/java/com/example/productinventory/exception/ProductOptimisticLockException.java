package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an optimistic locking conflict occurs during product updates. This
 * indicates that the product was modified by another transaction since it was read.
 */
public class ProductOptimisticLockException extends ProductException {

  /**
   * Constructs a ProductOptimisticLockException with the specified message.
   *
   * @param message the detail message explaining the reason for the optimistic lock conflict
   */
  public ProductOptimisticLockException(String message) {
    super(
        message,
        HttpStatus.CONFLICT,
        "PRODUCT_OPTIMISTIC_LOCK_ERROR",
        "The product was modified by another transaction");
  }

  /**
   * Constructs a ProductOptimisticLockException for a product with the specified ID, expected
   * version, and actual version.
   *
   * @param id the ID of the product that caused the conflict
   * @param expectedVersion the version expected by the client
   * @param actualVersion the actual version found in the database
   */
  public ProductOptimisticLockException(Long id, Integer expectedVersion, Integer actualVersion) {
    this(
        String.format(
            "Product with ID %d has been modified. Expected version %d, but found version %d",
            id, expectedVersion, actualVersion));
  }
}
