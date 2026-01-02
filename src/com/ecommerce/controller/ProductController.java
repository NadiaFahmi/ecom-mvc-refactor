package com.ecommerce.controller;


import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.model.entities.Product;
import com.ecommerce.service.ProductService;
import com.ecommerce.view.ProductView;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProductController {
    private Logger logger = Logger.getLogger(ProductController.class.getName());
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

    public void createProduct() {
        String name = productView.promptProductName();
        if (name == null || name.equalsIgnoreCase("exit")) {
            return;
        }
        double price= productView.promptPrice();
        if (price == -1 ) {
            return;
        }
        String category= productView.promptProductCategory();
        if (category == null) {
            return;
        }
        try{
        productService.addProduct(name, price, category);
//            logger.log(Level.INFO,"Added new product: name={0}, price={1}, category={2}", new Object[]{name,price,category});
    }catch(InvalidProductException e){
            logger.warning("Invalid product");
            productView.showError(e.getMessage());
        }
    }


    public void updateProduct() {

        int id =productView.promptId();
        if (id == -1 ) {
            return;
        }
        String newName=productView.promptNewName();
        if (newName == null || newName.equalsIgnoreCase("exit")) {
            return;
        }
        double newPrice=productView.promptNewPrice();
        String newCategory=productView.promptNewCategory();

        try {
            productService.updateProduct(id, newName, newPrice, newCategory);
            productView.showUpdatedProduct();
        }catch (InvalidProductException e){
            logger.warning("Invalid product");
            productView.showError(e.getMessage());
        }
    }

public void filterProductsByCategory() {
    String category = productView.promptCategory();
    try {

        List<Product> productsFiltered = productService.getProductsByCategory(category);
        productView.displayFilteredProducts(productsFiltered);
        logger.log(Level.INFO,"Success to find category={0}",category);
    }catch(InvalidProductException e){
        logger.warning("Failed: Category not found");
        productView.showError(e.getMessage());
    }catch (IllegalArgumentException e){
        logger.warning("Failed: Category is empty");
        productView.showError(e.getMessage());
    }
}

        public void deleteProduct() {
        int id = productView.promptId();
        if(id == -1){
            return;
        }
        try{
        productService.removeProduct(id);
        productView.successRemoved();
    }catch (InvalidProductException e){
            productView.showError(e.getMessage());
        }
        }
}


