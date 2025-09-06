package com.ecommerce.repository;

import com.ecommerce.model.entities.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomerRepository {

    private final String filePath;

    public CustomerRepository(String filePath) {
        this.filePath = filePath;
    }

    public void save(Collection<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Customer c : customers) {
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

    public List<Customer> load() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return customers;

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
                customers.add(customer);

                if (customerId > maxId) maxId = customerId;
            }
            Customer.setIdCount(maxId + 1);
            System.out.println("📂 Customers loaded successfully.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Failed to load customers: " + e.getMessage());
        }

        return customers;
    }
}
