package com.example.productinventory.service;

import com.example.productinventory.dto.ProductDTO;
import com.example.productinventory.exception.ProductNotFoundException;
import com.example.productinventory.exception.ProductOptimisticLockException;
import com.example.productinventory.model.Product;
import com.example.productinventory.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of ProductService interface. Provides business logic for product management
 * operations.
 */
@Service
public class ProductServiceImpl implements ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
  private final ProductRepository productRepository;

  @Autowired
  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  @Transactional
  public Product createProduct(ProductDTO productDTO) {
    logger.info("Creating new product: {}", productDTO.getName());
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

  @Override
  public Page<Product> getAllProducts(Pageable pageable) {
    logger.info("Retrieving all products with pagination: {}", pageable);
    return productRepository.findAll(pageable);
  }

  @Override
  public Product getProductById(Long id) {
    logger.info("Retrieving product by ID: {}", id);
    return productRepository
        .findById(id)
        .orElseThrow(
            () -> {
              logger.warn("Product not found with ID: {}", id);
              return new ProductNotFoundException("Product not found with id: " + id);
            });
  }

  @Override
  @Transactional
  public Product updateProduct(Long id, ProductDTO productDTO, Integer version) {
    logger.info("Updating product with ID: {}", id);
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

  @Override
  public Page<Product> searchProductsByName(String name, Pageable pageable) {
    logger.info("Searching products by name: {}", name);
    return productRepository.findByNameContainingIgnoreCase(name, pageable);
  }

  @Override
  public Page<Product> findProductsByPriceRange(
      BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
    logger.info("Searching products by price range: {} - {}", minPrice, maxPrice);
    return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
  }

  @Override
  public Page<Product> findProductsByQuantityRange(
      Integer minQuantity, Integer maxQuantity, Pageable pageable) {
    logger.info("Searching products by quantity range: {} - {}", minQuantity, maxQuantity);
    return productRepository.findByQuantityBetween(minQuantity, maxQuantity, pageable);
  }

  @Override
  public List<Product> findLowStockProducts(Integer threshold) {
    logger.info("Finding low stock products with threshold: {}", threshold);
    return productRepository.findLowStockProducts(threshold);
  }

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
}
