package com.ecommerce.controller;

import com.ecommerce.model.entities.Admin;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.AdminService;

import java.time.LocalDate;
import java.util.List;

public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    public void handleViewAllUsers() {
        adminService.viewAllUsers();
    }

    public void handleFilterUsersByName(String keyword) {


        adminService.filterUsersByNameKeyword(keyword);
    }

    public void handleUsersByBalanceRange(double min, double max) {
        List<Customer> users = adminService.getUsersByBalanceRange(min, max);
        System.out.println("💰 Users with balance between $" + min + " and $" + max + ": " + users.size());
        for (Customer user : users) {
            System.out.println("🆔 " + user.getId() + " | " + user.getName() + " | 💰 $" + user.getBalance());
        }
    }

    public void handleFilterProductsByCategory(String category) {
        adminService.filterProductsByCategory(category);
    }

    public void handleFilterOrdersByDate(LocalDate from, LocalDate to) {
        adminService.filterOrdersByDateRange(from, to);
    }

    public void handleViewAllTransactions() {
        adminService.viewAllTransactions();
    }

    public void handleViewTransactionsByUser(String email) {
        adminService.viewTransactionsByUser(email);
    }

    public void showAdminProfile() {
        Admin admin = adminService.getAdmin();
        System.out.println("👑 Admin: " + admin.getName() + " | 📧 " + admin.getEmail());
    }
}
