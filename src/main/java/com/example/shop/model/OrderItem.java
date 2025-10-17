package com.example.shop.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class OrderItem {
    private Long id;

    @NotNull(message = "Количество обязательно")
    @Positive(message = "Количество должно быть положительным")
    private Integer quantity;

    @NotNull(message = "Цена обязательна")
    @Positive(message = "Цена должна быть положительной")
    private BigDecimal price;

    @NotNull(message = "Товар обязателен")
    private Long productId;

    private Long orderId;
}