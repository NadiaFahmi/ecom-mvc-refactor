package com.ecommerce.view;

import com.ecommerce.controller.AdminController;
import com.ecommerce.controller.ProductController;
import com.ecommerce.model.entities.Product;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

public class AdminDashboard {

    private final AdminController adminController;
    private final ProductController productController;
    private final ProductView productView;
    private final Scanner scanner;

    public AdminDashboard(AdminController adminController,
                          ProductController productController,ProductView productView,
                          Scanner scanner) {
        this.adminController = adminController;
        this.productController = productController;
        this.productView = productView;
        this.scanner = scanner;
    }

    public void launch() {
        System.out.println("👋 Welcome");

        while (true) {
            System.out.println("\n👑 Admin Dashboard — Choose an action:");
            System.out.println("👥 User Management:");
            System.out.println("1 - View All Users");
            System.out.println("2 - Search Users by Keyword");
            System.out.println("3 - Filter Users by Balance");

            System.out.println("📦 Order Management:");
            System.out.println("4 - View All Transactions");
            System.out.println("5 - Filter Orders by Date");
            System.out.println("6 - View Transactions by Email");

            System.out.println("🛍️ Product Management:");
            System.out.println("7 - Filter Products by Category");
            System.out.println("8 - Add New Product");
            System.out.println("9 - Update Product");
            System.out.println("10 - Remove Product");
            System.out.println("11 - List All Products");

            System.out.println("🚪 Other:");
            System.out.println("0 - Logout and Exit Dashboard");
            System.out.print("\nYour choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "0", "exit" -> {
                    System.out.println("\n👋 Logging out of Admin Dashboard. Have a productive day!");
                    return;
                }
                case "1" -> adminController.viewAllUsers();

                case "2" -> adminController.filterUsersByName();

                case "3" -> adminController.getUsersByBalanceRange();

                case "4" -> adminController.getAllTransactions();

                case "5" -> adminController.getOrdersByDateRange();

                case "6" ->adminController.getOrdersByEmail();

                case "7" -> productController.filterProductsByCategory();

                case "8" -> productController.createProduct();

                case "9" -> productController.updateProduct();

                case "10" -> productController.deleteProduct();

                case "11" ->
                productController.getProducts();
                default -> System.out.println("⚠️ Invalid choice. Please try again.");
            }
        }
    }
}