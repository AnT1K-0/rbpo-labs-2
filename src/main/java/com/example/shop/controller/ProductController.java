package com.example.shop.controller;

import com.example.shop.model.Product;
import com.example.shop.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ShopService svc;

    @PostMapping
    public Product create(@Valid @RequestBody Product p) {
        return svc.createProduct(p);
    }

    @GetMapping
    public List<Product> all() {
        return svc.listProducts();
    }

    @GetMapping("/{id}")
    public Product one(@PathVariable Long id) {
        return svc.getProduct(id);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody Product p) {
        return svc.updateProduct(id, p);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        svc.deleteProduct(id);
    }
}