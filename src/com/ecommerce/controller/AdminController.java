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

    public void viewAllUsers() {
        Collection<Customer> customers = adminService.getAllCustomers();
        customerView.showAllCustomers(customers);
    }

    public void filterUsersByNameKeyword(String keyword) {
        List<Customer> customers = adminService.filterUsersByNameKeyword(keyword);
        customerView.displayFilteredUsers(customers,keyword);
    }


    public void ordersByBalanceRange(double min, double max) {
        List<Customer> users = adminService.getUsersByBalanceRange(min, max);
        transactionView.showUsersByBalanceRange(min, max,users);
    }

    public void ordersByDateRange(LocalDate from, LocalDate to) {
        List<Order> orders = adminService.getOrdersByDateRange(from, to);
        transactionView.displayOrdersByDateRange(orders, from, to);
    }

    public void viewAllTransactions() {
        List<Order> orders = adminService.getAllTransactions();
        transactionView.viewAllTransactions(orders);
    }

    public void viewOrdersByUser(String email) {
        List<Order> orders = adminService.getOrdersByUser(email);
        transactionView.viewTransactionsByUser(email, orders);
    }
}
