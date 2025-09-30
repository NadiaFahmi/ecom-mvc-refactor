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

    public void handleListProducts() {
        List<Product> products = productService.getAllProducts();
        productView.displayAllProducts(products);
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
