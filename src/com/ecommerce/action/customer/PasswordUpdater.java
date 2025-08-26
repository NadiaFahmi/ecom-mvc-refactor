package com.ecommerce.action.customer;

import com.ecommerce.model.entities.Customer;

import java.util.Scanner;

public class PasswordUpdater implements FieldUpdater{

    public boolean update(Customer customer, Scanner scanner) {
        System.out.print("🔒 Enter your current password: ");
        String current = scanner.nextLine().trim();

        if (!isCurrentPasswordCorrect(customer, current)) {
            System.out.println("❌ Current password is incorrect.");
            return false;
        }

        System.out.print("🔑 Enter new password (min 6 characters): ");
        String newPass = scanner.nextLine().trim();

        if (!isValidLength(newPass)) {
            System.out.println("⚠️ New password must be at least 6 characters.");
            return false;
        }

        System.out.print("🔁 Confirm new password: ");
        String confirmPass = scanner.nextLine().trim();

        if (!newPass.equals(confirmPass)) {
            System.out.println("⚠️ Password confirmation does not match.");
            return false;
        }

        customer.setPassword(newPass);
        System.out.println("✅ Password updated successfully.");
        return true;
    }

    public boolean resetPasswordWithoutCurrent(Customer customer, Scanner scanner) {
        System.out.print("🔑 Enter new password (min 6 characters): ");
        String newPass = scanner.nextLine().trim();

        if (!isValidLength(newPass)) {
            System.out.println("⚠️ New password must be at least 6 characters.");
            return false;
        }

        System.out.print("🔁 Confirm new password: ");
        String confirmPass = scanner.nextLine().trim();

        if (!newPass.equals(confirmPass)) {
            System.out.println("⚠️ Password confirmation does not match.");
            return false;
        }

        customer.setPassword(newPass);
        System.out.println("✅ Password reset successfully.");
        return true;
    }

    private boolean isValidLength(String password) {
        return password != null && password.length() >= 6;
    }

    private boolean isCurrentPasswordCorrect(Customer customer, String currentPass) {
        return customer.getPassword().equals(currentPass);
    }
}
