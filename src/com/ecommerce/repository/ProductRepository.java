package com.ecommerce.repository;

import com.ecommerce.model.entities.Product;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProductRepository {

    private final String filePath;

    public ProductRepository(String filePath) {
        this.filePath = filePath;
    }

    public void save(Collection<Product> products)

    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Product product : products) {
                writer.write(product.getId() + "," + product.getName() + "," +
                        product.getPrice() + "," + product.getCategory());
                writer.newLine();
            }
            System.out.println("📁 Products saved to " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save products to file: " + filePath, e);
        }
    }
    public Map<Integer, Product> load() {
        Map<Integer, Product> productMap = new HashMap<>();
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("📂 No existing product file found. Starting fresh.");
            return productMap;
        }

        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length != 4) {
                    System.out.println("⚠️ Skipping invalid line: " + line);
                    continue;
                }
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    String category = parts[3].trim();

                    productMap.put(id, new Product(id, name, price, category));
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Error parsing line: " + line);
                }
            }
            System.out.println("✅ Products loaded from file.");
        } catch (IOException e) {
            System.out.println("❌ Error reading file. Starting fresh.");
        }

        return productMap;
    }
}
