package com.ecommerce.controller;


import com.ecommerce.model.entities.Product;
import com.ecommerce.service.ProductService;
import com.ecommerce.view.ProductView;

import java.util.List;


public class ProductController {
    private ProductService productService;
    private final ProductView productView;

    public ProductController(ProductService productService, ProductView productView) {
        this.productService = productService;
        this.productView = productView;
    }

    public void getProducts() {
        List<Product> products = productService.getAllProducts();
        productView.displayAllProducts(products);
    }


    public void createProduct(String name, double price, String category) {
        productService.addProduct(name, price, category);
    }


    public void updateProduct(int id, String newName, double newPrice, String newCategory) {
        productService.updateProduct(id, newName, newPrice, newCategory);
    }

public List<Product> filterProductsByCategory(String category) {
    return productService.getProductsByCategory(category);
}

    public boolean deleteProduct(int id) {
        return productService.removeProduct(id);
    }
}


