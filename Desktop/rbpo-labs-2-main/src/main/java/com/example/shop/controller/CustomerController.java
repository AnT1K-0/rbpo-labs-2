package com.example.shop.controller;

import com.example.shop.model.Customer;
import com.example.shop.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final ShopService svc;

    @PostMapping
    public Customer create(@Valid @RequestBody Customer c) {
        return svc.createCustomer(c);
    }

    @GetMapping
    public List<Customer> all() {
        return svc.listCustomers();
    }

    @GetMapping("/{id}")
    public Customer one(@PathVariable Long id) {
        return svc.getCustomer(id);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @Valid @RequestBody Customer c) {
        return svc.updateCustomer(id, c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        svc.deleteCustomer(id);
    }
}