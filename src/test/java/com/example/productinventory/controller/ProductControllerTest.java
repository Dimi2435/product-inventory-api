package com.example.productinventory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.productinventory.dto.ProductDTO;
import com.example.productinventory.exception.ProductBadRequestException;
import com.example.productinventory.exception.ProductNotFoundException;
import com.example.productinventory.model.Product;
import com.example.productinventory.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebMvcTest(ProductController.class)
@AutoConfigureWebTestClient
public class ProductControllerTest {

  @Autowired private WebTestClient webTestClient;

  @MockBean private ProductService productService;

  private ProductDTO productDTO;
  private Product product;

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

  @Test
  void createProduct_validInput_returnsCreated() {
    when(productService.existsBySku(productDTO.getSku())).thenReturn(false);
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

  @Test
  void createProduct_duplicateSku_returnsConflict() {
    when(productService.existsBySku(eq(productDTO.getSku()))).thenReturn(true);

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

  @Test
  void createProduct_badRequest_returnsBadRequest() {
    when(productService.existsBySku(productDTO.getSku())).thenReturn(false);
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
        .isEqualTo(
            "Validation failed for the request"); // Ensure this matches the response structure
  }

  @Test
  void createProduct_internalServerError_returnsInternalServerError() {
    when(productService.existsBySku(productDTO.getSku())).thenReturn(false);
    when(productService.createProduct(any(ProductDTO.class)))
        .thenThrow(new RuntimeException("Unexpected error"));

    webTestClient
        .post()
        .uri("/api/v1/products")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(productDTO)
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("An unexpected error occurred while creating the product.");
  }

  @Test
  void getAllProducts_returnsOk() {
    // Mock the service to return a page of products
    Page<Product> productPage = new PageImpl<>(List.of(product), PageRequest.of(0, 10), 1);
    when(productService.getAllProducts(any(Pageable.class))).thenReturn(productPage);

    webTestClient
        .get()
        .uri("/api/v1/products?page=0&size=10")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.content[0].id")
        .isEqualTo(product.getId())
        .jsonPath("$.content[0].name")
        .isEqualTo(product.getName());
  }

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

  @Test
  void getProductById_nonExistingId_returnsNotFound() {
    when(productService.getProductById(1L))
        .thenThrow(new ProductNotFoundException("Product not found"));

    webTestClient
        .get()
        .uri("/api/v1/products/1")
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("Product not found");
  }
}
