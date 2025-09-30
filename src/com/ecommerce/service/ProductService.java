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
        List<Product> products = loadProductList();
        products.add(product);
        repository.save(products);
        System.out.println("✅ Product '" + name + "' added successfully with ID " + id + ".");
    }


    public List<Product> getAllProducts() {
        Collection<Product> loaded = repository.load();
        return new ArrayList<>(loaded); // ensures it's mutable and list-based
    }

//
    public void updateProduct(int id, String newName, double newPrice, String newCategory) {
        List<Product> products = loadProductList();
        Product updatedProduct = new Product(id, newName, newPrice, newCategory);
        boolean updated= false;

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                products.set(i, updatedProduct);
                updated = true;
                break;
            }
        }

        if (updated) {
            repository.save(products);
            System.out.println("✅ Product updated: " + updatedProduct.getName());
        } else {
            System.out.println("❌ Product ID " + id + " not found.");
        }
    }

    public void removeProduct(int id) {
        List<Product> products = loadProductList();
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
        List<Product> products = loadProductList();
        return filter.filter(products, criteria);
    }
    private List<Product> loadProductList() {
        return new ArrayList<>(repository.load());
    }
}