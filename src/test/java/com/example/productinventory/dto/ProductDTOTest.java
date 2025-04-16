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

/** Unit tests for the {@link ProductDTO} data transfer object. */
public class ProductDTOTest {

  private Validator validator;

  private static final String VALID_NAME = "Valid Product";
  private static final String VALID_DESCRIPTION = "A valid product description.";
  private static final BigDecimal VALID_PRICE = BigDecimal.valueOf(10.00);
  private static final int VALID_QUANTITY = 5;
  private static final String VALID_SKU = "VALID-SKU";
  private static final BigDecimal VALID_WEIGHT = BigDecimal.valueOf(1.5);
  private static final String VALID_DIMENSIONS = "30x20x5";

  /** Set up the validator before each test. */
  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /** Test that a valid ProductDTO passes validation. */
  @Test
  public void testValidProductDTO() {
    ProductDTO productDTO = createValidProductDTO();

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
    assertTrue(violations.isEmpty(), "ProductDTO should be valid");
  }

  /** Test that a ProductDTO without a name fails validation. */
  @Test
  public void testInvalidProductDTO_NoName() {
    ProductDTO productDTO = createValidProductDTO();
    productDTO.setName(null); // Remove name to make it invalid

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
    assertEquals(1, violations.size());
    assertEquals("Product name is required.", violations.iterator().next().getMessage());
  }

  /** Test that a ProductDTO with a negative price fails validation. */
  @Test
  public void testInvalidProductDTO_NegativePrice() {
    ProductDTO productDTO = createValidProductDTO();
    productDTO.setPrice(BigDecimal.valueOf(-10.00)); // Invalid price

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
    assertEquals(1, violations.size());
    assertEquals("Price must be greater than 0", violations.iterator().next().getMessage());
  }

  /** Test that a ProductDTO with an invalid SKU fails validation. */
  @Test
  public void testInvalidProductDTO_InvalidSKU() {
    ProductDTO productDTO = createValidProductDTO();
    productDTO.setSku("INVALID SKU"); // Invalid SKU

    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
    assertEquals(1, violations.size());
    assertEquals(
        "SKU must be alphanumeric and can include dashes.",
        violations.iterator().next().getMessage());
  }

  /**
   * Creates and returns a valid ProductDTO for testing.
   *
   * @return a valid ProductDTO
   */
  private ProductDTO createValidProductDTO() {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setName(VALID_NAME);
    productDTO.setDescription(VALID_DESCRIPTION);
    productDTO.setPrice(VALID_PRICE);
    productDTO.setQuantity(VALID_QUANTITY);
    productDTO.setSku(VALID_SKU);
    productDTO.setWeight(VALID_WEIGHT);
    productDTO.setDimensions(VALID_DIMENSIONS);
    return productDTO;
  }
}
