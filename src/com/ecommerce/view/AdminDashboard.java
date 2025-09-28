package com.ecommerce.view;

import com.ecommerce.controller.AdminController;
import com.ecommerce.controller.ProductController;
import com.ecommerce.model.entities.Order;

import java.time.LocalDate;
import java.util.*;

public class AdminDashboard {

    private final AdminController adminController;
    private final ProductController productController;
    private final Scanner scanner;

    public AdminDashboard(AdminController adminController,
                          ProductController productController,
                          Scanner scanner) {
        this.adminController = adminController;
        this.productController = productController;
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
                case "1" -> adminController.handleViewAllUsers();
                case "2" -> {
                    System.out.print("🔍 Enter keyword: ");
                    String keyword = scanner.nextLine();
                    adminController.handleFilterUsersByName(keyword);
                }
                case "3" -> {
                    System.out.print("💰 Min balance: ");
                    double min = Double.parseDouble(scanner.nextLine());
                    System.out.print("💰 Max balance: ");
                    double max = Double.parseDouble(scanner.nextLine());
                    adminController.handleUsersByBalanceRange(min, max);
                }
                case "4" -> adminController.handleViewAllTransactions();

                case "5" -> {
                    System.out.print("📅 From date (yyyy-MM-dd): ");
                    LocalDate from = LocalDate.parse(scanner.nextLine());
                    System.out.print("📅 To date (yyyy-MM-dd): ");
                    LocalDate to = LocalDate.parse(scanner.nextLine());
                    adminController.handleFilterOrdersByDate(from, to);
                }
                case "6" -> {
                    System.out.print("📧 Enter user email: ");
                    String email = scanner.nextLine();
                    adminController.handleViewTransactionsByUser(email);
                }
                case "7" -> {
                    System.out.print("📦 Enter category: ");
                    String category = scanner.nextLine();
                    adminController.handleFilterProductsByCategory(category);
                }
                case "8" -> {
                    System.out.print("🆕 Product name: ");
                    String name = scanner.nextLine();
                    System.out.print("💰 Price: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    System.out.print("📂 Category: ");
                    String category = scanner.nextLine();
                    productController.handleAddProduct(name, price, category);

                }
                case "9" -> {
                    System.out.print("🔄 Product ID to update: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("🆕 New name: ");
                    String name = scanner.nextLine();
                    System.out.print("💰 New price: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    System.out.print("📂 New category: ");
                    String category = scanner.nextLine();
                    productController.handleUpdateProduct(id,name,price,category);
                }
                case "10" -> {
                    System.out.print("❌ Product ID to remove: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    productController.handleRemoveProduct(id);

                }
                case "11" ->
                productController.handleListProducts();
                default -> System.out.println("⚠️ Invalid choice. Please try again.");
            }
        }
    }
}