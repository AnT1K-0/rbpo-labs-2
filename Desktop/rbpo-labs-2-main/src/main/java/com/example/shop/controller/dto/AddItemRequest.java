package com.example.shop.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddItemRequest(@NotNull Long productId, @Positive int quantity) {
}