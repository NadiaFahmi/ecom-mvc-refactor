package com.ecommerce.service;

import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.model.entities.Product;
import com.ecommerce.repository.ProductRepository;

import java.io.*;
import java.util.*;

public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public void addProduct(String name, double price, String category) {

        int id = calculateInitialCounter(loadProductList());
        Product product = new Product(id, name, price, category);

        List<Product> products = loadProductList();
        products.add(product);
        repository.save(products);
    }

    public List<Product> getAllProducts() {
        Collection<Product> loaded = loadProductList();
        return new ArrayList<>(loaded);
    }

    public Product getProductById(int id) {
        for (Product product : loadProductList()) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> products = loadProductList();
        List<Product> filtered = new ArrayList<>();
        if(category.isEmpty()){
            throw new IllegalArgumentException("Category must not be empty");
        }

        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filtered.add(product);
            }
        }

        return filtered;
    }

    public boolean removeProduct(int id) {
        List<Product> products = loadProductList();
        boolean removed = products.removeIf(p -> p.getId() == id);

        if (removed) {
            repository.save(products);
        }else{
            throw new InvalidProductException("productId not found");
        }
        return removed;
    }

    public void updateProduct(int id, String newName, double newPrice, String newCategory) {
        List<Product> products = loadProductList();
        Product updatedProduct = new Product(id, newName, newPrice, newCategory);
        boolean updated = false;

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                products.set(i, updatedProduct);
                updated = true;
                break;
            }
        }
        if (updated) {
            repository.save(products);
        } else {
            throw new InvalidProductException("❌ Product ID " + id + " not found.");
        }
    }

    private List<Product> loadProductList() {
        return new ArrayList<>(repository.load());
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
}