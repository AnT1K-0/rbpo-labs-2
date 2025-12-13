package com.example.shop.service;

import com.example.shop.model.*;
import com.example.shop.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    // ---------- CATEGORY ----------
    public Category createCategory(Category c) {
        return categoryRepo.save(c);
    }

    public List<Category> listCategories() {
        return categoryRepo.findAll();
    }

    public Category getCategory(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    public Category updateCategory(Long id, Category c) {
        c.setId(id);
        return categoryRepo.save(c);
    }

    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    // ---------- PRODUCT ----------
    public Product createProduct(Product p) {
        Category c = categoryRepo.findById(p.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        p.setCategory(c);
        return productRepo.save(p);
    }

    public List<Product> listProducts() {
        return productRepo.findAll();
    }

    public Product getProduct(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public Product updateProduct(Long id, Product p) {
        p.setId(id);
        return productRepo.save(p);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    // ---------- CUSTOMER ----------
    public Customer createCustomer(Customer c) {
        return customerRepo.save(c);
    }

    public List<Customer> listCustomers() {
        return customerRepo.findAll();
    }

    public Customer getCustomer(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    public Customer updateCustomer(Long id, Customer c) {
        c.setId(id);
        return customerRepo.save(c);
    }

    public void deleteCustomer(Long id) {
        customerRepo.deleteById(id);
    }

    // ---------- ORDER ----------
    public Order createOrder(Order o) {
        o = orderRepo.save(o);
        return orderRepo.findDetailedById(o.getId()).orElseThrow();
    }

    public List<Order> listOrders() {
        return orderRepo.findAllDetailed();
    }

    public Order getOrder(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    public Order updateOrder(Long id, Order o) {
        o.setId(id);
        o = orderRepo.save(o);
        return orderRepo.findDetailedById(o.getId()).orElseThrow();
    }

    public void deleteOrder(Long id) {
        orderRepo.deleteById(id);
    }

    // ---------- ORDER ITEM ----------
    public OrderItem createOrderItem(OrderItem oi) {
        return orderItemRepo.save(oi);
    }

    public List<OrderItem> listOrderItems() {
        return orderItemRepo.findAll();
    }

    public OrderItem getOrderItem(Long id) {
        return orderItemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found"));
    }

    public OrderItem updateOrderItem(Long id, OrderItem oi) {
        oi.setId(id);
        return orderItemRepo.save(oi);
    }

    public void deleteOrderItem(Long id) {
        orderItemRepo.deleteById(id);
    }

    // ---------- HELPERS ----------
    private double computeTotal(Long orderId) {
        return orderItemRepo.findByOrderId(orderId).stream()
                .mapToDouble(oi -> oi.getQuantity() * oi.getPrice())
                .sum();
    }

    private void ensureOrderStatus(Order o, OrderStatus expected) {
        if (o.getStatus() != expected)
            throw new IllegalStateException("Invalid order status");
    }

    // ---------- CHECKOUT ----------
    @Transactional
    public Order checkout(Long customerId, List<OrderItem> items) {
        Customer c = customerRepo.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Order o = new Order();
        o.setCustomer(c);
        o.setOrderDate(OffsetDateTime.now());
        o.setStatus(OrderStatus.CREATED);
        o = orderRepo.save(o);

        List<OrderItem> savedItems = new ArrayList<>();

        for (OrderItem req : items) {
            Product p = productRepo.findById(req.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            int qty = req.getQuantity();
            if (qty <= 0) {
                throw new IllegalArgumentException("Quantity must be > 0");
            }

            if (p.getStock() < qty) {
                throw new IllegalStateException("Not enough stock for product: " + p.getName());
            }

            p.setStock(p.getStock() - qty);
            productRepo.save(p);

            OrderItem oi = new OrderItem();
            oi.setOrder(o);
            oi.setProduct(p);
            oi.setQuantity(qty);
            oi.setPrice(p.getPrice());
            savedItems.add(orderItemRepo.save(oi));
        }


        o.setItems(savedItems);
        o.setTotal(computeTotal(o.getId()));
        o.setStatus(OrderStatus.PAID);
        o = orderRepo.save(o);

        return orderRepo.findById(o.getId()).orElseThrow();
    }


    // ---------- ORDER MODIFICATION ----------
    @Transactional
    public Order addItemToOrder(Long orderId, Long productId, int qty) {
        Order o = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        ensureOrderStatus(o, OrderStatus.CREATED);
        Product p = getProduct(productId);
        OrderItem oi = new OrderItem();
        oi.setOrder(o);
        oi.setProduct(p);
        oi.setQuantity(qty);
        oi.setPrice(p.getPrice());
        orderItemRepo.save(oi);
        o.setTotal(computeTotal(orderId));
        o = orderRepo.save(o);
        return orderRepo.findDetailedById(o.getId()).orElseThrow();
    }

    @Transactional
    public Order removeItemFromOrder(Long orderId, Long productId) {
        Order o = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        ensureOrderStatus(o, OrderStatus.CREATED);
        orderItemRepo.deleteByOrderIdAndProductId(orderId, productId);
        o.setTotal(computeTotal(orderId));
        o = orderRepo.save(o);
        return orderRepo.findDetailedById(o.getId()).orElseThrow();
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order o = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (o.getStatus() == OrderStatus.CANCELLED)
            return o;

        // Возвращаем товары на склад
        for (OrderItem item : o.getItems()) {
            Product p = item.getProduct();
            p.setStock(p.getStock() + item.getQuantity());
            productRepo.save(p);
        }

        o.setStatus(OrderStatus.CANCELLED);
        o = orderRepo.save(o);

        return orderRepo.findDetailedById(o.getId()).orElseThrow();
    }


    // ---------- SUMMARY ----------
    @Transactional(readOnly = true)
    public Summary summaryForCustomer(Long customerId, OffsetDateTime from, OffsetDateTime to) {
        var orders = orderRepo.findAll().stream()
                .filter(x -> x.getCustomer().getId().equals(customerId))
                .filter(x -> !x.getOrderDate().isBefore(from) && !x.getOrderDate().isAfter(to))
                .toList();

        long count = orders.size();
        double total = orders.stream().mapToDouble(Order::getTotal).sum();
        return new Summary(count, total);
    }

    public record Summary(long count, double total) {}
}
