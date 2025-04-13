package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a product cannot be found in the system. This could be due to an invalid
 * ID, SKU, or other search criteria.
 */
public class ProductNotFoundException extends ProductException {

  public ProductNotFoundException(String message) {
    super(
        message,
        HttpStatus.NOT_FOUND,
        "PRODUCT_NOT_FOUND",
        "The requested product could not be found in the system");
  }

  public ProductNotFoundException(Long id) {
    this("Product with ID " + id + " not found");
  }

  public ProductNotFoundException(String field, String value) {
    super(
        "Product with " + field + " '" + value + "' not found",
        HttpStatus.NOT_FOUND,
        "PRODUCT_NOT_FOUND",
        "No product found with " + field + " = " + value);
  }
}
