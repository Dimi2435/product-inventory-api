package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an internal server error occurs related to product operations. This
 * exception indicates that an unexpected error has occurred while processing a product-related
 * request.
 */
public class ProductInternalServerErrorException extends ProductException {

  /**
   * Constructs a ProductInternalServerErrorException with the specified message.
   *
   * @param message the detail message explaining the reason for the internal server error
   */
  public ProductInternalServerErrorException(String message) {
    super(
        message,
        HttpStatus.INTERNAL_SERVER_ERROR,
        "PRODUCT_INTERNAL_SERVER_ERROR",
        "An internal server error occurred while processing the product operation");
  }
}
