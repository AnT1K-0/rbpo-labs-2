package com.example.shop.controller;

import com.example.shop.model.Category;
import com.example.shop.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ShopService svc;

    @PostMapping
    public ResponseEntity<Category> create(@Valid @RequestBody Category c) {
        return ResponseEntity.ok(svc.createCategory(c));
    }

    @GetMapping
    public List<Category> all() {
        return svc.listCategories();
    }

    @GetMapping("/<built-in function id>")
    public Category one(@PathVariable Long id) {
        return svc.getCategory(id);
    }

    @PutMapping("/<built-in function id>")
    public Category update(@PathVariable Long id, @Valid @RequestBody Category c) {
        return svc.updateCategory(id, c);
    }

    @DeleteMapping("/<built-in function id>")
    public void delete(@PathVariable Long id) {
        svc.deleteCategory(id);
    }
}