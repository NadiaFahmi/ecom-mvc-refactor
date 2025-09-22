package com.ecommerce.controller;

import com.ecommerce.model.entities.Product;
import com.ecommerce.service.CategoryFilter;
import com.ecommerce.service.ProductFilter;
import com.ecommerce.service.ProductService;

import java.util.List;
import java.util.Scanner;

public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public void handleListProducts() {
        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("Product List:");
        for (Product product : products) {
            System.out.println("ID: " + product.getId() +
                    ", Name: " + product.getName() +
                    ", Price: " + product.getPrice() +
                    ", Category: " + product.getCategory());
        }
    }
    public void handleAddProduct() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // consume leftover newline

        System.out.print("Enter product category: ");
        String category = scanner.nextLine();

        productService.addProduct(name, price, category);
    }
    public void handleUpdateProduct() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter product ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new product name: ");
        String newName = scanner.nextLine();

        System.out.print("Enter new product price: ");
        double newPrice = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter new product category: ");
        String newCategory = scanner.nextLine();

        if (newPrice < 0) {
            System.out.println("❌ Price cannot be negative.");
            return;
        }

        productService.updateProduct(id, newName, newPrice, newCategory);
    }
    public void handleRemoveProduct() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter product ID to remove: ");
        int id = scanner.nextInt();

        productService.removeProduct(id);
    }
    public void handleFindProductById() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter product ID to search: ");
        int id = scanner.nextInt();

        Product product = productService.getProductById(id);

        if (product != null) {
            System.out.println("✅ Product found:");
            System.out.println("ID: " + product.getId());
            System.out.println("Name: " + product.getName());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Category: " + product.getCategory());
        } else {
            System.out.println("❌ Product ID not found.");
        }
    }
    public void handleFilterByCategory() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter category to filter by: ");
        String category = scanner.nextLine().trim();

        ProductFilter filter = new CategoryFilter();
        List<Product> filteredProducts = productService.getFilteredProducts(filter, category);

        if (filteredProducts.isEmpty()) {
            System.out.println("No products found in category: " + category);
        } else {
            System.out.println("Products in category '" + category + "':");
            for (Product product : filteredProducts) {
                System.out.println("ID: " + product.getId() +
                        ", Name: " + product.getName() +
                        ", Price: " + product.getPrice() +
                        ", Category: " + product.getCategory());
            }
        }
    }
}
