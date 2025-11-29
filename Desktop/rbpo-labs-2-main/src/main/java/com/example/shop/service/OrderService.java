package com.example.shop.service;

import com.example.shop.model.Order;
import com.example.shop.model.OrderItem;
import org.springframework.stereotype.Service;
import com.example.shop.model.OrderStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {
    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);
    private final OrderItemService orderItemService;

    public OrderService(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // CREATE
    public Order createOrder(Order order) {
        order.setId(nextId.getAndIncrement());
        order.setOrderDate(java.time.OffsetDateTime.now());

        // подстрахуемся, если items == null
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setOrder(order);
                orderItemService.createOrderItem(item);
            }
        }

        orders.put(order.getId(), order);
        return order;
    }


    // READ
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public Order getOrderById(Long id) {
        return orders.get(id);
    }

    // UPDATE (запрещено для оплаченных заказов)
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = orders.get(id);
        if (existingOrder != null && !"PAID".equals(existingOrder.getStatus())) {
            order.setId(id);
            orders.put(id, order);
            return order;
        }
        return null; // Нельзя изменять оплаченные заказы
    }

    // UPDATE STATUS (особый метод для изменения статуса)
    public boolean updateOrderStatus(Long id, String status) {
        Order order = orders.get(id);
        if (order != null) {
            try {
                // Преобразуем строку в значение enum (без учёта регистра)
                OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
                order.setStatus(newStatus);
                return true;
            } catch (IllegalArgumentException e) {
                // если передан некорректный статус
                return false;
            }
        }
        return false;
    }


    // DELETE
    public boolean deleteOrder(Long id) {
        Order order = orders.get(id);
        if (order != null && !"PAID".equals(order.getStatus())) {
            // Удаляем связанные OrderItems
            orderItemService.deleteOrderItemsByOrderId(id);
            return orders.remove(id) != null;
        }
        return false; // Нельзя удалять оплаченные заказы
    }
}