package com.ecommerce.view;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class CustomerView {
    private Scanner scanner;

    public CustomerView(Scanner scanner) {
        this.scanner = scanner;
    }

    public String promptNewName() {
        System.out.print("📝 Enter your new name: ");
        return scanner.nextLine().trim();
    }
    public String promptNewEmail() {
        System.out.print("📧 Enter your new email: ");
        return scanner.nextLine().trim().toLowerCase();
    }
    public String promptNewAddress() {
        System.out.print("🏠 Enter your new address: ");
        return scanner.nextLine().trim();
    }
    public String promptCurrentPassword() {
        System.out.print("Enter current password: ");
        return scanner.nextLine();
    }

    public String promptNewPassword(){
        System.out.print("Enter new password: ");
        return scanner.nextLine();
    }

    public String promptConfirmedPassword(){
        System.out.print("Confirm new password: ");
        return scanner.nextLine();
    }
    public double promptNewBalance() {
        System.out.print("💰 Enter your new balance: ");
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid balance. Defaulting to 0.");
            return 0;
        }
    }

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

    public void showEmailUpdated() {

        System.out.println("Email updated successfully.");    }

    public void showNameUpdated(){
        System.out.println("✅ Name updated.");
    }
    public void showDeleteCustomer(){
        System.out.println("Customer deleted");
    }

    public void showError(String message) {
        System.out.println(message);
    }
    public void showInvalidBalance(String message) {
        System.out.println("⚠️ Insufficient funds. Cannot set negative balance.");
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
        System.out.println("Balance: " +"$"+ customer.getBalance());
        System.out.println("------");

        if (orders == null || orders.isEmpty()) {
            System.out.println("📭 You haven’t placed any orders yet.");
        } else {
            System.out.println("📦 Orders:");
            for (Order order : orders) {
                System.out.println("🆔 Order ID: " + order.getOrderId());
                System.out.println("🗓️ Date: " + order.getOrderDate());
                System.out.printf("💰 Total: $%.2f%n", order.getOrderTotal());
                System.out.println("📌 Status: " + order.getStatus());
                System.out.println("------");
            }
        }}

public void showSuccess(){
    System.out.println("✅ Password reset successfully.");
}

    public String promptEmail() {
        String email = "";
        while (true) {
            System.out.print("Enter your registered email: ");
            email = scanner.nextLine().trim().toLowerCase();
            if (!email.isEmpty()) {
                return email;
            }
            System.out.println("❌ Email must not be empty. Please try again.");
        }
    }
    public double promptMinBalance() {

        System.out.print("💰 Min balance: ");
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public double promptMaxBalance() {

        System.out.print("💰 Max balance: ");
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    public String promptKeyword() {

        System.out.print("🔍 Enter keyword: ");
        return scanner.nextLine();
    }


}
