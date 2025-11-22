package com.ecommerce.view;

import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;

import java.util.List;
import java.util.Scanner;

public class OrderView {

    private final Scanner scanner;

    public OrderView(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean  confirmAddFunds(double total, double balance) {

        System.out.printf("❌ Insufficient balance. Cart total: %.2f, Your balance: %.2f%n", total, balance);
        System.out.print("Would you like to add funds to complete the purchase? (Y/N): ");
        String response = scanner.nextLine().trim();
        return response.equalsIgnoreCase("Y");

    }
    public double promptAmountToAdd() {
        System.out.print("Enter amount to add: ");
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("⚠ Invalid amount entered.");
            return -1;
        }
    }

    public void showOrderSuccess() {
        System.out.println("✅ Order placed successfully!");
    }
    public void showCartEmpty() {
        System.out.println("🛒 Your cart is empty. Add items before placing an order.");
    }

    public void showInsufficientAfterAdd() {
        System.out.println("🚫 Still insufficient. Please adjust your cart or add more funds.");
    }
    public void showOrderCancelled() {
        System.out.println("🕳 Order cancelled. Feel free to come back anytime.");
    }

    public void showNewBalance(double balance) {
        System.out.printf("💳 New balance: $%.2f%n", balance);
    }
    public void showOrderFailure() {
        System.out.println("❌ Order could not be placed.");
    }
    public void displayOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("📭 You haven’t placed any orders yet.");
        } else {
            for (Order order : orders) {
                System.out.println("🆔 Order ID: " + order.getOrderId());
                System.out.println("🗓️ Date: " + order.getOrderDate());
                System.out.println("🛒 Items:");
                for (CartItem item : order.getCartItems()) {
                    String name = item.getProduct().getName();
                    double price = item.getProduct().getPrice() * item.getQuantity();
                    System.out.printf(" - %s x%d = $%.2f%n", name, item.getQuantity(), price);
                }
                System.out.printf("💰 Total: $%.2f%n", order.getOrderTotal());
                System.out.println("📌 Status: " + order.getStatus());
                System.out.println("------");
            }
        }
    }
    public void renderOrderDetails(Order order, Customer customer) {
        System.out.println("\n🎉 THANK YOU FOR YOUR PURCHASE, " + customer.getName() + "!");
        System.out.println("🆔 Order ID: " + order.getOrderId());
        System.out.println("📅 Date: " + order.getOrderDate());
        System.out.println("📦 Status: " + order.getStatus());
        System.out.println("🛒 Items:");
        System.out.println("–––––––––––––––––––––––");

        double total = 0.0;
        for (CartItem item : order.getCartItems()) {
            String name = item.getProduct().getName();
            int quantity = item.getQuantity();
            double price = item.getProduct().getPrice() * quantity;
            total += price;
            System.out.printf(" - %s x%d = $%.2f%n", name, quantity, price);
        }

        System.out.printf("💰 TOTAL: $%.2f%n", total);
        System.out.printf("💳 Remaining Balance: $%.2f%n", customer.getBalance());
        System.out.println("✨ Your order is confirmed and being processed!\n");
    }

    public void showErrorMessage(String message){
        System.out.println(message);
    }
}
