package com.example.shop.service;

import com.example.shop.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CategoryService {
    private final Map<Long, Category> categories = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    // CREATE
    public Category createCategory(Category category) {
        category.setId(nextId.getAndIncrement());
        categories.put(category.getId(), category);
        return category;
    }

    // READ
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories.values());
    }

    public Category getCategoryById(Long id) {
        return categories.get(id);
    }

    // UPDATE
    public Category updateCategory(Long id, Category category) {
        if (categories.containsKey(id)) {
            category.setId(id);
            categories.put(id, category);
            return category;
        }
        return null;
    }

    // DELETE
    public boolean deleteCategory(Long id) {
        return categories.remove(id) != null;
    }
}