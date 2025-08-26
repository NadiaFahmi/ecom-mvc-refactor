package com.ecommerce.service;

import com.ecommerce.model.entities.Product;

import java.io.*;
import java.util.*;

public class ProductService {
    private static int productIdCounter = 1;

    public ProductService() {
        loadProductsFromFile();
    }

    private final static Map<Integer, Product> productMap = new HashMap<>();

    private static final String FILE_PATH = "products.txt";

    public void addProduct(String name, double price, String category) {
        int id = productIdCounter++;
        Product product = new Product(id, name, price, category);
        productMap.put(id, product);
        saveToFile();
        System.out.println("✅ Product '" + name + "' added successfully with ID " + id + ".");

    }

    public void listProducts() {
        if (productMap.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        System.out.println("Product List:");
        for (Product product : productMap.values()) {
            System.out.println("ID: " + product.getId() + ", Name: " + product.getName() +
                    ", Price: " + product.getPrice() + ", Category: " + product.getCategory());
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Product product : productMap.values()) {
                writer.write(product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getCategory());
                writer.newLine();
            }
            System.out.println("📁 Saved to " + FILE_PATH);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            throw new RuntimeException("Failed to save products to file: " + FILE_PATH, e);
        }
    }

    public void loadProductsFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("No existing product file found. Starting fresh.");
            return;
        }
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length != 4) {
                    System.out.println("Skipping invalid line: " + line);
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
                    System.out.println("Error parsing line: " + line);
                }
            }
            productIdCounter = maxId + 1;
            System.out.println("Products loaded from file.");
        } catch (IOException e) {
            System.out.println("Error reading file. Starting fresh.");
        }
    }

    public void updateProduct(int id, String newName, double newPrice, String newCategory) {
        if (!productMap.containsKey(id)) {
            System.out.println("Product ID not found.");
            return;
        }

        Product updatedProduct = new Product(id, newName, newPrice, newCategory);
        productMap.put(id, updatedProduct);

        saveToFile();
        System.out.println("Product updated.");


    }

    public void removeProduct(int id) {


        if (productMap.containsKey(id)) {
            productMap.remove(id);
            saveToFile();
            System.out.println("✅ Product with ID " + id + " successfully removed.");
        } else {
            System.out.println("❌ Product ID not found.");
        }

    }

    public Product getProductById(int id) {
        return productMap.get(id);
    }

    public List<Product> getFilteredProducts(ProductFilter filter, String criteria) {
        List<Product> productList = new ArrayList<>(productMap.values());
        return filter.filter(productList, criteria);
    }

}