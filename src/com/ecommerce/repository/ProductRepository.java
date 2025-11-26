package com.ecommerce.repository;

import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.model.entities.Product;

import java.io.*;
import java.util.*;

public class ProductRepository {
    private static final String ITEM_PART_DELIMITER = ",";
    private final String filePath;

    public ProductRepository(String filePath) {
        this.filePath = filePath;
    }

    public Collection<Product> load() {
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product product = parseProduct(line);
                if (product != null) {
                    products.add(product);
                }
            }
        } catch (IOException e) {
            throw new InvalidProductException("Failed to load products: ");
        }

        return products;
    }

    public void save(Collection<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Product product : products) {
                writer.write(formatProduct(product));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new InvalidProductException("Failed to save products: ");
        }
    }

    private Product parseProduct(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) return null;

        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            double price = Double.parseDouble(parts[2].trim());
            String category = parts[3].trim();
            return new Product(id, name, price, category);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String formatProduct(Product product) {
        return product.getId() + ITEM_PART_DELIMITER + product.getName() + ITEM_PART_DELIMITER + product.getPrice() + ITEM_PART_DELIMITER + product.getCategory();
    }
}
