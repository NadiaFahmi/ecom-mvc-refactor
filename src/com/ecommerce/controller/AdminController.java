package com.ecommerce.controller;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.service.AdminService;
import com.ecommerce.view.CustomerView;
import com.ecommerce.view.TransactionView;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class AdminController {

    private final AdminService adminService;
    private final TransactionView transactionView;
    private final CustomerView customerView;

    public AdminController(AdminService adminService,TransactionView transactionView, CustomerView customerView) {

        this.adminService = adminService;
        this.transactionView = transactionView;
        this.customerView = customerView;
    }

    public void handleViewAllUsers() {
        Collection<Customer> customers = adminService.getAllCustomers();
        customerView.showAllCustomers(customers);
    }

    public void handleFilterUsersByName(String keyword) {

        adminService.filterUsersByNameKeyword(keyword);
    }

    public void handleViewUsersByBalanceRange(double min, double max) {
        List<Customer> users = adminService.getUsersByBalanceRange(min, max);
        transactionView.showUsersByBalanceRange(min, max,users);
    }


    public void showOrdersByDateRange(LocalDate from, LocalDate to) {
        List<Order> orders = adminService.getOrdersByDateRange(from, to);
        transactionView.displayOrdersByDateRange(orders, from, to);
    }

    public void handleViewAllTransactions() {
        List<Order> orders = adminService.getAllTransactions();
        transactionView.viewAllTransactions(orders);
    }

    public void handleViewTransactionsByUser(String email) {
        List<Order> orders = adminService.getTransactionsByUser(email);
        transactionView.viewTransactionsByUser(email, orders);
    }
}
