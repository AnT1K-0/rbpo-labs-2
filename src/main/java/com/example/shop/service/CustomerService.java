package com.example.shop.service;

import com.example.shop.model.Customer;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomerService {
    private final Map<Long, Customer> customers = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    // CREATE
    public Customer createCustomer(Customer customer) {
        customer.setId(nextId.getAndIncrement());
        customers.put(customer.getId(), customer);
        return customer;
    }

    // READ
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public Customer getCustomerById(Long id) {
        return customers.get(id);
    }

    // UPDATE
    public Customer updateCustomer(Long id, Customer customer) {
        if (customers.containsKey(id)) {
            customer.setId(id);
            customers.put(id, customer);
            return customer;
        }
        return null;
    }

    // DELETE
    public boolean deleteCustomer(Long id) {
        return customers.remove(id) != null;
    }
}