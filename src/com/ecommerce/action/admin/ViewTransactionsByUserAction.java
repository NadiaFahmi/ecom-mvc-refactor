package com.ecommerce.action.admin;

import com.ecommerce.service.AdminService;

import java.util.Scanner;

public class ViewTransactionsByUserAction implements AdminAction{

    @Override
    public void execute(AdminService adminService, Scanner scanner) {

        String adminEmail = adminService.getAdmin().getEmail();
        System.out.println("🔐 Action triggered by admin: " + adminEmail);

        System.out.print("📧 Enter user's email to view their transactions: ");
        String email = scanner.nextLine().trim().toLowerCase();
        adminService.viewTransactionsByUser(email);
    }

}
