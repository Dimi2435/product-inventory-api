# Product Inventory API

This is a REST API for a "Product Inventory" application built using Spring Boot. The API allows users to manage products, including creating, retrieving, updating, and deleting products. It also supports pagination, validation, and error handling.

## Features

- Create, Read, Update, and Delete products
- In-memory H2 database
- RESTful endpoints
- Unit tests for API endpoints
- Swagger documentation

## Prerequisites

- Java (>= v17)
- Maven
- H2 Database (embedded)
- Swagger for API documentation

## Getting Started

### 1. Clone the Repository
```bash
git clone <repository-url>
cd product-inventory-api
```

### 2. Build the Application
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`


### 4. Access the H2 Database Console

The application uses an H2 in-memory database. You can access the H2 console at:
`http://localhost:8080/h2-console`

Database configuration:
- JDBC URL: `jdbc:h2:mem:productdb` (in the file is a another hardcoded value)
- Username: `sa`
- Password: (empty)

### 5. API Documentation

The API documentation is available at:
`http://localhost:8080/swagger-ui.html`

### 6. Testing the API with curl

Here are some example curl commands to test the API:

- **Create a Product**:

```bash
curl -X 'POST' \
  'http://localhost:8080/api/v1/products' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Premium Laptop",
  "description": "High-performance laptop with 16GB RAM",
  "price": 999.99,
  "quantity": 10,
  "categoryId": 1,
  "sku": "LAP-001",
  "weight": 2.5,
  "dimensions": "30x20x5",
  "version": 0
}'
```
- **Get All Products**:

```bash
curl -X 'GET' \
  'http://localhost:8080/api/v1/products?page=0&size=10&sortBy=name&direction=asc' \
  -H 'accept: */*'
```

- **Get a Product by ID**:

```bash
curl -X 'GET' \
  'http://localhost:8080/api/v1/products/1' \
  -H 'accept: */*'
```

- **Update a Product**:

```bash
curl -X 'PUT' \
  'http://localhost:8080/api/v1/products/1?version=1' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Premium Laptop",
  "description": "High-performance laptop with 16GB RAM",
  "price": 999.99,
  "quantity": 10,
  "categoryId": 1,
  "sku": "LAP-002",
  "weight": 2.5,
  "dimensions": "30x20x5",
  "version": 0
}'
```

- **Delete a Product**:

```bash
curl -X 'DELETE' \
  'http://localhost:8080/api/v1/products/1' \
  -H 'accept: */*'
```


### 7. Running Tests

To run the unit tests, use the following command:

```bash
mvn test
```

## Conclusion

This README provides a comprehensive guide to setting up and using the Product Inventory API. For any questions or issues, please feel free to reach out.