package com.example.shop.service;

import com.example.shop.model.OrderItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    private final Map<Long, OrderItem> orderItems = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    // CREATE
    public OrderItem createOrderItem(OrderItem orderItem) {
        orderItem.setId(nextId.getAndIncrement());
        orderItems.put(orderItem.getId(), orderItem);
        return orderItem;
    }

    // READ
    public List<OrderItem> getAllOrderItems() {
        return new ArrayList<>(orderItems.values());
    }

    public OrderItem getOrderItemById(Long id) {
        return orderItems.get(id);
    }

    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItems.values().stream()
                .filter(item -> orderId.equals(item.getOrder().getId()))
                .collect(Collectors.toList());
    }

    // UPDATE
    public OrderItem updateOrderItem(Long id, OrderItem orderItem) {
        if (orderItems.containsKey(id)) {
            orderItem.setId(id);
            orderItems.put(id, orderItem);
            return orderItem;
        }
        return null;
    }

    // DELETE
    public boolean deleteOrderItem(Long id) {
        return orderItems.remove(id) != null;
    }

    public void deleteOrderItemsByOrderId(Long orderId) {
        orderItems.entrySet().removeIf(entry -> orderId.equals(entry.getValue().getOrder().getId()));
    }
    }