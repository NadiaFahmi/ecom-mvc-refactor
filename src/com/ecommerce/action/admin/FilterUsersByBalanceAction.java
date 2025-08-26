package com.ecommerce.action.admin;

import com.ecommerce.service.AdminService;

import java.util.Scanner;

public class FilterUsersByBalanceAction  implements AdminAction{


    @Override
    public void execute(AdminService adminService, Scanner scanner) {
        String adminEmail = adminService.getAdmin().getEmail();
        System.out.println("🔐 Action triggered by admin: " + adminEmail);

        try {
            System.out.print("💳 Enter minimum balance: ");
            double min = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("💳 Enter maximum balance: ");
            double max = Double.parseDouble(scanner.nextLine().trim());

            adminService.getUsersByBalanceRange(min, max);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter numbers like 50.00 or 200.00");
        }
    }

}
