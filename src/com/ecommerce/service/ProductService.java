package com.ecommerce.service;

import com.ecommerce.model.entities.Product;
import com.ecommerce.repository.ProductRepository;

import java.io.*;
import java.util.*;

public class ProductService {
    private int productIdCounter;
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
        this.productIdCounter = calculateInitialCounter(repository.load());
    }

    private int calculateInitialCounter(Collection<Product> products) {
        int maxId = 0;
        for (Product product : products) {
            if (product.getId() > maxId) {
                maxId = product.getId();
            }
        }
        return maxId + 1;
    }

    public void addProduct(String name, double price, String category) {
        int id = productIdCounter++;
        Product product = new Product(id, name, price, category);
        List<Product> products = new ArrayList<>(repository.load());
        products.add(product);
        repository.save(products);
        System.out.println("✅ Product '" + name + "' added successfully with ID " + id + ".");
    }

    public void listProducts() {
        Collection<Product> products = repository.load();
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        System.out.println("Product List:");
        for (Product product : products) {
            System.out.println("ID: " + product.getId() + ", Name: " + product.getName() +
                    ", Price: " + product.getPrice() + ", Category: " + product.getCategory());
        }
    }

    public Map<Integer, Product> getAllProducts() {
        Map<Integer, Product> map = new HashMap<>();
        for (Product product : repository.load()) {
            map.put(product.getId(), product);
        }
        return map;
    }

    public void updateProduct(int id, String newName, double newPrice, String newCategory) {
        List<Product> products = new ArrayList<>(repository.load());
        boolean found = false;

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                products.set(i, new Product(id, newName, newPrice, newCategory));
                found = true;
                break;
            }
        }

        if (found) {
            repository.save(products);
            System.out.println("✅ Product updated.");
        } else {
            System.out.println("❌ Product ID not found.");
        }
    }

    public void removeProduct(int id) {
        List<Product> products = new ArrayList<>(repository.load());
        boolean removed = products.removeIf(p -> p.getId() == id);

        if (removed) {
            repository.save(products);
            System.out.println("✅ Product with ID " + id + " successfully removed.");
        } else {
            System.out.println("❌ Product ID not found.");
        }
    }

    public Product getProductById(int id) {
        for (Product product : repository.load()) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public List<Product> getFilteredProducts(ProductFilter filter, String criteria) {
        List<Product> products = new ArrayList<>(repository.load());
        return filter.filter(products, criteria);
    }

}