package com.ecommerce.view;

import com.ecommerce.model.entities.Product;

import java.util.List;
import java.util.Scanner;

public class ProductView {

private Scanner scanner;

    public ProductView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void displayFilteredProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("🚫 No products found" );
        } else {
            System.out.println("📦 Products with category: ");
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

        }

    }
    public String promptProductCategory() {
        String category;
        do {
            System.out.println("Enter product category: ");
            category = scanner.nextLine();
            if (category.isEmpty()) {
                System.out.println("Category must not be empty.");
            }

        } while (category.isEmpty());
        return category;

    }
    public String promptNewCategory() {
        String category;
        do {
            System.out.println("Enter product category: ");
            category = scanner.nextLine();
            if (category.isEmpty()) {
                System.out.println("Category must not be empty.");
            }

        } while (category.isEmpty());
        return category;

    }
    public String promptCategory() {
        System.out.print("📦 Enter category: ");
        String category = scanner.nextLine().trim();

        if (category.isEmpty()) {
            System.out.println("⚠️ Category cannot be empty. Please try again.");

        }
        return category;
    }

    public String promptProductName() {
        String name;
        do {
            System.out.println("Enter product name or (type 'exit' to cancel and return :");
            name = scanner.nextLine();
            if(name.equalsIgnoreCase("exit")){
                return null;
            }
            if (name.isEmpty()) {
                System.out.println("Product name must not be empty.");
            }
        }while(name.isEmpty()) ;

        return name;
    }
    public String promptNewName() {
        String name;
        do {
            System.out.println("Enter new name or (type 'exit' to cancel and return :");
            name = scanner.nextLine();
            if(name.equalsIgnoreCase("exit")){
                return null;
            }
            if (name.isEmpty()) {
                System.out.println("Product name must not be empty.");
            }
        }while(name.isEmpty()) ;

        return name;
    }
    public double promptPrice() {
        while (true) {
            System.out.println("Enter product price:");
            String input = scanner.nextLine();
            try {

                return Double.parseDouble(input);
            } catch (NumberFormatException e) {

                System.out.println("Invalid input. Please enter a valid numerical price.");
            }
        }}
    public int promptId() {
        System.out.println("Enter product Id or type 'exit' to cancel:");
        while (true) {
//            System.out.println("Enter product Id:");
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("exit")){
                return -1;
            }
            try {

                return Integer.parseInt(input);
            } catch (NumberFormatException e) {

                System.out.println("Invalid input.");
            }
        }}
    public double promptNewPrice() {
        while (true) {
            System.out.println("Enter new price:");
            String input = scanner.nextLine();
            try {

                return Double.parseDouble(input);
            } catch (NumberFormatException e) {

                System.out.println("Invalid input. Please enter a valid numerical price.");
            }
        }}

        public void showError(String message){
        System.out.println(message);
    }
    public void showUpdatedProduct(){
        System.out.println("✅ Product updated: ");
    }

public void successRemoved(){
    System.out.println("✅ Product successfully removed.");
}
}
