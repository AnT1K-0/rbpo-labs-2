package com.example.shop.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CheckoutRequest(@NotNull Long customerId, List<Item> items) {
    public record Item(@NotNull Long productId, @Positive int quantity) {
    }
}