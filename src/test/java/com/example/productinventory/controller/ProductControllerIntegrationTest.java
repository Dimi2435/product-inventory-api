package com.example.productinventory.controller;

import com.example.productinventory.dto.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  private static final String BASE_URL = "/api/v1/products";

  @BeforeEach
  void setUp() throws Exception {
    // Clean up the database or reset state if necessary
    // This can be done by using a DELETE request or by ensuring the database is reset before each
    // test
  }

  @Test
  void createProduct_validInput_returnsCreated() throws Exception {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setName("Test Product");
    productDTO.setDescription("Test Description");
    productDTO.setPrice(BigDecimal.TEN);
    productDTO.setQuantity(10);
    productDTO.setSku("SKU-001-" + System.currentTimeMillis()); // Ensure unique SKU
    productDTO.setWeight(BigDecimal.valueOf(1.5));
    productDTO.setDimensions("30x20x5");

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"));
  }

  @Test
  void getProductById_existingId_returnsOk() throws Exception {
    // First create a product
    ProductDTO productDTO = new ProductDTO();
    productDTO.setName("Test Product");
    productDTO.setDescription("Test Description");
    productDTO.setPrice(BigDecimal.TEN);
    productDTO.setQuantity(10);
    productDTO.setSku("SKU-002-" + System.currentTimeMillis()); // Ensure unique SKU
    productDTO.setWeight(BigDecimal.valueOf(1.5));
    productDTO.setDimensions("30x20x5");

    String response =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productDTO)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Extract the ID from the response
    Long productId = objectMapper.readTree(response).get("id").asLong();

    // Then try to get the product
    mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URL + "/" + productId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productId))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"));
  }

  // @Test
  // void getProductById_nonExistingId_returnsNotFound() throws Exception {
  //     mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/999"))
  //             .andExpect(MockMvcResultMatchers.status().isNotFound())
  //             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product with ID 999
  // not found"));
  // }

  // Add more tests for other endpoints and scenarios (positive and negative)
}
