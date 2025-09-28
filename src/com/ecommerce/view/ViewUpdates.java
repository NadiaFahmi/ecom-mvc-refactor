package com.ecommerce.view;

import com.ecommerce.controller.CustomerController;
import com.ecommerce.model.entities.Customer;

import java.util.Scanner;

public class ViewUpdates {

    private CustomerController customerController;

    public ViewUpdates(CustomerController customerController) {
        this.customerController = customerController;
    }

    public void launchUpdateMenu(Customer customer, Scanner scanner) {
        if (customer == null) {
            System.out.println("❌ No customer selected.");
            return;
        }

        while (true) {
            System.out.println("""
            \n🔧 Update Menu:
            1. Update Name
            2. Update Email
            3. Update Address
            4. Update Balance
            5. Change Password
            6. Reset Password
            0. Exit
            """);

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("Enter new name: ");
                    String name = scanner.nextLine();
                    customerController.updateName(customer.getId(), name);
                }
                case "2" -> {
                    System.out.print("Enter new email: ");
                    String email = scanner.nextLine();
                    customerController.updateEmail(customer.getId(), email);
                }
                case "3" -> {
                    System.out.print("Enter new address: ");
                    String address = scanner.nextLine();
                    customerController.updateAddress(customer.getId(), address);
                }
                case "4" -> {
                    System.out.print("Enter amount to adjust balance: ");
                    double amount = Double.parseDouble(scanner.nextLine());
                    customerController.updateBalance(customer.getId(), amount);
                }
                case "5" -> {
                    System.out.print("Enter current password: ");
                    String current = scanner.nextLine();
                    System.out.print("Enter new password: ");
                    String newPass = scanner.nextLine();
                    System.out.print("Confirm new password: ");
                    String confirm = scanner.nextLine();
                    customerController.changePassword(customer.getId(), current, newPass, confirm);
                }
                case "6" -> {
                    System.out.print("Enter your email: ");
                    String inputEmail = scanner.nextLine();
                    System.out.print("Enter new password: ");
                    String newPass = scanner.nextLine();
                    System.out.print("Confirm new password: ");
                    String confirm = scanner.nextLine();
                    customerController.resetPassword(customer.getId(), inputEmail, newPass, confirm);
                }
                case "0" -> {
                    System.out.println("👋 Exiting update menu.");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice.");
            }
        }
    }
}
