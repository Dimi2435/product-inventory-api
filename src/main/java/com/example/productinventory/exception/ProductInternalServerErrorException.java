package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/** Exception thrown when an internal server error occurs related to product operations. */
public class ProductInternalServerErrorException extends ProductException {

  public ProductInternalServerErrorException(String message) {
    super(
        message,
        HttpStatus.INTERNAL_SERVER_ERROR,
        "PRODUCT_INTERNAL_SERVER_ERROR",
        "An internal server error occurred while processing the product operation");
  }
}
