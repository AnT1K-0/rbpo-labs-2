package com.example.shop.service;

import com.example.shop.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {
    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    // CREATE
    public Product createProduct(Product product) {
        product.setId(nextId.getAndIncrement());
        products.put(product.getId(), product);
        return product;
    }

    // READ
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public Product getProductById(Long id) {
        return products.get(id);
    }

    // UPDATE
    public Product updateProduct(Long id, Product product) {
        if (products.containsKey(id)) {
            product.setId(id);
            products.put(id, product);
            return product;
        }
        return null;
    }

    // DELETE
    public boolean deleteProduct(Long id) {
        return products.remove(id) != null;
    }
}