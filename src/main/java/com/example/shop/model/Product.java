package com.example.shop.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class Product {
    private Long id;

    @NotBlank(message = "Название товара обязательно")
    private String name;

    private String description;

    @NotNull(message = "Цена обязательна")
    @Positive(message = "Цена должна быть положительной")
    private BigDecimal price;

    @NotNull(message = "Категория обязательна")
    private Long categoryId;
}