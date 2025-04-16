// package com.example.productinventory.repository;

// import static org.assertj.core.api.Assertions.assertThat;

// import com.example.productinventory.model.Product;
// import java.math.BigDecimal;
// import java.util.Optional;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.test.context.ActiveProfiles;

// @DataJpaTest
// @ActiveProfiles("test") // Use a test profile if you have specific configurations
// public class ProductRepositoryTest {

//   @Autowired private ProductRepository productRepository;

//   @BeforeEach
//   public void setUp() {
//     // Clear the repository before each test
//     productRepository.deleteAll();
//   }

//   private Product createAndSaveProduct(String name, String sku, BigDecimal price, int quantity) {
//     Product product = new Product();
//     product.setName(name);
//     product.setDescription("A test product.");
//     product.setPrice(price);
//     product.setQuantity(quantity);
//     product.setSku(sku);
//     product.setWeight(BigDecimal.valueOf(1.0));
//     product.setDimensions("30x20x5");
//     return productRepository.save(product);
//   }

//   @Test
//   public void testSaveAndFindProduct() {
//     Product savedProduct =
//         createAndSaveProduct("Test Product", "TEST-SKU", BigDecimal.valueOf(100.00), 10);

//     // Find the product by ID
//     Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

//     assertThat(foundProduct).isPresent();
//     assertThat(foundProduct.get().getName()).isEqualTo("Test Product");
//   }

//   @Test
//   public void testFindBySku() {
//     createAndSaveProduct("Test Product", "TEST-SKU", BigDecimal.valueOf(100.00), 10);

//     // Find the product by SKU
//     Optional<Product> foundProduct = productRepository.findBySku("TEST-SKU");

//     assertThat(foundProduct).isPresent();
//     assertThat(foundProduct.get().getName()).isEqualTo("Test Product");
//   }

//   @Test
//   public void testFindByNonExistingSku() {
//     // Attempt to find a product by a non-existing SKU
//     Optional<Product> foundProduct = productRepository.findBySku("NON-EXISTING-SKU");

//     assertThat(foundProduct).isNotPresent();
//   }

//   @Test
//   public void testExistsBySku() {
//     createAndSaveProduct("Test Product", "TEST-SKU", BigDecimal.valueOf(100.00), 10);

//     // Check if the SKU exists
//     boolean exists = productRepository.existsBySku("TEST-SKU");

//     assertThat(exists).isTrue();
//   }

//   @Test
//   public void testExistsByNonExistingSku() {
//     // Check if a non-existing SKU exists
//     boolean exists = productRepository.existsBySku("NON-EXISTING-SKU");

//     assertThat(exists).isFalse();
//   }

//   @Test
//   public void testFindByPriceBetween() {
//     Product product1 = createAndSaveProduct("Product 1", "SKU-1", BigDecimal.valueOf(50.00), 5);
//     Product product2 = createAndSaveProduct("Product 2", "SKU-2", BigDecimal.valueOf(150.00),
// 10);

//     // Find products within a price range
//     Page<Product> products =
//         productRepository.findByPriceBetween(
//             BigDecimal.valueOf(40.00), BigDecimal.valueOf(100.00), Pageable.unpaged());

//     assertThat(products.getContent()).containsExactly(product1);
//   }

//   @Test
//   public void testFindByPriceBetween_NoProductsInRange() {
//     createAndSaveProduct("Product 1", "SKU-1", BigDecimal.valueOf(150.00), 5);
//     createAndSaveProduct("Product 2", "SKU-2", BigDecimal.valueOf(200.00), 10);

//     // Find products within a price range that has no products
//     Page<Product> products =
//         productRepository.findByPriceBetween(
//             BigDecimal.valueOf(40.00), BigDecimal.valueOf(100.00), Pageable.unpaged());

//     assertThat(products.getContent()).isEmpty();
//   }
// }
