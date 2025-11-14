-- V2__seed.sql
INSERT INTO categories (name,description) VALUES ('Phones','Smartphones') ON CONFLICT(name) DO NOTHING;
INSERT INTO categories (name,description) VALUES ('Accessories','Gadgets') ON CONFLICT(name) DO NOTHING;
INSERT INTO products (name,description,price,category_id) SELECT 'iPhone 15','Apple smartphone',999.99,id FROM categories WHERE name='Phones' ON CONFLICT DO NOTHING;
INSERT INTO products (name,description,price,category_id) SELECT 'USB-C Cable','1m cable',9.99,id FROM categories WHERE name='Accessories' ON CONFLICT DO NOTHING;
INSERT INTO customers (first_name,last_name,email,phone) VALUES ('Ivan','Ivanov','ivan@example.com','+49111111111') ON CONFLICT(email) DO NOTHING;
INSERT INTO customers (first_name,last_name,email,phone) VALUES ('Anna','Smirnova','anna@example.com','+49222222222') ON CONFLICT(email) DO NOTHING;
