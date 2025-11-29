package com.example.shop.repository;

import com.example.shop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);

    void deleteByOrderIdAndProductId(Long orderId, Long productId);
}