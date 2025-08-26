package com.ecommerce.action.admin;

import com.ecommerce.service.AdminService;
import com.ecommerce.service.ProductService;

import java.util.Scanner;

public class AddProductAction implements AdminAction {

    public void execute(AdminService adminService, Scanner scanner) {

        String adminEmail = adminService.getAdmin().getEmail();
        System.out.println("🔐 Action triggered by admin: " + adminEmail);

        ProductService pm = adminService.getProductService();

        System.out.println("\n🆕 Add New Product");

        System.out.print("📦 Product Name: ");
        String name = scanner.nextLine().trim();

        double price = -1;
        while (price < 0) {
            System.out.print("💰 Price (e.g. 99.99): ");
            try {
                price = Double.parseDouble(scanner.nextLine().trim());
                if (price < 0) System.out.println("⚠️ Price must be positive.");
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Try again.");
            }
        }

        System.out.print("🗂️ Category: ");
        String category = scanner.nextLine().trim();

        pm.addProduct(name, price, category);
    }

    }
