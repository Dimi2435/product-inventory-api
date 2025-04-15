package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/** Exception thrown when a bad request occurs, such as invalid input for product creation. */
public class ProductBadRequestException extends ProductException {

  public ProductBadRequestException(String message) {
    super(
        message,
        HttpStatus.BAD_REQUEST,
        "PRODUCT_BAD_REQUEST",
        "The request for the product is invalid");
  }
}
