package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a bad request occurs, such as invalid input for product creation. This
 * exception indicates that the client has made an invalid request.
 */
public class ProductBadRequestException extends ProductException {

  /**
   * Constructs a ProductBadRequestException with the specified message.
   *
   * @param message the detail message explaining the reason for the bad request
   */
  public ProductBadRequestException(String message) {
    super(
        message,
        HttpStatus.BAD_REQUEST,
        "PRODUCT_BAD_REQUEST",
        "The request for the product is invalid");
  }
}
