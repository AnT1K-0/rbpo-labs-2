package com.example.shop.controller;

import com.example.shop.controller.dto.AddItemRequest;
import com.example.shop.controller.dto.CheckoutRequest;
import com.example.shop.controller.dto.SummaryResponse;
import com.example.shop.model.Order;
import com.example.shop.model.OrderItem;
import com.example.shop.model.Product;
import com.example.shop.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final ShopService svc;

    @PostMapping
    public Order create(@Valid @RequestBody Order o) {
        return svc.createOrder(o);
    }

    @GetMapping
    public List<Order> all() {
        return svc.listOrders();
    }

    @GetMapping("/{id}")
    public Order one(@PathVariable Long id) {
        return svc.getOrder(id);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable Long id, @Valid @RequestBody Order o) {
        return svc.updateOrder(id, o);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        svc.deleteOrder(id);
    }

    @PostMapping("/checkout")
    public Order checkout(@Valid @RequestBody CheckoutRequest req) {
        var items = req.items().stream().map(i -> {
            var oi = new OrderItem();
            var p = new Product();
            p.setId(i.productId());
            oi.setProduct(p);
            oi.setQuantity(i.quantity());
            return oi;
        }).toList();
        return svc.checkout(req.customerId(), items);
    }

    @PostMapping("/{orderId}/items")
    public Order addItem(@PathVariable Long orderId, @Valid @RequestBody AddItemRequest body) {
        return svc.addItemToOrder(orderId, body.productId(), body.quantity());
    }

    @DeleteMapping("/{orderId}/items/{productId}")
    public Order removeItem(@PathVariable Long orderId, @PathVariable Long productId) {
        return svc.removeItemFromOrder(orderId, productId);
    }

    @PostMapping("/{orderId}/cancel")
    public Order cancel(@PathVariable Long orderId) {
        return svc.cancelOrder(orderId);
    }

    @GetMapping("/customer/{customerId}/summary")
    public SummaryResponse summary(@PathVariable Long customerId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        var s = svc.summaryForCustomer(customerId, from, to);
        return new SummaryResponse(s.count(), s.total());
    }
}
