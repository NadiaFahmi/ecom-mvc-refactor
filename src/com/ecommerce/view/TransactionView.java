package com.ecommerce.view;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;

import java.time.LocalDate;
import java.util.List;

public class TransactionView {
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
        System.out.println("🆔 Order ID: " + order.getOrderId() +
                ", Customer: " + order.getCustomer().getName() +
                ", Total: $" + order.getOrder_total() +
                ", Status: " + order.getStatus());
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
        System.out.println("📅 Admin Orders from " + from + " to " + to);

        if (orders.isEmpty()) {
            System.out.println("🚫 No orders found in the specified date range.");
            return;
        }

        for (Order order : orders) {
            System.out.println("🆔 Order ID: " + order.getOrderId() +
                    ", Customer: " + order.getCustomer().getName() +
                    ", Total: $" + order.getOrder_total() +
                    ", Status: " + order.getStatus());
        }
    }
}
