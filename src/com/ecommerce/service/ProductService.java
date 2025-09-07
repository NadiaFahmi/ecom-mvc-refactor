package com.ecommerce.service;

import com.ecommerce.model.entities.Product;
import com.ecommerce.repository.ProductRepository;

import java.io.*;
import java.util.*;

public class ProductService {

    private int productIdCounter;
    private final Map<Integer, Product> productMap;
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
        this.productMap = repository.load();
        this.productIdCounter = calculateInitialCounter(productMap);
    }
private int calculateInitialCounter(Map<Integer, Product> map) {
    int maxId = 0;
    for (int id : map.keySet()) {
        if (id > maxId) {
            maxId = id;
        }
    }
    return maxId + 1;
}

    public void addProduct(String name, double price, String category) {
        int id = productIdCounter++;
        Product product = new Product(id, name, price, category);
        productMap.put(id, product);
        saveProducts();
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
        public void saveProducts() {
            repository.save(productMap.values());}
//
    public void updateProduct(int id, String newName, double newPrice, String newCategory) {

        if (!productMap.containsKey(id)) {
            System.out.println("Product ID not found.");
            return;
        }

        Product updatedProduct = new Product(id, newName, newPrice, newCategory);
        productMap.put(id, updatedProduct);

        saveProducts();
        System.out.println("Product updated.");
    }

    public void removeProduct(int id) {

        if (productMap.containsKey(id)) {
            productMap.remove(id);
            saveProducts();
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