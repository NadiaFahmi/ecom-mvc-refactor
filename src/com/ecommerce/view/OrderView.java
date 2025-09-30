package com.ecommerce.view;

import java.util.Scanner;

public class OrderView {

    private final Scanner scanner;

    public OrderView(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean  confirmAddFunds(double total, double balance) {

        System.out.printf("❌ Insufficient balance. Cart total: %.2f%n", total, balance);
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

}
