package com.ecommerce.view;

import com.ecommerce.controller.CartController;
import com.ecommerce.controller.CustomerController;
import com.ecommerce.controller.OrderController;
import com.ecommerce.controller.ProductController;
import com.ecommerce.model.entities.Customer;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;



public class CustomerDashboard {
    private  Customer customer;
    private CartController cartController;
    private ProductController productController;
    private CustomerController customerController;
    private  OrderController orderController;
    private ViewUpdates viewUpdates;
    private final Scanner scanner;



    public CustomerDashboard(Customer customer,
                             ProductController productController,
                             CartController cartController,
                             OrderController orderController,
                             CustomerController customerController,
                             ViewUpdates viewUpdates,
                             Scanner scanner) {
        this.customer = customer;
        this.cartController = cartController;
        this.productController = productController;
        this.orderController = orderController;
        this.customerController = customerController;
        this.viewUpdates =viewUpdates;
        this.scanner = scanner;

    }

    public void launch() {
        System.out.printf("👋 Hello, %s! You have %d item(s) in your cart.%n",
                customer.getName(),
                customer.getCart().getCartItems().size());

        boolean running = true;

        while (running) {
            System.out.println("\n🛒 Customer Dashboard — Choose an action:");
            System.out.println("🛍️ --- Product & Cart Actions ---");
            System.out.println("1 - View Products");
            System.out.println("2 - Add Product to Cart");
            System.out.println("3 - View Cart");
            System.out.println("4 - Remove Item from Cart");
            System.out.println("5 - Check Total Price");
            System.out.println("6 - Update Cart Item");
            System.out.println("7 - Save Cart");
            System.out.println("📦 --- Order Processing ---");
            System.out.println("8 - Place Order");
            System.out.println("9 - View My Orders");
            System.out.println("10 - Filter My Orders By Date");
            System.out.println("👤 --- Account ---");
            System.out.println("11 - Update My Account Info");
            System.out.println("🔚 exit - Logout");

            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> productController.handleListProducts();
                case "2" -> {
                    System.out.print("🆔 Enter Product ID: ");
                    String productIdInput = scanner.nextLine().trim();

                    try {
                        int productId = Integer.parseInt(productIdInput);
                        System.out.print("Enter quantity: ");
                        int quantity = Integer.parseInt(scanner.nextLine().trim());

                        cartController.handleAddToCart(productId, quantity);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid input. Please enter numeric values.");
                    }
                }
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
                case "8" -> orderController.handlePlaceOrder(customer, scanner);
                case "9" -> orderController.printCustomerOrders(customer);



                case "10" -> {
                    System.out.print("📅 Enter date (YYYY-MM-DD): ");
                    String dateInput = scanner.nextLine().trim();
                    try {
                        LocalDate date = LocalDate.parse(dateInput);
                        orderController.filterCustomerOrdersByDate(customer, date);
                    } catch (DateTimeParseException e) {
                        System.out.println("⚠️ Invalid date format.");
                    }
                }

                case "11" -> viewUpdates.launchUpdateMenu(customer, scanner);
                case "exit" -> {
                    System.out.println("👋 Logging out. See you soon, " + customer.getName() + "!");
                    running = false;
                }
                default -> System.out.println("⚠️ Invalid choice. Please try again.");
            }
        }
    }

}
