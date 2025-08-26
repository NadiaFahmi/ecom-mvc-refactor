package com.ecommerce.action.admin;

import com.ecommerce.service.AdminService;

import java.util.Scanner;

public class FilterProductsByCategoryAction implements AdminAction{

    public void execute(AdminService adminService, Scanner scanner) {

        String adminEmail = adminService.getAdmin().getEmail();
        System.out.println("🔐 Action triggered by admin: " + adminEmail);

        System.out.print("🗂️ Enter category to filter: ");
        String category = scanner.nextLine();
        adminService.filterProductsByCategory(category);
    }

}
