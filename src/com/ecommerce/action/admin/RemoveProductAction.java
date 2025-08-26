package com.ecommerce.action.admin;
import com.ecommerce.service.AdminService;
import com.ecommerce.service.ProductService;

import java.util.Scanner;


public class RemoveProductAction implements AdminAction{
    public void execute(AdminService adminService, Scanner scanner) {
        String adminEmail = adminService.getAdmin().getEmail();
        System.out.println("🔐 Action triggered by admin: " + adminEmail);

        ProductService pm = adminService.getProductService();

        System.out.print("🔍 Enter Product ID to remove: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            pm.removeProduct(id);
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number.");
        }
    }

}
