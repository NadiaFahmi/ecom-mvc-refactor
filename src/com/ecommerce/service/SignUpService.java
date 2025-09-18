package com.ecommerce.service;

import com.ecommerce.model.entities.Customer;

import java.util.Scanner;
public class SignUpService {
    private final CustomerService customerService;

    public SignUpService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Customer registerNewCustomer(Scanner scanner) {
        String email;
        while (true) {
            System.out.print("📧 Enter your email (or type 'exit' to cancel): ");
            email = scanner.nextLine().trim().toLowerCase();
            if (email.equals("exit")) return null;

            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
                System.out.println("❌ Invalid email format. Please try again.");
                continue;
            }

            if (customerService.getCustomerByEmail(email) != null) {
                System.out.println("⚠️ Email already registered. Try a different one.");
                continue;
            }

            break;
        }

        String name;
        while (true) {
            System.out.print("🧑 Enter your name: ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.println("❌ Name cannot be empty. Please try again.");
        }

        String password;
        while (true) {
            System.out.print("🔒 Create a password (min 6 characters): ");
            password = scanner.nextLine().trim();
            if (password.length() >= 6) break;
            System.out.println("🔐 Password too short. Please try again.");
        }

        double balance = 0.0;
        while (true) {
            System.out.print("💰 Enter your starting balance: ");
            try {
                balance = Double.parseDouble(scanner.nextLine().trim());
                if (balance < 0) {
                    System.out.println("❌ Balance cannot be negative.");
                } else break;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Try again.");
            }
        }

        String address;
        while (true) {
            System.out.print("🏡 Enter your address: ");
            address = scanner.nextLine().trim();
            if (!address.isEmpty()) break;
            System.out.println("❌ Address cannot be empty. Please try again.");
        }

        Customer customer = new Customer(name, email, password, balance, address);
        customerService.registerCustomer(customer);


        System.out.println("✅ Registration complete! Welcome, " + name + ".");
        return customer;
    }
}
