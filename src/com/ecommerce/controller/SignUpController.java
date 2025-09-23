package com.ecommerce.controller;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.SignUpService;

import java.util.Scanner;

public class SignUpController {
//    private final SignUpService signUpService;
//
//    public SignUpController(SignUpService signUpService) {
//        this.signUpService = signUpService;
//    }
//
//    public Customer handleSignUp(Scanner scanner) {
//        return signUpService.registerNewCustomer(scanner);
//    }
private final SignUpService signUpService;
    private final Scanner scanner = new Scanner(System.in);

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    public Customer handleSignUp() {
        System.out.print("📧 Email: ");
        String email = scanner.nextLine();

        System.out.print("🧑 Name: ");
        String name = scanner.nextLine();

        System.out.print("🔒 Password: ");
        String password = scanner.nextLine();

        System.out.print("💰 Balance: ");
        double balance = Double.parseDouble(scanner.nextLine());

        System.out.print("🏡 Address: ");
        String address = scanner.nextLine();

        try {
            Customer customer = signUpService.registerNewCustomer(name, email, password, balance, address);
            System.out.println("✅ Welcome, " + customer.getName() + "!");
            return customer;
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
            return null;
        }
    }

}
