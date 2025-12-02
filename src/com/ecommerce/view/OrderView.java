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

    public void showOrderSuccess(Order order) {
        System.out.println("✅ Order placed successfully!");
        System.out.println("-----------------------------");
        System.out.println("🆔 Order ID: " + order.getOrderId());
        System.out.println("👤 Customer ID: " + order.getCustomerId());
        System.out.println("💰 Total: " + order.getOrderTotal());
        System.out.println("📌 Status: " + order.getStatus());
        System.out.println("🗓️ Date: " + order.getOrderDate());
        System.out.println("-----------------------------");
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

    public void displayOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("📭 You haven’t placed any orders yet.");
        } else {
            for (Order order : orders) {
                System.out.println("🆔 Order ID: " + order.getOrderId());
                System.out.println("🗓️ Date: " + order.getOrderDate());
                System.out.println(" Order Total: " + order.getOrderTotal());
                System.out.println("📌 Status: " + order.getStatus());
                System.out.println("------");
            }
        }
    }

    public void showErrorMessage(String message){
        System.out.println(message);
    }
}
