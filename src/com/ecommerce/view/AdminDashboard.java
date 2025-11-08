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
                case "2" -> {
                    System.out.print("🔍 Enter keyword: ");
                    String keyword = scanner.nextLine();
                    adminController.filterUsersByNameKeyword(keyword);
                }
                case "3" -> {
                    try {
                        System.out.print("💰 Min balance: ");
                        double min = Double.parseDouble(scanner.nextLine());

                        System.out.print("💰 Max balance: ");
                        double max = Double.parseDouble(scanner.nextLine());

                        adminController.getOrdersByBalanceRange(min, max);

                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Please enter valid numbers for balance.");
                    }
                }
                case "4" -> adminController.getAllTransactions();

                case "5" -> {
                    try {
                        System.out.print("From date (yyyy-MM-dd): ");
                        LocalDate from = LocalDate.parse(scanner.nextLine());

                        System.out.print("To date (yyyy-MM-dd): ");
                        LocalDate to = LocalDate.parse(scanner.nextLine());

                        adminController.getOrdersByDateRange(from, to);

                    } catch (DateTimeException e) {
                        System.out.println("⚠️ Invalid date format. Please use yyyy-MM-dd (e.g., 2025-10-10).");
                    }
                }
                case "6" -> {
                    System.out.print("📧 Enter user email: ");
                    String email = scanner.nextLine();
                    adminController.getOrdersByUser(email);
                }
                case "7" -> {
                    System.out.print("📦 Enter category: ");
                    String category = scanner.nextLine().trim();

                    if (category.isEmpty()) {
                        System.out.println("⚠️ Category cannot be empty. Please try again.");
                        break;
                    }

                    List<Product> filtered = productController.filterProductsByCategory(category);
                    productView.displayFilteredProducts(filtered, category);
                }
                case "8" -> {
                    try {
                        System.out.print("🆕System.out.println(Product name: ");
                        String name = scanner.nextLine();

                        System.out.print("Price: ");
                        double price = Double.parseDouble(scanner.nextLine());

                        System.out.print("Category: ");
                        String category = scanner.nextLine();

                        if (name.isEmpty() || category.isEmpty()) {
                            System.out.println("⚠️ Product name and category cannot be empty.");
                            break;
                        }
                        productController.createProduct(name, price, category);
                        System.out.println("✅ Product '" + name + "' added successfully");
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Invalid price format. Please enter a valid number.");

                    } catch (Exception e) {
                        System.out.println("⚠️ Something went wrong while adding the product. Please try again.");
                    }

                }
                case "9" -> {
                    try {
                        System.out.print("🔄 Product ID to update: ");
                        int id = Integer.parseInt(scanner.nextLine());

                        System.out.print("New name: ");
                        String name = scanner.nextLine();

                        System.out.print("New price: ");
                        double price = Double.parseDouble(scanner.nextLine());

                        System.out.print("New category: ");
                        String category = scanner.nextLine();
                        productController.updateProduct(id, name, price, category);
                    } catch (NumberFormatException e) {

                        System.out.println("⚠️ Invalid number format. Please enter valid numeric values for ID and price.");
                    } catch (Exception e) {
                        System.out.println("⚠️ Something went wrong while updating the product. Please try again.");
                    }
                }
                case "10" -> {
                    System.out.print("❌ Product ID to remove: ");
                    int id = Integer.parseInt(scanner.nextLine());

                    boolean success = productController.deleteProduct(id);
                    if (success) {
                        System.out.println("✅ Product with ID " + id + " successfully removed.");
                    } else {
                        System.out.println("❌ Product ID not found.");
                    }
                }
                case "11" ->
                productController.getProducts();
                default -> System.out.println("⚠️ Invalid choice. Please try again.");
            }
        }
    }
}