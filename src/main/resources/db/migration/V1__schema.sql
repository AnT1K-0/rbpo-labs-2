-- V1__schema.sql
CREATE TABLE categories (id BIGSERIAL PRIMARY KEY, name TEXT NOT NULL UNIQUE, description TEXT);
CREATE TABLE products (id BIGSERIAL PRIMARY KEY, name TEXT NOT NULL, description TEXT, price NUMERIC(12,2) NOT NULL CHECK (price>0), category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE RESTRICT, UNIQUE(name,category_id));
CREATE INDEX idx_products_category ON products(category_id);
CREATE TABLE customers (id BIGSERIAL PRIMARY KEY, first_name TEXT NOT NULL, last_name TEXT NOT NULL, email TEXT NOT NULL UNIQUE, phone TEXT);
CREATE TABLE orders (id BIGSERIAL PRIMARY KEY, customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE RESTRICT, order_date TIMESTAMPTZ NOT NULL DEFAULT now(), status TEXT NOT NULL CHECK (status IN ('CREATED','PAID','CANCELLED')), total NUMERIC(12,2) NOT NULL DEFAULT 0);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE TABLE order_items (id BIGSERIAL PRIMARY KEY, order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE, product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE RESTRICT, quantity INT NOT NULL CHECK (quantity>0), price NUMERIC(12,2) NOT NULL CHECK (price>0), UNIQUE(order_id,product_id));
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_product ON order_items(product_id);
