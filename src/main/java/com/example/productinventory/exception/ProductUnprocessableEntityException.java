package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

public class ProductUnprocessableEntityException extends ProductException {
  public ProductUnprocessableEntityException(String message) {
    super(message, HttpStatus.UNPROCESSABLE_ENTITY, "PRODUCT_UNPROCESSABLE_ENTITY", message);
  }
}
