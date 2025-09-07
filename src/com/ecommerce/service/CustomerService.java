package com.ecommerce.service;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.repository.CustomerRepository;

import java.io.*;
import java.util.*;

public class CustomerService {

    private final CustomerRepository repository;

    private final Map<String, Customer> customerMap = new HashMap<>();

    //
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }
    //
    public void addCustomer(Customer customer) {
        String emailKey = customer.getEmail().trim().toLowerCase();
        customerMap.put(emailKey, customer);
    }

    public Customer getCustomerByEmail(String email) {
        return customerMap.get(email.trim().toLowerCase());
    }

    public Customer getCustomerById(int id) {
        for (Customer c : customerMap.values()) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public Collection<Customer> getAllCustomers() {
        return customerMap.values();
    }

        public void saveCustomers() {
            repository.save(customerMap.values());

    }

    public void loadCustomers() {
        List<Customer> loaded = repository.load();
        for (Customer c : loaded) {
            customerMap.put(c.getEmail().toLowerCase(), c);
        }

    }


    public Customer findCustomerByIdFromMap(int id) {
        for (Customer c : customerMap.values()) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }
    public void updateCustomerEmail(int customerId, String newEmail) {
        Customer customer = findCustomerByIdFromMap(customerId);

        if (customer == null) {
            System.out.println("❌ Customer not found.");
            return;
        }

        if (newEmail == null || newEmail.isBlank() ||
                !newEmail.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            System.out.println("⚠️ Invalid email format.");
            return;
        }

        String emailKey = newEmail.trim().toLowerCase();
        if (customerMap.containsKey(emailKey)) {

            System.out.println("⚠️ This email is already used by another customer.");
            return;
        }

        customerMap.remove(customer.getEmail().toLowerCase());
        customer.setEmail(emailKey);
        customerMap.put(emailKey, customer);

        System.out.println("✅ Email updated successfully.");

    }
    public Map<String, Customer> getCustomerMap() {
        return customerMap;
    }

    public boolean existsEmail(String email) {
        if (email == null || email.isBlank()) return false;
        String emailKey = email.trim().toLowerCase();
        return customerMap.containsKey(emailKey);
    }
    }

