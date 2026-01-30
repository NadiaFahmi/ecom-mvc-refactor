package com.ecommerce.view;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class TransactionView {
    private Scanner scanner;

    public TransactionView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void viewTransactionsByUser(String email, List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("❌ No transactions found for user: " + email);
            return;
        }

        for (Order order : orders) {
            printOrderSummary(order);
        }
    }

    public void viewAllTransactions(List<Order> orders) {
        System.out.println("📦 All Transactions Overview: " + orders.size());

        for (Order order : orders) {
            printOrderSummary(order);
        }
    }

    private void printOrderSummary(Order order) {
        System.out.println("🆔 Order ID: " + order.getOrderId());
        System.out.println("🗓️ Date: " + order.getOrderDate());
        System.out.println("💰 total Price: $" + order.getOrderTotal());
        System.out.println("📌 Status: " + order.getStatus());
        System.out.println("------");
    }
    public void showUsersByBalanceRange(double min, double max, List<Customer> users) {
        System.out.println("💰 Users with balance between $" + min + " and $" + max + ": " + users.size());

        if (users.isEmpty()) {
            System.out.println("❌ No users found in this balance range.");
            return;
        }

        for (Customer user : users) {
            System.out.println("👤 " + user.getName() + " | Email: " + user.getEmail() + " | Balance: $" + user.getBalance());
        }
    }
    public void displayOrdersByDateRange(List<Order> orders, LocalDate from, LocalDate to) {
        System.out.println("📅 Orders placed from " + from + " to " + to);

        if (orders.isEmpty()) {
            System.out.println("🚫 No orders found in the specified date range.");
            return;
        }

        for (Order order : orders) {
            System.out.println("🆔 Order ID: " + order.getOrderId() +
                    ", Customer: " + order.getCustomerId() +
                    ", Total: $" + order.getOrderTotal() +
                    ", Status: " + order.getStatus());
        }
    }
    public LocalDate showDateFromPrompt() {
        LocalDate date = null;
        while (date == null) {

            System.out.print("From date (yyyy-MM-dd): ");
            String dateInput = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(dateInput);

            }
            catch (DateTimeParseException e) {
                System.out.println("⚠️ Invalid date format.");
                return null;
            }
        }
        return date;
    }
    public LocalDate showDateToPrompt() {
        LocalDate date = null;
        while (date == null) {

            System.out.print("To date (yyyy-MM-dd): ");
            String dateInput = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(dateInput);

            }
            catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
                return null;
            }

        }
        return date;
    }
    public void showError(String message){
        System.out.println(message);

    }
    public String promptEmail(){
        System.out.print("📧 Enter user email: ");

        return scanner.nextLine();
    }
}
