package com.example.productinventory.service;

import com.example.productinventory.dto.ProductDTO;
import com.example.productinventory.exception.ProductBadRequestException;
import com.example.productinventory.exception.ProductConflictException;
import com.example.productinventory.exception.ProductNotFoundException;
import com.example.productinventory.exception.ProductOptimisticLockException;
import com.example.productinventory.exception.ProductUnprocessableEntityException;
import com.example.productinventory.model.Product;
import com.example.productinventory.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the ProductService interface. Provides business logic for product management
 * operations, including creating, retrieving, updating, and deleting products.
 */
@Service
public class ProductServiceImpl implements ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
  private final ProductRepository productRepository;

  @Value("${product.sort.fields}")
  private String[] validSortFields;

  @Value("${product.sort.directions}")
  private String[] validDirections;

  @Autowired
  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  /**
   * Creates a new product.
   *
   * @param productDTO the product data to create
   * @return the created product
   * @throws ProductConflictException if a product with the same SKU already exists
   */
  @Override
  @Transactional
  public Product createProduct(ProductDTO productDTO) {
    logger.info("Creating new product: {}", productDTO.getName());

    // Check for duplicate SKU
    if (existsBySku(productDTO.getSku())) {
      throw new ProductConflictException(
          "A product with SKU " + productDTO.getSku() + " already exists.");
    }

    Product product = new Product();
    product.setName(productDTO.getName());
    product.setDescription(productDTO.getDescription());
    product.setPrice(productDTO.getPrice());
    product.setQuantity(productDTO.getQuantity());
    product.setSku(productDTO.getSku());
    product.setWeight(productDTO.getWeight());
    product.setDimensions(productDTO.getDimensions());

    Product savedProduct = productRepository.save(product);
    logger.info("Product created successfully with ID: {}", savedProduct.getId());
    return savedProduct;
  }

  /**
   * Retrieves all products with pagination and sorting.
   *
   * @param pageable pagination and sorting information
   * @param sortProperty the property to sort by
   * @return a page of products
   */
  @Override
  public Page<Product> getAllProducts(Pageable pageable, String sortProperty) {
    validatePage(pageable.getPageNumber());
    validateSize(pageable.getPageSize());

    // Extract sort information
    String sortBy =
        pageable.getSort().isSorted()
            ? pageable.getSort().getOrderFor(sortProperty).getProperty()
            : "name"; // Default sort by name
    String direction =
        pageable.getSort().isSorted()
            ? pageable.getSort().getOrderFor(sortProperty).getDirection().name()
            : "asc"; // Default direction

    // Validate sort
    validateSort(sortBy, direction);

    logger.info("Retrieving all products with pagination: {}", pageable);
    return productRepository.findAll(pageable);
  }

  /**
   * Retrieves a product by its ID.
   *
   * @param id the product ID
   * @return the product
   * @throws ProductNotFoundException if the product is not found
   */
  @Override
  public Product getProductById(Long id) {
    logger.info("Retrieving product by ID: {}", id);

    validateProductId(id);

    return productRepository
        .findById(id)
        .orElseThrow(
            () -> {
              logger.warn("Product not found with ID: {}", id);
              return new ProductNotFoundException("Product not found with id: " + id);
            });
  }

  /**
   * Updates an existing product with optimistic locking.
   *
   * @param id the product ID
   * @param productDTO the updated product data
   * @param version the current version for optimistic locking
   * @return the updated product
   * @throws ProductNotFoundException if the product is not found
   * @throws ProductOptimisticLockException if there is a version mismatch
   */
  @Override
  @Transactional
  public Product updateProduct(Long id, ProductDTO productDTO, Integer version) {
    logger.info("Updating product with ID: {}", id);

    validateProductId(id); // Validate the ID before proceeding
    validateProductDTO(productDTO); // Validate input fields

    Product existingProduct =
        productRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  logger.warn("Product not found with ID: {}", id);
                  return new ProductNotFoundException("Product not found with id: " + id);
                });

    if (!existingProduct.getVersion().equals(version)) {
      logger.warn("Optimistic lock failure for product ID: {}", id);
      throw new ProductOptimisticLockException("Product data has been updated by another user.");
    }

    existingProduct.setName(productDTO.getName());
    existingProduct.setDescription(productDTO.getDescription());
    existingProduct.setPrice(productDTO.getPrice());
    existingProduct.setQuantity(productDTO.getQuantity());
    existingProduct.setSku(productDTO.getSku());
    existingProduct.setWeight(productDTO.getWeight());
    existingProduct.setDimensions(productDTO.getDimensions());

    Product updatedProduct = productRepository.save(existingProduct);
    logger.info("Product updated successfully with ID: {}", id);
    return updatedProduct;
  }

  /**
   * Deletes a product by its ID.
   *
   * @param id the product ID
   * @throws ProductNotFoundException if the product is not found
   */
  @Override
  @Transactional
  public void deleteProduct(Long id) {
    logger.info("Deleting product with ID: {}", id);
    if (!productRepository.existsById(id)) {
      logger.warn("Product not found with ID: {}", id);
      throw new ProductNotFoundException("Product not found with id: " + id);
    }
    productRepository.deleteById(id);
    logger.info("Product deleted successfully with ID: {}", id);
  }

  /**
   * Searches products by name with pagination.
   *
   * @param name the name to search for
   * @param pageable pagination information
   * @return a page of matching products
   */
  @Override
  public Page<Product> searchProductsByName(String name, Pageable pageable) {
    logger.info("Searching products by name: {}", name);
    return productRepository.findByNameContainingIgnoreCase(name, pageable);
  }

  /**
   * Finds products within a price range.
   *
   * @param minPrice minimum price
   * @param maxPrice maximum price
   * @param pageable pagination information
   * @return a page of products in the specified price range
   */
  @Override
  public Page<Product> findProductsByPriceRange(
      BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
    logger.info("Searching products by price range: {} - {}", minPrice, maxPrice);
    return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
  }

  /**
   * Finds products within a quantity range.
   *
   * @param minQuantity minimum quantity
   * @param maxQuantity maximum quantity
   * @param pageable pagination information
   * @return a page of products in the specified quantity range
   */
  @Override
  public Page<Product> findProductsByQuantityRange(
      Integer minQuantity, Integer maxQuantity, Pageable pageable) {
    logger.info("Searching products by quantity range: {} - {}", minQuantity, maxQuantity);
    return productRepository.findByQuantityBetween(minQuantity, maxQuantity, pageable);
  }

  /**
   * Finds products with low stock.
   *
   * @param threshold the quantity threshold
   * @return a list of products with low stock
   */
  @Override
  public List<Product> findLowStockProducts(Integer threshold) {
    logger.info("Finding low stock products with threshold: {}", threshold);
    return productRepository.findLowStockProducts(threshold);
  }

  /**
   * Advanced search with multiple criteria.
   *
   * @param name product name (optional)
   * @param minPrice minimum price (optional)
   * @param maxPrice maximum price (optional)
   * @param minQuantity minimum quantity (optional)
   * @param maxQuantity maximum quantity (optional)
   * @param pageable pagination information
   * @return a page of products matching the specified criteria
   */
  @Override
  public Page<Product> searchProductsByCriteria(
      String name,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      Integer minQuantity,
      Integer maxQuantity,
      Pageable pageable) {
    logger.info(
        "Searching products by criteria - name: {}, price: {}-{}, quantity: {}-{}",
        name,
        minPrice,
        maxPrice,
        minQuantity,
        maxQuantity);
    return productRepository.findByCriteria(
        name, minPrice, maxPrice, minQuantity, maxQuantity, pageable);
  }

  /**
   * Gets a product by SKU.
   *
   * @param sku the product SKU
   * @return the product if found
   * @throws ProductNotFoundException if the product is not found
   */
  @Override
  public Product getProductBySku(String sku) {
    logger.info("Retrieving product by SKU: {}", sku);
    return productRepository
        .findBySku(sku)
        .orElseThrow(
            () -> {
              logger.warn("Product not found with SKU: {}", sku);
              return new ProductNotFoundException("Product not found with SKU: " + sku);
            });
  }

  /**
   * Checks if a product exists by its SKU.
   *
   * @param sku the product SKU
   * @return true if the product exists, false otherwise
   */
  @Override
  public boolean existsBySku(String sku) {
    logger.info("Checking if product exists with SKU: {}", sku);
    boolean exists = productRepository.existsBySku(sku);
    logger.info("Product exists with SKU {}: {}", sku, exists);
    return exists;
  }

  /**
   * Validates the provided ProductDTO.
   *
   * @param productDTO the product data to validate
   * @throws ProductUnprocessableEntityException if validation fails
   */
  private void validateProductDTO(ProductDTO productDTO) {
    if (productDTO.getName() == null || productDTO.getName().isEmpty()) {
      throw new ProductUnprocessableEntityException("Product name is required.");
    }
    if (productDTO.getPrice() == null || productDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
      throw new ProductUnprocessableEntityException("Product price must be a positive value.");
    }
    if (productDTO.getQuantity() < 0) {
      throw new ProductUnprocessableEntityException("Product quantity cannot be negative.");
    }
    // Add more validations as necessary
  }

  /**
   * Validates the page number.
   *
   * @param page the page number to validate
   * @throws ProductBadRequestException if the page number is invalid
   */
  private void validatePage(int page) {
    if (page < 0) {
      throw new ProductBadRequestException("Page number must be zero or greater.");
    }
  }

  /**
   * Validates the page size.
   *
   * @param size the page size to validate
   * @throws ProductBadRequestException if the size is invalid
   */
  private void validateSize(int size) {
    if (size <= 0) {
      throw new ProductBadRequestException("Size must be a positive integer.");
    }
  }

  /**
   * Validates the sort parameters.
   *
   * @param sortBy the field to sort by
   * @param direction the sort direction
   * @throws ProductUnprocessableEntityException if the sort parameters are invalid
   */
  private void validateSort(String sortBy, String direction) {
    logger.info("validateSort sortBy  and direction: {}  {}", sortBy, direction);

    // Validate sortBy field
    if (!Arrays.asList(validSortFields).contains(sortBy.toLowerCase())) {
      throw new ProductUnprocessableEntityException(
          "Sort field must be one of: " + Arrays.toString(validSortFields));
    }

    // Validate sort direction
    if (!Arrays.asList(validDirections).contains(direction.toLowerCase())) {
      throw new ProductUnprocessableEntityException("Sort direction must be 'asc' or 'desc'.");
    }
  }

  /**
   * Validates the product ID.
   *
   * @param id the product ID to validate
   * @throws ProductUnprocessableEntityException if the ID is invalid
   */
  private void validateProductId(Long id) {
    if (id <= 0) {
      throw new ProductUnprocessableEntityException("Product ID must be a positive integer.");
    }
  }
}
