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

  @NotEmpty(message = "Product name is required.")
  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
  @Schema(description = "Product name", example = "Premium Laptop", required = true)
  private String name;

  @Size(max = 500, message = "Description cannot exceed 500 characters")
  @Schema(description = "Product description", example = "High-performance laptop with 16GB RAM")
  private String description;

  @NotNull(message = "Product price is required.")
  @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
  @Schema(description = "Product price", example = "999.99", required = true)
  private BigDecimal price;

  @NotNull(message = "Product quantity is required.")
  @Min(value = 0, message = "Quantity must be non-negative")
  @Schema(description = "Product quantity in stock", example = "10", required = true)
  private Integer quantity;

  @Schema(description = "Category ID (optional)", example = "1")
  private Long categoryId;

  @NotEmpty(message = "Product SKU is required.")
  @Pattern(regexp = "^[A-Z0-9-]+$", message = "SKU must be alphanumeric and can include dashes.")
  @Schema(description = "Product SKU (Stock Keeping Unit)", example = "LAP-001", required = true)
  private String sku;

  @NotNull(message = "Weight is required")
  @Schema(description = "Product weight in kilograms", example = "2.5", required = true)
  private BigDecimal weight;

  @NotBlank(message = "Dimensions are required")
  @Schema(
    description = "Product dimensions in format 'LxWxH' in centimeters",
    example = "30x20x5",
    required = true
  )
  private String dimensions;
}
