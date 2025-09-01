package com.ecommerce.view;

import com.ecommerce.controller.CartController;
import com.ecommerce.controller.OrderController;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.*;
import com.ecommerce.service.OrderService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CustomerDashboard {
    private final CartController cartController;
    private final OrderController orderController;
    private final Customer customer;
    private final ProductService productService;
    private final UpdateService updateService;
    private final Scanner scanner;


    public CustomerDashboard(Customer customer,
                             ProductService productService,
                             CartController cartController,
                             OrderController orderController,
                             UpdateService updateService,
                             Scanner scanner) {
        this.customer = customer;
        this.productService = productService;
        this.cartController = cartController;
        this.orderController = orderController;
        this.updateService = updateService;
        this.scanner = scanner;
    }

    public void launch() {

        cartController.handleLoadCart();
        System.out.printf("👋 Hello, %s! You have %d item(s) in your cart.%n",
                customer.getName(),
                customer.getCart().getCartItems().size());

        boolean running = true;

        while (running) {
            printMenu();

            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> productService.listProducts();
                case "2" -> {
                    System.out.print("🆔 Enter Product ID: ");
                    String productIdInput = scanner.nextLine().trim();

                    try {
                        int productId = Integer.parseInt(productIdInput);
                        System.out.print("Enter quantity: ");
                        int quantity = Integer.parseInt(scanner.nextLine().trim());

                        cartController.handleAddToCart(productId, quantity); // ✅ use controller
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid input. Please enter numeric values.");
                    }
                }
//
                case "3" -> cartController.handlelistCartItems();
                case "4" -> {
                    System.out.print("Enter Product ID to remove from cart: ");
                    try {
                        int productId = Integer.parseInt(scanner.nextLine().trim());
                        cartController.handleRemoveFromCart(productId);
                        cartController.handleSaveCart();
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Invalid product ID. Please enter a number.");
                    }
                }

//
                case "5" -> cartController.handleTotalPrice();
                case "6" -> {
                    try {
                        System.out.print("Enter Product ID: ");
                        String productIdInput = scanner.nextLine().trim();
                        int productId = Integer.parseInt(productIdInput);

                        System.out.print("Enter New Quantity: ");
                        String quantityInput = scanner.nextLine().trim();
                        int newQty = Integer.parseInt(quantityInput);

                        if (newQty < 0) {
                            System.out.println("❌ Quantity cannot be negative.");
                        } else {
                            cartController.handleUpdateQuantity(productId, newQty);
                            System.out.println("✅ Cart item updated.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid input. Please enter numeric values.");
                    } catch (Exception e) {
                        System.out.println("⚠️ An unexpected error occurred while updating the cart.");
                    }

                }

                case "7" -> cartController.handleSaveCart();
                case "8" -> cartController.handleClearCartFile();
                case "9" -> cartController.handleDeleteCartFile(customer);
                case "10" -> orderController.handlePlaceOrder(customer, scanner);
                case "11" -> orderController.handlePrintCustomerOrders(customer);
                case "12" -> {
                    System.out.print("📅 Enter date (YYYY-MM-DD): ");
                    String dateInput = scanner.nextLine().trim();
                    try {
                        LocalDate date = LocalDate.parse(dateInput);
                        orderController.handleFilterCustomerOrdersByDate(customer, date);
                    } catch (DateTimeParseException e) {
                        System.out.println("⚠️ Invalid date format.");
                    }
                }

            case "13" -> updateService.launchUpdateMenu(customer, scanner);

            case "exit" -> {
                System.out.println("👋 Logging out. See you soon, " + customer.getName() + "!");
                running = false;
            }
            default -> System.out.println("⚠️ Invalid choice. Please try again.");

        }
    }
}
    private void printMenu() {
        System.out.println("\n🛒 === Customer Dashboard ===");
        System.out.println("Choose an action below:\n");

        System.out.println("🛍️  Product & Cart Actions");
        System.out.printf("  %-3s %-30s%n", "1", "View Products");
        System.out.printf("  %-3s %-30s%n", "2", "Add to Cart");
        System.out.printf("  %-3s %-30s%n", "3", "View Cart");
        System.out.printf("  %-3s %-30s%n", "4", "Remove from Cart");
        System.out.printf("  %-3s %-30s%n", "5", "Total Price");
        System.out.printf("  %-3s %-30s%n", "6", "Update Item");
        System.out.printf("  %-3s %-30s%n", "7", "Save Cart");
        System.out.printf("  %-3s %-30s%n", "8", "Clear Cart File");
        System.out.printf("  %-3s %-30s%n", "9", "Delete Cart File");

        System.out.println("\n📦  Order Processing");
        System.out.printf("  %-3s %-30s%n", "10", "Place Order");
        System.out.printf("  %-3s %-30s%n", "11", "My Orders");
        System.out.printf("  %-3s %-30s%n", "12", "Filter Orders");

        System.out.println("\n👤  Account");
        System.out.printf("  %-3s %-30s%n", "13", "Update Info");

        System.out.println("\n🔚  Type 'exit' to Logout");
    }


}
