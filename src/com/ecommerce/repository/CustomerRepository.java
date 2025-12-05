package com.ecommerce.repository;

import com.ecommerce.exception.CustomerNotFoundException;
import com.ecommerce.model.entities.Customer;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class CustomerRepository {
    private int idCounter =1;
    private static final String DELIMITER = ",";
    private final String filePath;
    private Map<Integer, Customer> customerMap = new HashMap<>();

    public CustomerRepository(String filePath) {
        this.filePath = filePath;
    }


    public Customer createCustomer(String name, String email, String password,
                                   double balance, String address){
        int id = idCounter++;
        Customer customer = new Customer(id,name,email,password,balance,address);
        customerMap.put(id,customer);
        saveAll();
        return customer;
    }
    public void saveAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Customer c : customerMap.values()) {

                writer.write(String.join(DELIMITER,
                        String.valueOf(c.getId()),
                        c.getName(),
                        c.getEmail(),
                        c.getPassword(),
                        String.valueOf(c.getBalance()),
                        c.getAddress()));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to save customers to file");

        }
    }
    public void saveCustomer(Customer customer){
        customerMap.put(customer.getId(),customer);
        saveAll();
    }

    public Customer getCustomerByEmail(String email) {
        String normalized = email.trim().toLowerCase();
        for (Customer customer : customerMap.values()) {
            if (customer.getEmail().trim().toLowerCase().equals(normalized)) {
                return customer;
            }
        }
        return null;
    }

    public Customer getCustomerById(int id) {
        Customer customer = customerMap.get(id);
        if (customer == null) {
            throw new NoSuchElementException("❌ Customer with ID " + id + " not found.");
        }
        return customer;

    }

    //
    public Collection<Customer> getAllCustomers() {
        return customerMap.values();
    }

    public void updateCustomer(Customer updatedCustomer) {
        int id = updatedCustomer.getId();
        if (!customerMap.containsKey(id)) {
            throw new NoSuchElementException("❌ Customer with ID " + id + " not found.");
        }
        customerMap.put(id, updatedCustomer);
    }

    public void deleteByEmail(String email) {

        Customer customer = customerMap.values().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElse(null);
//        return customer != null && customerMap.remove(customer.getId()) != null;
        customerMap.remove(customer.getId());
    }

    public void load() {
        File file = new File(filePath);
        if (!file.exists()) return;

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
                customerMap.put(customerId, customer);

                if (customerId > maxId) maxId = customerId;
            }
            idCounter = maxId+1;
        } catch (IOException | NumberFormatException e) {
            throw new IllegalArgumentException("Failed to load customers to file");

        }
    }
}