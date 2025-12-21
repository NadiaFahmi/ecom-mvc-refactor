package com.ecommerce.view;

import com.ecommerce.model.entities.User;

import java.util.Scanner;

public class LoginView {

    private final Scanner scanner;

    public LoginView(Scanner scanner) {
        this.scanner = scanner;
    }

    public String promptEmail() {
        String email = "";
        while (true) {
            System.out.print("📧 Enter your registered email: ");
            email = scanner.nextLine().trim().toLowerCase();
            if (!email.isEmpty()) {
                return email;
            }
            System.out.println("❌ Email must not be empty. Please try again.");
        }
    }

    public String promptPassword() {
        String password = "";
        while (true) {
            System.out.print("🔒 Enter your password: ");
            password = scanner.nextLine().trim();
            if (!password.isEmpty()) {
                return password;
            }
            System.out.println("❌ Password must not be empty. Please try again.");
        }
    }

    public void showWelcome(User user) {
        System.out.println("✅ Welcome back, " + user.getName() + "!");
        System.out.println("👤 Welcome to Nadia’s Shop!");
    }

    public void showExitMessage() {
        System.out.println("👋 Exiting login.");
        return ;
    }

    public void showInvalidChoice() {
        System.out.println("❌ Invalid choice.");
    }

    public String promptRetryChoice() {
        System.out.print("Forgot your password? Type 'yes' to reset, 'no' to retry, or 'exit' to cancel: ");
        return scanner.nextLine().trim().toLowerCase();
    }

    public String promptNewPassword() {
        System.out.print("🔒 Enter new password: ");
        return scanner.nextLine().trim();
    }

    public String promptConfirmPassword() {
        System.out.print("🔁 Confirm new password: ");
        return scanner.nextLine().trim();
    }

    public void showPasswordResetResult(boolean success) {

        if (!success) {

            System.out.println("⚠️ Password reset failed. Please try again.");
        } else {
            System.out.println("Password reset successfully ");
        }
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showFailed(String message) {
        System.out.println(message);
    }
}
