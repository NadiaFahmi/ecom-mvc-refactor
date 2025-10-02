package com.ecommerce.view;

import com.ecommerce.model.entities.User;

import java.util.Scanner;

public class LoginView {

    private final Scanner scanner;

    public LoginView(Scanner scanner) {
        this.scanner = scanner;
    }

    public String promptEmail() {
        System.out.print("📧 Enter your registered email: ");
        return scanner.nextLine().trim().toLowerCase();
    }

    public String promptPassword() {
        System.out.print("🔒 Enter your password: ");
        return scanner.nextLine().trim();
    }

    public void showWelcome(User user) {
        System.out.println("✅ Welcome back, " + user.getName() + "!");
    }

    public void showLoginFailed() {
        System.out.println("⚠️ Login failed.");
    }

    public void showIncorrectPassword() {
        System.out.println("❌ Still incorrect.");
    }

    public void showExitMessage() {
        System.out.println("👋 Exiting login.");
    }

    public void showInvalidChoice() {
        System.out.println("❌ Invalid choice.");
    }

    public String promptRetryChoice() {
        System.out.print("Forgot your password? Type 'yes' to reset, 'no' to retry, or 'exit' to cancel: ");
        return scanner.nextLine().trim().toLowerCase();
    }
}
