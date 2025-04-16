package com.example.productinventory.controller;

import com.example.productinventory.dto.PaginatedResponse;
import com.example.productinventory.dto.ProductDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ProductController handles API requests related to product management. It provides endpoints for
 * creating, retrieving, updating, and deleting products.
 */
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

  /**
   * Creates a new product with the provided details.
   *
   * @param productDTO the product details to create
   * @return ResponseEntity containing the created product and HTTP status 201 (Created)
   */
  @PostMapping
  @Operation(
    summary = "Create a new product",
    description = "Creates a new product with the provided details"
  )
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "201", description = "Product created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "409", description = "Conflict with current state"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
    }
  )
  public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
    logger.info("Creating product: {}", productDTO);
    Product createdProduct = productService.createProduct(productDTO);
    logger.info("Product created successfully: {}", createdProduct);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  /**
   * Retrieves a paginated list of all products.
   *
   * @param page the page number (0-based)
   * @param size the number of items per page
   * @param sortBy the field to sort by
   * @param direction the direction of sorting (asc or desc)
   * @return ResponseEntity containing a paginated response of products
   */
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
  public ResponseEntity<PaginatedResponse<Product>> getAllProducts(
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
        "Get all products - Page: {}, Size: {}, Sort By: {}, Direction: {}",
        page,
        size,
        sortBy,
        direction);

    Sort.Direction sortDirection = Sort.Direction.fromString(direction.toLowerCase());
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

    Page<Product> productsPage = productService.getAllProducts(pageRequest, sortBy);
    PaginatedResponse<Product> response =
        new PaginatedResponse<>(
            productsPage.getContent(),
            productsPage.getNumber(),
            productsPage.getTotalPages(),
            productsPage.getTotalElements(),
            productsPage.getSize());
    logger.info("Successfully retrieved {} products", productsPage.getTotalElements());
    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves a specific product by its ID.
   *
   * @param id the ID of the product to retrieve
   * @return ResponseEntity containing the product and HTTP status 200 (OK)
   */
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
  public ResponseEntity<Product> getProductById(@PathVariable Long id) {
    logger.info("Retrieving product by ID: {}", id);
    Product product = productService.getProductById(id);
    return ResponseEntity.ok(product);
  }

  /**
   * Updates an existing product with the provided details.
   *
   * @param id the ID of the product to update
   * @param productDTO the updated product details
   * @param version the current version of the product for optimistic locking
   * @return ResponseEntity containing the updated product and HTTP status 200 (OK)
   */
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
      @ApiResponse(
        responseCode = "422",
        description = "Unprocessable entity due to validation errors"
      ),
      @ApiResponse(responseCode = "500", description = "Internal server error")
    }
  )
  public ResponseEntity<Product> updateProduct(
      @Parameter(description = "Product ID", example = "1") @PathVariable Long id,
      @Valid @RequestBody ProductDTO productDTO,
      @Parameter(description = "Current version of the product", example = "1") @RequestParam
          Integer version) {
    logger.info("Updating product with ID: {}", id);
    Product updatedProduct = productService.updateProduct(id, productDTO, version);
    logger.info("Product updated successfully: {}", updatedProduct);
    return ResponseEntity.ok(updatedProduct);
  }

  /**
   * Deletes a product by its ID.
   *
   * @param id the ID of the product to delete
   * @return ResponseEntity with HTTP status 204 (No Content) if successful
   */
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
    logger.info("Deleting product with ID: {}", id);
    productService.deleteProduct(id);
    logger.info("Product deleted successfully with ID: {}", id);
    return ResponseEntity.noContent().build(); // Returns 204 No Content
  }
}
