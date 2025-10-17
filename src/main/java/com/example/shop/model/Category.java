package com.example.shop.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class Category {
    private Long id;

    @NotBlank(message = "Название категории обязательно")
    private String name;

    private String description;
}