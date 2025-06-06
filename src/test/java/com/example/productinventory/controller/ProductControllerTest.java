package com.example.productinventory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.example.productinventory.dto.ProductDTO;
import com.example.productinventory.exception.ProductBadRequestException;
import com.example.productinventory.exception.ProductConflictException;
import com.example.productinventory.exception.ProductNotFoundException;
import com.example.productinventory.exception.ProductOptimisticLockException;
import com.example.productinventory.model.Product;
import com.example.productinventory.service.ProductService;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebMvcTest(ProductController.class)
@AutoConfigureWebTestClient
public class ProductControllerTest {

  @Autowired private WebTestClient webTestClient;

  @MockBean private ProductService productService;

  private ProductDTO productDTO;
  private Product product;

  /**
   * Set up the test environment before each test. Initializes a valid ProductDTO and Product object
   * for testing.
   */
  @BeforeEach
  void setUp() {
    productDTO = new ProductDTO();
    productDTO.setName("Test Product");
    productDTO.setDescription("Test Description");
    productDTO.setPrice(BigDecimal.TEN);
    productDTO.setQuantity(10);
    productDTO.setSku("TEST-SKU");
    productDTO.setWeight(new BigDecimal("1.5"));
    productDTO.setDimensions("30x20x5");

    product = new Product();
    product.setId(1L);
    product.setName("Test Product");
    product.setDescription("Test Description");
    product.setPrice(BigDecimal.TEN);
    product.setQuantity(10);
    product.setSku("TEST-SKU");
    product.setWeight(new BigDecimal("1.5"));
    product.setDimensions("30x20x5");
  }

  /**
   * Test the creation of a product with valid input. Expects a 201 Created response with the
   * product details.
   */
  @Test
  void createProduct_validInput_returnsCreated() {
    when(productService.createProduct(any(ProductDTO.class))).thenReturn(product);

    webTestClient
        .post()
        .uri("/api/v1/products")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(productDTO)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo(product.getId())
        .jsonPath("$.name")
        .isEqualTo(product.getName());
  }

  /**
   * Test the creation of a product with invalid input. Expects a 400 Bad Request response with an
   * error message.
   */
  @Test
  void createProduct_invalidInput_returnsBadRequest() {
    // Simulate invalid input by throwing a ProductBadRequestException
    when(productService.createProduct(any(ProductDTO.class)))
        .thenThrow(new ProductBadRequestException("Validation failed for the request"));

    webTestClient
        .post()
        .uri("/api/v1/products")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(productDTO)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("Validation failed for the request");
  }

  /**
   * Test the creation of a product with a duplicate SKU. Expects a 409 Conflict response with an
   * error message.
   */
  @Test
  void createProduct_duplicateSku_returnsConflict() {
    when(productService.createProduct(any(ProductDTO.class)))
        .thenThrow(new ProductConflictException("A product with SKU TEST-SKU already exists."));

    webTestClient
        .post()
        .uri("/api/v1/products")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(productDTO)
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("A product with SKU TEST-SKU already exists.");
  }

  /**
   * Test retrieving a product by its existing ID. Expects a 200 OK response with the product
   * details.
   */
  @Test
  void getProductById_existingId_returnsOk() {
    when(productService.getProductById(1L)).thenReturn(product);

    webTestClient
        .get()
        .uri("/api/v1/products/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo(product.getId())
        .jsonPath("$.name")
        .isEqualTo(product.getName());
  }

  /**
   * Test retrieving a product by a non-existing ID. Expects a 404 Not Found response with an error
   * message.
   */
  @Test
  void getProductById_nonExistingId_returnsNotFound() {
    when(productService.getProductById(1L))
        .thenThrow(new ProductNotFoundException("Product not found with ID: 1"));

    webTestClient
        .get()
        .uri("/api/v1/products/1")
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("Product not found with ID: 1");
  }

  /**
   * Test updating a product with an existing ID and valid version. Expects a 200 OK response with
   * the updated product details.
   */
  @Test
  void updateProduct_existingIdAndValidVersion_returnsOk() {
    when(productService.updateProduct(eq(1L), any(ProductDTO.class), eq(1))).thenReturn(product);

    webTestClient
        .put()
        .uri("/api/v1/products/1?version=1")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(productDTO)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo(product.getId())
        .jsonPath("$.name")
        .isEqualTo(product.getName());
  }

  /**
   * Test updating a product with a non-existing ID. Expects a 404 Not Found response with an error
   * message.
   */
  @Test
  void updateProduct_nonExistingId_returnsNotFound() {
    when(productService.updateProduct(eq(1L), any(ProductDTO.class), eq(1)))
        .thenThrow(new ProductNotFoundException("Product not found with ID: 1"));

    webTestClient
        .put()
        .uri("/api/v1/products/1?version=1")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(productDTO)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("Product not found with ID: 1");
  }

  /**
   * Test updating a product with a version mismatch. Expects a 409 Conflict response with an error
   * message.
   */
  @Test
  void updateProduct_versionMismatch_returnsConflict() {
    when(productService.updateProduct(eq(1L), any(ProductDTO.class), eq(1)))
        .thenThrow(
            new ProductOptimisticLockException("Product data has been updated by another user."));

    webTestClient
        .put()
        .uri("/api/v1/products/1?version=1")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(productDTO)
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("Product data has been updated by another user.");
  }

  /** Test deleting a product with an existing ID. Expects a 204 No Content response. */
  @Test
  void deleteProduct_existingId_returnsNoContent() {
    doNothing().when(productService).deleteProduct(1L);

    webTestClient
        .delete()
        .uri("/api/v1/products/1")
        .exchange()
        .expectStatus()
        .isNoContent(); // 204 No Content
  }

  /**
   * Test deleting a product with a non-existing ID. Expects a 404 Not Found response with an error
   * message.
   */
  @Test
  void deleteProduct_nonExistingId_returnsNotFound() {
    doThrow(new ProductNotFoundException("Product not found with ID: 1"))
        .when(productService)
        .deleteProduct(1L);

    webTestClient
        .delete()
        .uri("/api/v1/products/1")
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("Product not found with ID: 1");
  }
}
