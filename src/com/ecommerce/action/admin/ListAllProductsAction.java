package com.ecommerce.action.admin;

import com.ecommerce.service.AdminService;
import com.ecommerce.service.ProductService;

import java.util.Scanner;

public class ListAllProductsAction implements AdminAction{

    public void execute(AdminService adminService, Scanner scanner) {

        String adminEmail = adminService.getAdmin().getEmail();
        System.out.println("🔐 Action triggered by admin: " + adminEmail);


        ProductService pm = adminService.getProductService();  // ✅ Use shared manager
        System.out.println("\n📋 Listing All Products:");
        pm.listProducts();  // Your method already handles display
    }

    }

