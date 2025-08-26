package com.ecommerce.action.admin;

import com.ecommerce.service.AdminService;
import com.ecommerce.service.ProductService;


import java.util.Scanner;

public class UpdateProductAction implements AdminAction{

    public void execute(AdminService adminService, Scanner scanner) {

        String adminEmail = adminService.getAdmin().getEmail();
        System.out.println("🔐 Action triggered by admin: " + adminEmail);

        ProductService pm = adminService.getProductService();

        System.out.println("\n✏️ Update Product");

        System.out.print("Enter Product ID: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
            return;
        }

        if (pm.getProductById(id) == null) {
            System.out.println("🚫 Product with ID " + id + " not found.");
            return;
        }


        System.out.print("New Name: ");
        String newName = scanner.nextLine().trim();

        double newPrice = -1;
        while (newPrice < 0) {
            System.out.print("New Price: ");
            try {
                newPrice = Double.parseDouble(scanner.nextLine().trim());
                if (newPrice < 0) System.out.println("⚠️ Price must be positive.");
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid price format.");
            }
        }

        System.out.print("New Category: ");
        String newCategory = scanner.nextLine().trim();

        pm.updateProduct(id, newName, newPrice, newCategory);
    }

    }

