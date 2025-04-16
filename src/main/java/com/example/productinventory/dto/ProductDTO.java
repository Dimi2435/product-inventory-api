package com.example.productinventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for Product entity. Used for transferring product data between layers
 * of the application. Contains validation constraints and OpenAPI documentation.
 */
@Data
@Schema(description = "Product data transfer object")
public class ProductDTO {

  /** The name of the product. Must be between 2 and 100 characters and is required. */
  @NotEmpty(message = "Product name is required.")
  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
  @Schema(description = "Product name", example = "Premium Laptop", required = true)
  private String name;

  /** A brief description of the product. Cannot exceed 500 characters. */
  @Size(max = 500, message = "Description cannot exceed 500 characters")
  @Schema(description = "Product description", example = "High-performance laptop with 16GB RAM")
  private String description;

  /** The price of the product. Must be greater than 0 and is required. */
  @NotNull(message = "Product price is required.")
  @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
  @Schema(description = "Product price", example = "999.99", required = true)
  private BigDecimal price;

  /** The quantity of the product in stock. Must be non-negative and is required. */
  @NotNull(message = "Product quantity is required.")
  @Min(value = 0, message = "Quantity must be non-negative")
  @Schema(description = "Product quantity in stock", example = "10", required = true)
  private Integer quantity;

  /** The ID of the category to which the product belongs. This field is optional. */
  @Schema(description = "Category ID (optional)", example = "1")
  private Long categoryId;

  /**
   * The Stock Keeping Unit (SKU) of the product. Must be alphanumeric and can include dashes. This
   * field is required.
   */
  @NotEmpty(message = "Product SKU is required.")
  @Pattern(regexp = "^[A-Z0-9-]+$", message = "SKU must be alphanumeric and can include dashes.")
  @Schema(description = "Product SKU (Stock Keeping Unit)", example = "LAP-001", required = true)
  private String sku;

  /** The weight of the product in kilograms. This field is required. */
  @NotNull(message = "Weight is required")
  @Schema(description = "Product weight in kilograms", example = "2.5", required = true)
  private BigDecimal weight;

  /** The dimensions of the product in the format 'LxWxH' in centimeters. This field is required. */
  @NotBlank(message = "Dimensions are required")
  @Schema(
    description = "Product dimensions in format 'LxWxH' in centimeters",
    example = "30x20x5",
    required = true
  )
  private String dimensions;

  /**
   * The version of the product for optimistic locking. This field is used to manage concurrent
   * updates.
   */
  private Integer version; // Include version for updates
}
