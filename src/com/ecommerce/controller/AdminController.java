package com.ecommerce.controller;

import com.ecommerce.exception.EmptyDataException;
import com.ecommerce.exception.InvalidBalanceException;
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

    public void filterUsersByName() {
        String keyword = customerView.promptKeyword();
        try {
            List<Customer> customers = adminService.filterUsersByNameKeyword(keyword);
            customerView.displayFilteredUsers(customers, keyword);
        } catch (IllegalArgumentException e) {
            customerView.showError(e.getMessage());
        }
    }
    public void getUsersByBalanceRange() {
        double min = customerView.promptMinBalance();
        double max = customerView.promptMaxBalance();
        try {
            List<Customer> users = adminService.getUsersByBalanceRange(min, max);
            transactionView.showUsersByBalanceRange(min, max, users);
        }catch (InvalidBalanceException e){
            customerView.showError(e.getMessage());
        }
    }

    public void getOrdersByDateRange(
//            LocalDate from, LocalDate to
    ) {

        LocalDate from = transactionView.showDateFromPrompt();
        LocalDate to = transactionView.showDateToPrompt();
        try {
            List<Order> orders = adminService.getOrdersByDateRange(from, to);
            transactionView.displayOrdersByDateRange(orders, from, to);
        }catch (IllegalArgumentException e){
            transactionView.showError(e.getMessage());
        }
    }

    public void getAllTransactions() {
        List<Order> orders = adminService.getAllTransactions();
        transactionView.viewAllTransactions(orders);
    }

    public void getOrdersByEmail() {
        String email = transactionView.promptEmail();

        try {
            List<Order> orders = adminService.getOrdersByUser(email);
            transactionView.viewTransactionsByUser(email, orders);
        }catch (IllegalArgumentException e){
            transactionView.showError(e.getMessage());
        }
    }
}
