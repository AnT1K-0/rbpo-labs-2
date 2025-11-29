package com.example.shop.controller;

import com.example.shop.model.OrderItem;
import com.example.shop.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {
    private final ShopService svc;

    @PostMapping
    public OrderItem create(@Valid @RequestBody OrderItem oi) {
        return svc.createOrderItem(oi);
    }

    @GetMapping
    public List<OrderItem> all() {
        return svc.listOrderItems();
    }

    @GetMapping("/{id}")
    public OrderItem one(@PathVariable Long id) {
        return svc.getOrderItem(id);
    }

    @PutMapping("/{id}")
    public OrderItem update(@PathVariable Long id, @Valid @RequestBody OrderItem oi) {
        return svc.updateOrderItem(id, oi);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        svc.deleteOrderItem(id);
    }
}