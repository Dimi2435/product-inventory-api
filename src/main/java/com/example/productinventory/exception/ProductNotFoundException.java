package com.example.productinventory.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a product cannot be found in the system. This could be due to an invalid
 * ID, SKU, or other search criteria.
 */
public class ProductNotFoundException extends ProductException {

  /**
   * Constructs a ProductNotFoundException with the specified message.
   *
   * @param message the detail message explaining the reason for the product not being found
   */
  public ProductNotFoundException(String message) {
    super(
        message,
        HttpStatus.NOT_FOUND,
        "PRODUCT_NOT_FOUND",
        "The requested product could not be found in the system");
  }

  /**
   * Constructs a ProductNotFoundException for a product with the specified ID.
   *
   * @param id the ID of the product that could not be found
   */
  public ProductNotFoundException(Long id) {
    this("Product with ID " + id + " not found");
  }

  /**
   * Constructs a ProductNotFoundException for a product with the specified field and value.
   *
   * @param field the field that was used to search for the product (e.g., SKU)
   * @param value the value of the field that was searched
   */
  public ProductNotFoundException(String field, String value) {
    super(
        "Product with " + field + " '" + value + "' not found",
        HttpStatus.NOT_FOUND,
        "PRODUCT_NOT_FOUND",
        "No product found with " + field + " = " + value);
  }
}
