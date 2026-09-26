select version();
drop schema product_hub;
create database product_hub;
show databases;
use product_hub;
select database();
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(12,2) NOT NULL,
    category VARCHAR(100) NOT NULL,
    image_url VARCHAR(500),
    available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_product_created_id
ON products (created_at DESC, id DESC);
describe products;

CREATE INDEX idx_product_price_created_id
ON products (price, created_at DESC, id DESC);