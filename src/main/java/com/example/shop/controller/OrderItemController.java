package com.example.shop.controller;

import com.example.shop.model.OrderItem;
import com.example.shop.service.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<OrderItem> createOrderItem(@Valid @RequestBody OrderItem orderItem) {
        return ResponseEntity.ok(orderItemService.createOrderItem(orderItem));
    }

    @GetMapping
    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
        return ResponseEntity.ok(orderItemService.getAllOrderItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long id) {
        OrderItem orderItem = orderItemService.getOrderItemById(id);
        return orderItem != null ? ResponseEntity.ok(orderItem) : ResponseEntity.notFound().build();
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemService.getOrderItemsByOrderId(orderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable Long id, @Valid @RequestBody OrderItem orderItem) {
        OrderItem updated = orderItemService.updateOrderItem(id, orderItem);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        boolean deleted = orderItemService.deleteOrderItem(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}