package com.example.productinventory.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link Product} model class. */
public class ProductTest {

  private Validator validator;

  /** Set up the validator before each test. */
  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /** Test that a valid product passes validation. */
  @Test
  public void testValidProduct() {
    Product product = new Product();
    product.setName("Valid Product");
    product.setDescription("A valid product description.");
    product.setPrice(BigDecimal.valueOf(10.00));
    product.setQuantity(5);
    product.setSku("VALID-SKU");
    product.setWeight(BigDecimal.valueOf(1.5));
    product.setDimensions("30x20x5");
    product.setCreatedAt(LocalDateTime.now());
    product.setUpdatedAt(LocalDateTime.now());

    Set<ConstraintViolation<Product>> violations = validator.validate(product);
    assertTrue(violations.isEmpty(), "Product should be valid");
  }

  /** Test that a product without a name fails validation. */
  @Test
  public void testInvalidProduct_NoName() {
    Product product = new Product();
    product.setDescription("A valid product description.");
    product.setPrice(BigDecimal.valueOf(10.00));
    product.setQuantity(5);
    product.setSku("VALID-SKU");
    product.setWeight(BigDecimal.valueOf(1.5));
    product.setDimensions("30x20x5");

    Set<ConstraintViolation<Product>> violations = validator.validate(product);
    assertEquals(1, violations.size());
    assertEquals("Name is required", violations.iterator().next().getMessage());
  }

  /** Test that a product with a negative price fails validation. */
  @Test
  public void testInvalidProduct_NegativePrice() {
    Product product = new Product();
    product.setName("Valid Product");
    product.setDescription("A valid product description.");
    product.setPrice(BigDecimal.valueOf(-10.00)); // Invalid price
    product.setQuantity(5);
    product.setSku("VALID-SKU");
    product.setWeight(BigDecimal.valueOf(1.5));
    product.setDimensions("30x20x5");

    Set<ConstraintViolation<Product>> violations = validator.validate(product);
    assertEquals(1, violations.size());
    assertEquals(
        "Product price must be greater than zero.", violations.iterator().next().getMessage());
  }

  /** Test that a product with an invalid SKU fails validation. */
  @Test
  public void testInvalidProduct_InvalidSKU() {
    Product product = new Product();
    product.setName("Valid Product");
    product.setDescription("A valid product description.");
    product.setPrice(BigDecimal.valueOf(10.00));
    product.setQuantity(5);
    product.setSku("INVALID SKU"); // Invalid SKU
    product.setWeight(BigDecimal.valueOf(1.5));
    product.setDimensions("30x20x5");

    Set<ConstraintViolation<Product>> violations = validator.validate(product);
    assertEquals(1, violations.size());
    assertEquals(
        "SKU must be alphanumeric and can include dashes.",
        violations.iterator().next().getMessage());
  }

  /** Test that a product with a negative quantity fails validation. */
  @Test
  public void testInvalidProduct_NegativeQuantity() {
    Product product = new Product();
    product.setName("Valid Product");
    product.setDescription("A valid product description.");
    product.setPrice(BigDecimal.valueOf(10.00));
    product.setQuantity(-5); // Invalid quantity
    product.setSku("VALID-SKU");
    product.setWeight(BigDecimal.valueOf(1.5));
    product.setDimensions("30x20x5");

    Set<ConstraintViolation<Product>> violations = validator.validate(product);

    // Assert that there is exactly one violation
    assertEquals(1, violations.size(), "There should be one violation for negative quantity.");

    // Assert that the violation is specifically for the quantity
    ConstraintViolation<Product> violation = violations.iterator().next();
    assertEquals("Product quantity cannot be negative.", violation.getMessage());
    assertEquals(
        "quantity",
        violation
            .getPropertyPath()
            .toString()); // Check that the violation is for the 'quantity' field
  }
}
