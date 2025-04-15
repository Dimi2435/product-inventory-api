package com.example.productinventory.controller;

import com.example.productinventory.dto.ProductDTO;
import com.example.productinventory.exception.ProductBadRequestException;
import com.example.productinventory.exception.ProductConflictException;
import com.example.productinventory.exception.ProductInternalServerErrorException;
import com.example.productinventory.exception.ProductNotFoundException;
import com.example.productinventory.model.Product;
import com.example.productinventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Controller", description = "APIs for managing products")
public class ProductController {

  private final ProductService productService;
  private final Logger logger = LoggerFactory.getLogger(ProductController.class);

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  @Operation(
    summary = "Create a new product",
    description = "Creates a new product with the provided details"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "201",
        description = "Product created successfully",
        content = @Content(schema = @Schema(implementation = Product.class))
      ),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "401", description = "Authentication required"),
      @ApiResponse(responseCode = "403", description = "User does not have permission"),
      @ApiResponse(responseCode = "404", description = "Related resource not found"),
      @ApiResponse(responseCode = "409", description = "Conflict with current state"),
      @ApiResponse(responseCode = "422", description = "Validation errors"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
    }
  )
  public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
    logger.info("Creating product: {}", productDTO);

    // Validate input fields
    if (productDTO.getName() == null || productDTO.getName().isEmpty()) {
      logger.warn("Invalid input: Product name is required.");
      throw new ProductBadRequestException("Product name is required.");
    }
    if (productDTO.getSku() == null || productDTO.getSku().isEmpty()) {
      logger.warn("Invalid input: Product SKU is required.");
      throw new ProductBadRequestException("Product SKU is required.");
    }
    if (productDTO.getPrice() == null || productDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
      logger.warn("Invalid input: Product price must be greater than zero.");
      throw new ProductBadRequestException("Product price must be greater than zero.");
    }
    if (productDTO.getQuantity() == null || productDTO.getQuantity() < 0) {
      logger.warn("Invalid input: Product quantity cannot be negative.");
      throw new ProductBadRequestException("Product quantity cannot be negative.");
    }

    // Check for duplicate SKU
    if (productService.existsBySku(productDTO.getSku())) {
      logger.warn("Conflict: Product with SKU {} already exists", productDTO.getSku());
      throw new ProductConflictException(
          "A product with SKU " + productDTO.getSku() + " already exists.");
    }

    try {
      Product createdProduct = productService.createProduct(productDTO);
      logger.info("Product created successfully: {}", createdProduct);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    } catch (ProductBadRequestException e) {
      logger.error("Bad request error: {}", e.getMessage());
      throw e; // Rethrow to be handled by the global exception handler
    } catch (ProductConflictException e) {
      logger.error("Conflict error: {}", e.getMessage());
      throw e; // Rethrow to be handled by the global exception handler
    } catch (Exception e) {
      logger.error("Unexpected error occurred while creating product: {}", e.getMessage(), e);
      throw new ProductInternalServerErrorException(
          "An unexpected error occurred while creating the product.");
    }
  }

  @GetMapping
  @Operation(
    summary = "Get all products",
    description = "Retrieves a paginated list of all products"
  )
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved products"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
    }
  )
  public ResponseEntity<Page<Product>> getAllProducts(
      @Parameter(description = "Page number (0-based)", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Number of items per page", example = "10")
          @RequestParam(defaultValue = "10")
          int size,
      @Parameter(description = "Sort field", example = "name") @RequestParam(defaultValue = "name")
          String sortBy,
      @Parameter(description = "Sort direction", example = "asc")
          @RequestParam(defaultValue = "asc")
          String direction) {

    logger.info(
        "Retrieving all products - Page: {}, Size: {}, Sort By: {}, Direction: {}",
        page,
        size,
        sortBy,
        direction);
    Sort.Direction sortDirection = Sort.Direction.fromString(direction.toLowerCase());
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    logger.info(
        "Successfully retrieved {} products",
        productService.getAllProducts(pageRequest).getTotalElements());
    Page<Product> products = productService.getAllProducts(pageRequest);
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its ID")
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved product",
        content = @Content(schema = @Schema(implementation = Product.class))
      ),
      @ApiResponse(responseCode = "404", description = "Product not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
    }
  )
  public ResponseEntity<Product> getProductById(
      @Parameter(description = "Product ID", example = "1") @PathVariable Long id) {
    try {
      Product product = productService.getProductById(id);
      logger.info("Successfully retrieved product: {}", product);
      return ResponseEntity.ok(product);
    } catch (ProductNotFoundException e) {
      logger.warn("Product not found with ID: {}", id);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @PutMapping("/{id}")
  @Operation(
    summary = "Update product",
    description = "Updates an existing product with optimistic locking"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Product updated successfully",
        content = @Content(schema = @Schema(implementation = Product.class))
      ),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "404", description = "Product not found"),
      @ApiResponse(responseCode = "409", description = "Concurrent modification conflict"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
    }
  )
  public ResponseEntity<Product> updateProduct(
      @Parameter(description = "Product ID", example = "1") @PathVariable Long id,
      @Valid @RequestBody ProductDTO productDTO,
      @Parameter(description = "Current version of the product", example = "1") @RequestParam
          Integer version) {
    try {
      Product updatedProduct = productService.updateProduct(id, productDTO, version);
      logger.info("Product updated successfully: {}", updatedProduct);
      return ResponseEntity.ok(updatedProduct);
    } catch (ProductNotFoundException e) {
      logger.warn("Product not found for update with ID: {}", id);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    } catch (jakarta.persistence.OptimisticLockException e) {
      logger.error("Concurrent modification conflict for product ID: {}", id);
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Product data has been updated by another user. Please refresh and try again.");
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete product", description = "Deletes a product by its ID")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Product not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
    }
  )
  public ResponseEntity<Void> deleteProduct(
      @Parameter(description = "Product ID", example = "1") @PathVariable Long id) {
    try {
      productService.deleteProduct(id);
      logger.info("Product deleted successfully with ID: {}", id);
      return ResponseEntity.noContent().build();
    } catch (ProductNotFoundException e) {
      logger.warn("Product not found for deletion with ID: {}", id);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }
}
