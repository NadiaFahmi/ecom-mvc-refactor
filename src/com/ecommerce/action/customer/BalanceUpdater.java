package com.ecommerce.action.customer;

import com.ecommerce.model.entities.Customer;

import java.util.Scanner;

public class BalanceUpdater implements FieldUpdater {

    public boolean update(Customer customer, Scanner scanner) {
        System.out.print("💵 Enter amount to add to your balance: ");
        if (scanner.hasNextDouble()) {
            double amount = scanner.nextDouble();
            scanner.nextLine();
            if (customer.tryAddToBalance(amount)) {
                System.out.printf("✅ Balance updated! New balance: $%.2f%n", customer.getBalance());
                return true;
            } else {
                System.out.println("⚠️ Amount must be greater than zero.");
            }
        } else {
            System.out.println("⚠️ Invalid input. Please enter a number.");
            scanner.nextLine(); // clear buffer
        }
        return false;
    }

}
