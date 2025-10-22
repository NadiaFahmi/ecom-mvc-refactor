package com.ecommerce.view;

import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;

import java.util.Collection;
import java.util.List;

public class CustomerView {
    public void showAllCustomers(Collection<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            System.out.println("🚫 No customers found.");
            return;
        }

        System.out.println("👥 Total Users: " + customers.size());
        for (Customer customer : customers) {
            showCustomerSummary(customer);
        }
    }

    public void showCustomerSummary(Customer customer) {
        System.out.println("🆔 " + customer.getId() + " | " + customer.getName() + " | 💰 $" + customer.getBalance());
    }
    public void displayFilteredUsers(List<Customer> customers, String keyword) {
        System.out.println("🔍 Searching for users with name containing: \"" + keyword + "\"");

        if (customers.isEmpty()) {
            System.out.println("❌ No users found matching the keyword.");
        } else {
            System.out.println("✅ Filtered Users:");
            for (Customer user : customers) {
                showCustomerSummary(user);
            }
        }

    }
    public void displayCustomerWithOrders(Customer customer, List<Order> orders) {
        if (customer == null) {
            System.out.println("❌ No customer found.");
            return;
        }

        System.out.println("👤 Customer Info:");
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Name: " + customer.getName());

        System.out.println("Address: " + customer.getAddress());
        System.out.println("Balance: " + customer.getBalance());
        System.out.println("------");

        if (orders == null || orders.isEmpty()) {
            System.out.println("📭 You haven’t placed any orders yet.");
        } else {
            System.out.println("📦 Orders:");
            for (Order order : orders) {
                System.out.println("🆔 Order ID: " + order.getOrderId());
                System.out.println("🗓️ Date: " + order.getOrderDate());
                System.out.println("🛒 Items:");
                for (CartItem item : order.getCartItems()) {
                    String name = item.getProduct().getName();
                    double price = item.getProduct().getPrice() * item.getQuantity();
                    System.out.printf(" - %s x%d = $%.2f%n", name, item.getQuantity(), price);
                }
                System.out.printf("💰 Total: $%.2f%n", order.getOrder_total());
                System.out.println("📌 Status: " + order.getStatus());
                System.out.println("------");
            }
        }
    }

}
