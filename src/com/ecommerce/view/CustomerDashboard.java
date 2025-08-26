package com.ecommerce.view;

import com.ecommerce.service.CartService;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.*;
import com.ecommerce.model.entities.Product;
import com.ecommerce.service.OrderServic;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CustomerDashboard {
    private final Customer customer;
    private final ProductService productService;
    private final CartService cartService;
    private final OrderServic orderServic;
    private final Scanner scanner;
   private final UpdateService updateService ;

    public CustomerDashboard(Customer customer,
                             ProductService productService,
                             CartService cartService,
                             OrderServic orderServic,
                             UpdateService updateService,
                             Scanner scanner) {
        this.customer = customer;
        this.productService = productService;
        this.cartService = cartService;
        this.orderServic = orderServic;
        this.scanner = scanner;
        this.updateService =updateService;
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
                case "1" -> productService.listProducts();
                case "2" -> {
                    System.out.print("🆔 Enter Product ID: ");
                    String productIdInput = scanner.nextLine().trim();

                    try {
                        int productId = Integer.parseInt(productIdInput); // ✅ Only works if input is a valid number
                        Product productToAdd = productService.getProductById(productId);

                        if (productToAdd != null) {
                            System.out.print("Enter quantity: ");
                            String quantityInput = scanner.nextLine().trim();

                            try {
                                int quantity = Integer.parseInt(quantityInput);
                                cartService.addToCart(productToAdd, quantity);
                                System.out.println("✅ Added to cart.");
                            } catch (NumberFormatException e) {
                                System.out.println("❌ Invalid quantity. Please enter a number.");
                            }

                        } else {
                            System.out.println("❌ Product not found.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid product ID. Please enter a numeric ID.");
                    }
                }

                case "3" -> cartService.listCartItems();
                case "4" -> {
                    System.out.print("Enter Product ID to remove from cart: ");
                    try {
                        int productId = Integer.parseInt(scanner.nextLine().trim());
                        cartService.removeFromCart(productId);
                        cartService.saveCartToFile(); // Optional, nice touch
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Invalid product ID. Please enter a number.");
                    }
                }

                case "5" -> System.out.printf("🧾 Total: $%.2f%n", cartService.getTotalPrice());
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
                            cartService.updateCartItemQuantity(productId, newQty);
                            System.out.println("✅ Cart item updated.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid input. Please enter numeric values.");
                    } catch (Exception e) {
                        System.out.println("⚠️ An unexpected error occurred while updating the cart.");
                    }

                }

                case "7" -> cartService.saveCartToFile();
                case "8" -> orderServic.placeOrder(customer, scanner);
                case "9" -> orderServic.printCustomerOrders(customer);
                case "10" -> {
                    System.out.print("📅 Enter date (YYYY-MM-DD): ");
                    String dateInput = scanner.nextLine().trim();
                    try {
                        LocalDate date = LocalDate.parse(dateInput);
                        orderServic.filterCustomerOrdersByDate(customer, date);
                    } catch (DateTimeParseException e) {
                        System.out.println("⚠️ Invalid date format.");
                    }
                }

                case "11" -> updateService.launchUpdateMenu(customer, scanner);
                case "exit" -> {
                    System.out.println("👋 Logging out. See you soon, " + customer.getName() + "!");
                    running = false;
                }
                default -> System.out.println("⚠️ Invalid choice. Please try again.");
            }
        }
    }


}
