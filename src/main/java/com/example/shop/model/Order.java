package com.example.shop.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    private Long id;
    private LocalDateTime orderDate;
    private String status = "CREATED";

    @NotNull(message = "Покупатель обязателен")
    private Long customerId;

    @NotNull(message = "Список товаров не может быть пустым")
    private List<OrderItem> items = new ArrayList<>();
}