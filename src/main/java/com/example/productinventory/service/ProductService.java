package com.example.productinventory.service;

import com.example.productinventory.dto.ProductDTO;
import com.example.productinventory.exception.ProductNotFoundException;
import com.example.productinventory.exception.ProductOptimisticLockException;
import com.example.productinventory.model.Product;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Service interface for Product operations. Provides business logic for product management. */
public interface ProductService {

  /**
   * Creates a new product
   *
   * @param productDTO the product data to create
   * @return the created product
   */
  Product createProduct(ProductDTO productDTO);

  /**
   * Retrieves all products with pagination and sorting
   *
   * @param pageable pagination and sorting information
   * @return page of products
   */
  Page<Product> getAllProducts(Pageable pageable);

  /**
   * Retrieves a product by its ID
   *
   * @param id the product ID
   * @return the product
   * @throws ProductNotFoundException if product not found
   */
  Product getProductById(Long id) throws ProductNotFoundException;

  /**
   * Updates an existing product with optimistic locking
   *
   * @param id the product ID
   * @param productDTO the updated product data
   * @param version the current version for optimistic locking
   * @return the updated product
   * @throws ProductNotFoundException if product not found
   * @throws OptimisticLockException if version mismatch
   */
  Product updateProduct(Long id, ProductDTO productDTO, Integer version)
      throws ProductNotFoundException, ProductOptimisticLockException;

  /**
   * Deletes a product by its ID
   *
   * @param id the product ID
   * @throws ProductNotFoundException if product not found
   */
  void deleteProduct(Long id) throws ProductNotFoundException;

  /**
   * Searches products by name with pagination
   *
   * @param name the name to search for
   * @param pageable pagination information
   * @return page of matching products
   */
  Page<Product> searchProductsByName(String name, Pageable pageable);

  /**
   * Finds products within a price range
   *
   * @param minPrice minimum price
   * @param maxPrice maximum price
   * @param pageable pagination information
   * @return page of products in price range
   */
  Page<Product> findProductsByPriceRange(
      BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

  /**
   * Finds products within a quantity range
   *
   * @param minQuantity minimum quantity
   * @param maxQuantity maximum quantity
   * @param pageable pagination information
   * @return page of products in quantity range
   */
  Page<Product> findProductsByQuantityRange(
      Integer minQuantity, Integer maxQuantity, Pageable pageable);

  /**
   * Finds products with low stock
   *
   * @param threshold the quantity threshold
   * @return list of products with low stock
   */
  List<Product> findLowStockProducts(Integer threshold);

  /**
   * Advanced search with multiple criteria
   *
   * @param name product name (optional)
   * @param minPrice minimum price (optional)
   * @param maxPrice maximum price (optional)
   * @param minQuantity minimum quantity (optional)
   * @param maxQuantity maximum quantity (optional)
   * @param pageable pagination information
   * @return page of products matching criteria
   */
  Page<Product> searchProductsByCriteria(
      String name,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      Integer minQuantity,
      Integer maxQuantity,
      Pageable pageable);

  /**
   * Gets a product by SKU
   *
   * @param sku the product SKU
   * @return the product if found
   * @throws ProductNotFoundException if product not found
   */
  Product getProductBySku(String sku) throws ProductNotFoundException;

  /**
   * Checks if a product exists by its SKU
   *
   * @param sku the product SKU
   * @return true if the product exists, false otherwise
   */
  boolean existsBySku(String sku);
}
