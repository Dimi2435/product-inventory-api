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

//   @Test
//   public void testSaveAndFindProduct() {
//     Product product = new Product();
//     product.setName("Test Product");
//     product.setDescription("A test product.");
//     product.setPrice(BigDecimal.valueOf(100.00));
//     product.setQuantity(10);
//     product.setSku("TEST-SKU");
//     product.setWeight(BigDecimal.valueOf(1.0));
//     product.setDimensions("30x20x5");

//     // Save the product
//     Product savedProduct = productRepository.save(product);

//     // Find the product by ID
//     Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

//     assertThat(foundProduct).isPresent();
//     assertThat(foundProduct.get().getName()).isEqualTo("Test Product");
//   }

//   @Test
//   public void testFindBySku() {
//     Product product = new Product();
//     product.setName("Test Product");
//     product.setDescription("A test product.");
//     product.setPrice(BigDecimal.valueOf(100.00));
//     product.setQuantity(10);
//     product.setSku("TEST-SKU");
//     product.setWeight(BigDecimal.valueOf(1.0));
//     product.setDimensions("30x20x5");

//     // Save the product
//     productRepository.save(product);

//     // Find the product by SKU
//     Optional<Product> foundProduct = productRepository.findBySku("TEST-SKU");

//     assertThat(foundProduct).isPresent();
//     assertThat(foundProduct.get().getName()).isEqualTo("Test Product");
//   }

//   @Test
//   public void testExistsBySku() {
//     Product product = new Product();
//     product.setName("Test Product");
//     product.setDescription("A test product.");
//     product.setPrice(BigDecimal.valueOf(100.00));
//     product.setQuantity(10);
//     product.setSku("TEST-SKU");
//     product.setWeight(BigDecimal.valueOf(1.0));
//     product.setDimensions("30x20x5");

//     // Save the product
//     productRepository.save(product);

//     // Check if the SKU exists
//     boolean exists = productRepository.existsBySku("TEST-SKU");

//     assertThat(exists).isTrue();
//   }

//   @Test
//   public void testFindByPriceBetween() {
//     Product product1 = new Product();
//     product1.setName("Product 1");
//     product1.setPrice(BigDecimal.valueOf(50.00));
//     product1.setQuantity(5);
//     product1.setSku("SKU-1");
//     productRepository.save(product1);

//     Product product2 = new Product();
//     product2.setName("Product 2");
//     product2.setPrice(BigDecimal.valueOf(150.00));
//     product2.setQuantity(10);
//     product2.setSku("SKU-2");
//     productRepository.save(product2);

//     // Find products within a price range
//     Page<Product> products =
//         productRepository.findByPriceBetween(
//             BigDecimal.valueOf(40.00), BigDecimal.valueOf(100.00), Pageable.unpaged());

//     assertThat(products.getContent()).containsExactly(product1);
//   }
// }
