package com.example.productinventory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.productinventory.dto.ProductDTO;
import com.example.productinventory.exception.ProductBadRequestException;
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
}
