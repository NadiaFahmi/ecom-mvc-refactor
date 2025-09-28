package com.ecommerce.controller;


import com.ecommerce.service.ProductService;


public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public void handleListProducts() {
        productService.getAllProducts();

    }

    public void handleAddProduct(String name, double price, String category) {
        productService.addProduct(name, price, category);
    }

public void handleRemoveProduct(int id) {
    productService.removeProduct(id);
}
public void handleUpdateProduct(int id, String newName, double newPrice, String newCategory) {
    productService.updateProduct( id,newName, newPrice, newCategory);
}

}
