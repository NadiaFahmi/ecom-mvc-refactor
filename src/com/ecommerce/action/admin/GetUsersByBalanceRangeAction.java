package com.ecommerce.action.admin;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.AdminService;

import java.util.List;
import java.util.Scanner;

public class GetUsersByBalanceRangeAction implements AdminAction{

    public void execute(AdminService adminService, Scanner scanner) {

        try {
        System.out.print("💳 Enter minimum balance: ");
        double min = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("💳 Enter maximum balance: ");
        double max = Double.parseDouble(scanner.nextLine().trim());

        List<Customer> matchingUsers = adminService.getUsersByBalanceRange(min, max);

        if (matchingUsers.isEmpty()) {
            System.out.println("🚫 No users found in that balance range.");
        } else {
            System.out.println("💰 Users with balance between $" + min + " and $" + max + ":");
            for (Customer customer : matchingUsers) {
                System.out.println("🆔 " + customer.getId() +
                        " | " + customer.getName() +
                        " | 📧 " + customer.getEmail() +
                        " | 💰 $" + customer.getBalance());
            }
        }

    } catch (NumberFormatException e) {
        System.out.println("⚠️ Invalid input. Please enter valid numbers for balances.");
    }
}

}
