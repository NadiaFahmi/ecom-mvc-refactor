package com.ecommerce.view;

import com.ecommerce.model.entities.Product;

import java.util.List;

public class ProductView {


    public void displayFilteredProducts(List<Product> products, String category) {
        if (products.isEmpty()) {
            System.out.println("🚫 No products found in category: " + category);
        } else {
            System.out.println("📦 Products in category: " + category);
            for (Product p : products) {
                System.out.println("🔹 ID: " + p.getId() +
                        ", Name: " + p.getName() +
                        ", Price: $" + p.getPrice());
            }
        }
    }

    public void displayAllProducts(List<Product> products) {
        if (products == null || products.isEmpty()) {
            System.out.println("📭 No products available at the moment.");
            return;
        }

        System.out.println("\n🛍️ Available Products:");
        for (Product product : products) {
            System.out.println("🔹 ID: " + product.getId() +
                    ", Name: " + product.getName() +
                    ", Price: $" + product.getPrice() +
                    ", Category: " + product.getCategory());
        }

        if (products == null || products.isEmpty()) {
            System.out.println("📭 No products available at the moment.");
            return;
        }

        System.out.println("\n🛍️ Available Products:");
        for (Product product : products) {
            System.out.println("🔹 ID: " + product.getId() +
                    ", Name: " + product.getName() +
                    ", Price: $" + product.getPrice() +
                    ", Category: " + product.getCategory());
        }
    }
    public void showError(String message){
        System.out.println(message);
    }
    public void showUpdatedProduct(){
        System.out.println("✅ Product updated: ");
    }
}
