# Product Inventory API

A Spring Boot REST API for managing product inventory.

## Features

- Create, Read, Update, and Delete products
- In-memory H2 database
- RESTful endpoints
- Integration tests

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

1. Clone the repository:
```bash
git clone <repository-url>
cd product-inventory-api
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

- Create a product: `POST /api/products`
- Get all products: `GET /api/products`
- Get a product by ID: `GET /api/products/{id}`
- Update a product: `PUT /api/products/{id}`
- Delete a product: `DELETE /api/products/{id}`

## Database

The application uses an H2 in-memory database. You can access the H2 console at:
`http://localhost:8080/h2-console`

Database configuration:
- JDBC URL: `jdbc:h2:mem:productdb`
- Username: `sa`
- Password: (empty)

## Testing

Run the tests using:
```bash
mvn test
``` 