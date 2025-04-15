package com.example.productinventory.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Product entity representing a product in the inventory system. Includes validation constraints
 * and optimistic locking support.
 */
@Entity
@Table(name = "products")
@Data
@Schema(description = "Product entity representing a product in the inventory system")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Unique identifier of the product", example = "1")
  private Long id;

  @NotBlank(message = "Name is required")
  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
  @Column(nullable = false, length = 100)
  @Schema(description = "Name of the product", example = "Premium Laptop", required = true)
  private String name;

  @Size(max = 500, message = "Description cannot exceed 500 characters")
  @Column(length = 500)
  @Schema(
    description = "Description of the product",
    example = "High-performance laptop with 16GB RAM"
  )
  private String description;

  @NotNull(message = "Price is required")
  @Positive(message = "Product price must be greater than zero.")
  @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
  @Column(nullable = false, precision = 10, scale = 2)
  @Schema(description = "Price of the product", example = "999.99", required = true)
  private BigDecimal price;

  @NotNull(message = "Quantity is required")
  @Positive(message = "Product quantity cannot be negative.")
  @Min(value = 0, message = "Quantity must be non-negative")
  @Column(nullable = false)
  @Schema(description = "Quantity of the product in stock", example = "10", required = true)
  private Integer quantity;

  @NotNull(message = "Product SKU is required.")
  @Column(unique = true, length = 50)
  @Schema(description = "Stock Keeping Unit (SKU) of the product", example = "LAP-001")
  private String sku;

  @Column(precision = 10, scale = 2)
  @Schema(description = "Weight of the product in kilograms", example = "2.5")
  private BigDecimal weight;

  @Column(length = 20)
  @Schema(
    description = "Dimensions of the product in format 'LxWxH' in centimeters",
    example = "30x20x5"
  )
  private String dimensions;

  @Version
  @Column(nullable = false)
  @Schema(description = "Version number for optimistic locking", example = "1")
  private Integer version;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  @Schema(description = "Timestamp when the product was created")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  @Schema(description = "Timestamp when the product was last updated")
  private LocalDateTime updatedAt;

  // Optional: Category relationship
  // @ManyToOne(fetch = FetchType.LAZY)
  // @JoinColumn(name = "category_id")
  // private Category category;
}
