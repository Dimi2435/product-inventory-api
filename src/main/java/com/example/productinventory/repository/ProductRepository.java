package com.example.productinventory.repository;

import com.example.productinventory.model.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Product entity. Provides methods for data access and querying products.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  /** Find all products with pagination and sorting */
  Page<Product> findAll(Pageable pageable);

  /** Find products by name (case-insensitive) with pagination */
  Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

  /** Find products by price range with pagination */
  Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

  /** Find products by quantity range with pagination */
  Page<Product> findByQuantityBetween(Integer minQuantity, Integer maxQuantity, Pageable pageable);

  /** Find products by SKU (exact match) */
  @Query("SELECT p FROM Product p WHERE p.sku = :sku")
  Optional<Product> findBySku(String sku);

  /** Check if a product exists by SKU (exact match) */
  @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.sku = :sku")
  boolean existsBySku(String sku);

  /** Find products with low stock (quantity less than threshold) */
  @Query("SELECT p FROM Product p WHERE p.quantity < :threshold")
  List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

  /** Find products by multiple criteria with pagination */
  @Query(
      "SELECT p FROM Product p WHERE "
          + "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "
          + "(:minPrice IS NULL OR p.price >= :minPrice) AND "
          + "(:maxPrice IS NULL OR p.price <= :maxPrice) AND "
          + "(:minQuantity IS NULL OR p.quantity >= :minQuantity) AND "
          + "(:maxQuantity IS NULL OR p.quantity <= :maxQuantity)")
  Page<Product> findByCriteria(
      @Param("name") String name,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("minQuantity") Integer minQuantity,
      @Param("maxQuantity") Integer maxQuantity,
      Pageable pageable);

  // Future category relationship queries
  // @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
  // Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}
