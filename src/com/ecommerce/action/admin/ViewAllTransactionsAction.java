package com.ecommerce.action.admin;

import com.ecommerce.service.AdminService;

import java.util.Scanner;

public class ViewAllTransactionsAction implements AdminAction{

    @Override
    public void execute(AdminService adminService, Scanner scanner) {
        String adminEmail = adminService.getAdmin().getEmail();
        System.out.println("🔐 Action triggered by admin: " + adminEmail);

        adminService.viewAllTransactions();
    }

}
