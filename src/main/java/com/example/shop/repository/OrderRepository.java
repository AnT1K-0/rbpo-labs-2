package com.example.shop.repository;

import com.example.shop.model.Order;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Загрузка всех заказов сразу со связанными сущностями
    @Query("select distinct o from Order o " +
            "left join fetch o.items i " +
            "left join fetch i.product " +
            "left join fetch o.customer")
    List<Order> findAllDetailed();

    // Загрузка одного заказа по id с подгрузкой связей
    @Query("select o from Order o " +
            "left join fetch o.items i " +
            "left join fetch i.product " +
            "left join fetch o.customer " +
            "where o.id = :id")
    Optional<Order> findDetailedById(@Param("id") Long id);
}
