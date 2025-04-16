package com.example.productinventory.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductDTOTest {

  private Validator validator;

  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void testValidProductDTO() {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setName("Valid Product");
    productDTO.setDescription("A valid product description.");
    productDTO.setPrice(BigDecimal.valueOf(10.00));
    productDTO.setQuantity(5);
    productDTO.setSku("VALID-SKU");
    productDTO.setWeight(BigDecimal.valueOf(1.5));
    productDTO.setDimensions("30x20x5");

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
    assertTrue(violations.isEmpty(), "ProductDTO should be valid");
  }

  @Test
  public void testInvalidProductDTO_NoName() {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setDescription("A valid product description.");
    productDTO.setPrice(BigDecimal.valueOf(10.00));
    productDTO.setQuantity(5);
    productDTO.setSku("VALID-SKU");
    productDTO.setWeight(BigDecimal.valueOf(1.5));
    productDTO.setDimensions("30x20x5");

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
    assertEquals(1, violations.size());
    assertEquals("Product name is required.", violations.iterator().next().getMessage());
  }

  @Test
  public void testInvalidProductDTO_NegativePrice() {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setName("Valid Product");
    productDTO.setDescription("A valid product description.");
    productDTO.setPrice(BigDecimal.valueOf(-10.00)); // Invalid price
    productDTO.setQuantity(5);
    productDTO.setSku("VALID-SKU");
    productDTO.setWeight(BigDecimal.valueOf(1.5));
    productDTO.setDimensions("30x20x5");

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
    assertEquals(1, violations.size());
    assertEquals("Price must be greater than 0", violations.iterator().next().getMessage());
  }

  @Test
  public void testInvalidProductDTO_InvalidSKU() {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setName("Valid Product");
    productDTO.setDescription("A valid product description.");
    productDTO.setPrice(BigDecimal.valueOf(10.00));
    productDTO.setQuantity(5);
    productDTO.setSku("INVALID SKU"); // Invalid SKU
    productDTO.setWeight(BigDecimal.valueOf(1.5));
    productDTO.setDimensions("30x20x5");

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
    assertEquals(1, violations.size());
    assertEquals(
        "SKU must be alphanumeric and can include dashes.",
        violations.iterator().next().getMessage());
  }
}
