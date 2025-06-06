CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    quantity INT NOT NULL CHECK (quantity >= 0),
    category_id BIGINT,  -- Keep this for future use, but no foreign key constraint
    sku VARCHAR(50) NOT NULL UNIQUE,  -- Ensure SKU is unique
    weight DECIMAL(10, 2) NOT NULL,
    dimensions VARCHAR(50) NOT NULL,
    version INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);