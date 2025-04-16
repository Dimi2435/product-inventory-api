package com.example.productinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Product Inventory application. This class contains the main method which
 * starts the Spring Boot application.
 */
@SpringBootApplication
public class ProductInventoryApplication {
  /**
   * The main method that serves as the entry point for the Spring Boot application.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(ProductInventoryApplication.class, args);
  }
}
