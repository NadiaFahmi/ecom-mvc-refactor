package com.ecommerce.service;

import com.ecommerce.model.entities.Admin;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.User;

import java.util.Scanner;

public class LoginService {

    private final CustomerService customerService;

    public LoginService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public User login(Scanner scanner) {
        System.out.print("📧 Enter your registered email: ");
        String email = scanner.nextLine().trim().toLowerCase();

        System.out.print("🔒 Enter your password: ");
        String password = scanner.nextLine().trim();


        if (email.equals("admin@email.com")) {
            if (password.equals("adminPass")) {
                System.out.println("✅ Welcome back, Admin!");
                return new Admin(0, "Jailan(Admin)", email, password);
            } else {
                System.out.println("❌ Invalid admin password.");
                return null;
            }
        }

        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            System.out.println("❌ Email not found. Please sign up first.");
            return null;
        }

        if (customer.getPassword().equals(password)) {
            System.out.println("✅ Welcome back, " + customer.getName() + "!");
            return customer;
        }

        System.out.println("⚠️ Incorrect password.");
        return handlePasswordRetry(scanner, customer);
    }

    private User handlePasswordRetry(Scanner scanner, Customer customer) {
        while (true) {
            System.out.print("Forgot your password? Type 'yes' to reset, 'no' to retry, or 'exit' to cancel: ");
            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "yes" -> {
                    forgotPassword(scanner);
                    return null;
                }
                case "no" -> {
                    System.out.print("🔒 Enter your password: ");
                    String password = scanner.nextLine().trim();
                    if (customer.getPassword().equals(password)) {
                        System.out.println("✅ Welcome back, " + customer.getName() + "!");
                        return customer;
                    } else {
                        System.out.println("❌ Still incorrect.");
                    }
                }
                case "exit" -> {
                    System.out.println("👋 Exiting login.");
                    return null;
                }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }
       public void forgotPassword(Scanner scanner) {
        System.out.print("📧 Re-enter your registered email: ");
        String email = scanner.nextLine().trim().toLowerCase();

        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            System.out.println("❌ Email not found. Please make sure you registered first.");
            return;
        }

            customerService.registerCustomer(customer);
        }

}
