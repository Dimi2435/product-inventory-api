package com.example.productinventory.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(ProductController.class)
// public class ProductControllerTest {

//   @Autowired private MockMvc mockMvc;

//   @MockBean private ProductService productService;

//   @Autowired private ObjectMapper objectMapper;

//   private ProductDTO productDTO;
//   private Product product;

//   @BeforeEach
//   void setUp() {
//     productDTO = new ProductDTO();
//     productDTO.setName("Test Product");
//     productDTO.setDescription("Test Description");
//     productDTO.setPrice(BigDecimal.TEN);
//     productDTO.setSku(null);
//     productDTO.setWeight(BigDecimal.valueOf(1.5));
//     productDTO.setDimensions("30x20x5");

//     product = new Product();
//     product.setId(1L);
//     product.setName("Test Product");
//     product.setDescription("Test Description");
//     product.setPrice(BigDecimal.TEN);
//     product.setSku("TEST-SKU");
//     product.setWeight(BigDecimal.valueOf(1.5));
//     product.setDimensions("30x20x5");
//   }

//   @Test
//   void createProduct_validInput_returnsCreated() throws Exception {
//     Mockito.when(productService.createProduct(any(ProductDTO.class))).thenReturn(product);

//     mockMvc
//         .perform(
//             MockMvcRequestBuilders.post("/api/v1/products")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(productDTO)))
//         .andExpect(status().isCreated())
//         .andExpect(jsonPath("$.id").value(product.getId()))
//         .andExpect(jsonPath("$.name").value(product.getName()));
//   }

//   @Test
//   void createProduct_duplicateSku_returnsConflict() throws Exception {
//     Mockito.when(productService.createProduct(any(ProductDTO.class)))
//         .thenThrow(new ProductConflictException("A product with SKU TEST-SKU already exists."));

//     mockMvc
//         .perform(
//             MockMvcRequestBuilders.post("/api/v1/products")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(productDTO)))
//         .andExpect(status().isConflict())
//         .andExpect(jsonPath("$.message").value("A product with SKU TEST-SKU already exists."));
//   }

  // @Test
  // void getAllProducts_returnsOk() throws Exception {
  //   List<Product> products = Collections.singletonList(product);
  //   Page<Product> productPage = new PageImpl<>(products);
  //   Mockito.when(productService.getAllProducts(any())).thenReturn(productPage);

  //   mockMvc
  //       .perform(MockMvcRequestBuilders.get("/api/v1/products"))
  //       .andExpect(status().isOk())
  //       .andExpect(jsonPath("$.content[0].id").value(product.getId()))
  //       .andExpect(jsonPath("$.content[0].name").value(product.getName()));
  // }

  // @Test
  // void getProductById_existingId_returnsOk() throws Exception {
  //   Mockito.when(productService.getProductById(1L)).thenReturn(product);

  //   mockMvc
  //       .perform(MockMvcRequestBuilders.get("/api/v1/products/1"))
  //       .andExpect(status().isOk())
  //       .andExpect(jsonPath("$.id").value(product.getId()))
  //       .andExpect(jsonPath("$.name").value(product.getName()));
  // }

  // @Test
  // void getProductById_nonExistingId_returnsNotFound() throws Exception {
  //   Mockito.when(productService.getProductById(1L))
  //       .thenThrow(new ProductNotFoundException("Product not found"));

  //   mockMvc
  //       .perform(MockMvcRequestBuilders.get("/api/v1/products/1"))
  //       .andExpect(status().isNotFound())
  //       .andExpect(jsonPath("$.message").value("Product not found"));
  // }

  // @Test
  // void updateProduct_existingId_returnsOk() throws Exception {
  //   Mockito.when(productService.updateProduct(eq(1L), any(ProductDTO.class), eq(0)))
  //       .thenReturn(product);

  //   mockMvc
  //       .perform(
  //           MockMvcRequestBuilders.put("/api/v1/products/1?version=0")
  //               .contentType(MediaType.APPLICATION_JSON)
  //               .content(objectMapper.writeValueAsString(productDTO)))
  //       .andExpect(status().isOk())
  //       .andExpect(jsonPath("$.id").value(product.getId()))
  //       .andExpect(jsonPath("$.name").value(product.getName()));
  // }

  // @Test
  // void updateProduct_nonExistingId_returnsNotFound() throws Exception {
  //   Mockito.when(productService.updateProduct(eq(1L), any(ProductDTO.class), eq(0)))
  //       .thenThrow(new ProductNotFoundException("Product not found"));

  //   mockMvc
  //       .perform(
  //           MockMvcRequestBuilders.put("/api/v1/products/1?version=0")
  //               .contentType(MediaType.APPLICATION_JSON)
  //               .content(objectMapper.writeValueAsString(productDTO)))
  //       .andExpect(status().isNotFound())
  //       .andExpect(jsonPath("$.message").value("Product not found"));
  // }

  // @Test
  // void deleteProduct_existingId_returnsNoContent() throws Exception {
  //   doNothing().when(productService).deleteProduct(1L);

  //   mockMvc
  //       .perform(MockMvcRequestBuilders.delete("/api/v1/products/1"))
  //       .andExpect(status().isNoContent());
  // }

  // @Test
  // void deleteProduct_nonExistingId_returnsNotFound() throws Exception {
  //   doThrow(new ProductNotFoundException("Product not found"))
  //       .when(productService)
  //       .deleteProduct(1L);

  //   mockMvc
  //       .perform(MockMvcRequestBuilders.delete("/api/v1/products/1"))
  //       .andExpect(status().isNotFound())
  //       .andExpect(jsonPath("$.message").value("Product not found"));
  // }
// }
