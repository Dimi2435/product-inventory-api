package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an optimistic locking conflict occurs during product updates. This
 * indicates that the product was modified by another transaction since it was read.
 */
public class ProductOptimisticLockException extends ProductException {

  public ProductOptimisticLockException(String message) {
    super(
        message,
        HttpStatus.CONFLICT,
        "PRODUCT_OPTIMISTIC_LOCK_ERROR",
        "The product was modified by another transaction");
  }

  public ProductOptimisticLockException(Long id, Integer expectedVersion, Integer actualVersion) {
    this(
        String.format(
            "Product with ID %d has been modified. Expected version %d, but found version %d",
            id, expectedVersion, actualVersion));
  }
}
