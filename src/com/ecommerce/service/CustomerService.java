package com.ecommerce.service;

import com.ecommerce.model.entities.Customer;

import java.io.*;
import java.util.*;

public class CustomerService {

    private final Map<String, Customer> customerMap = new HashMap<>();

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

    public void saveCustomersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt"))) {
            for (Customer c : customerMap.values()) {
                writer.write(c.getId() + "," +
                        c.getName() + "," +
                        c.getEmail() + "," +
                        c.getPassword() + "," +
                        c.getBalance() + "," +
                        c.getAddress());
                writer.newLine();
            }
            System.out.println("✅ Customers saved successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error saving customers: " + e.getMessage());
        }
    }

    public void loadCustomersFromFile() {
        File file = new File("customers.txt");
        if (!file.exists()) return;

        customerMap.clear();
        int maxId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                int customerId = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String email = parts[2].trim().toLowerCase();
                String password = parts[3].trim();
                double balance = Double.parseDouble(parts[4].trim());
                String address = parts[5].trim();

                Customer customer = new Customer(customerId, name, email, password, balance, address);
                customerMap.put(email, customer);

                if (customerId > maxId) maxId = customerId;
            }
            Customer.setIdCount(maxId + 1);
            System.out.println("📂 Customers loaded successfully.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Failed to load customers: " + e.getMessage());
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
    //////
    public boolean existsEmail(String email) {
        if (email == null || email.isBlank()) return false;
        String emailKey = email.trim().toLowerCase();
        return customerMap.containsKey(emailKey);
    }
    }

