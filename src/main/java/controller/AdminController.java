package main.java.controller;


import main.java.exception.CustomerNotFoundException;
import main.java.exception.InvalidBalanceException;
import main.java.model.entities.Customer;
import main.java.model.entities.Order;
import main.java.service.AdminService;
import main.java.view.CustomerView;
import main.java.view.TransactionView;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class AdminController {
    private Logger logger=Logger.getLogger(AdminController.class.getName());
    private final AdminService adminService;
    private final TransactionView transactionView;
    private final CustomerView customerView;

    public AdminController(AdminService adminService,TransactionView transactionView, CustomerView customerView) {

        this.adminService = adminService;
        this.transactionView = transactionView;
        this.customerView = customerView;
    }

    public void getAllUsers() {
        Collection<Customer> customers = adminService.getAllCustomers();
        customerView.showAllCustomers(customers);
    }

    public void filterUsersByName() {
        String keyword = customerView.promptKeyword();
        try {
            List<Customer> customers = adminService.filterUsersByNameKeyword(keyword);
            customerView.displayFilteredUsers(customers, keyword);
        } catch (IllegalArgumentException e) {
            logger.warning("Failed. Empty keyword");
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
            logger.warning("Invalid balance");
            customerView.showError(e.getMessage());
        }
    }

    public void getOrdersByDateRange() {

        LocalDate from = transactionView.showDateFromPrompt();
        LocalDate to = transactionView.showDateToPrompt();
        try {
            List<Order> orders = adminService.getOrdersByDateRange(from, to);
            transactionView.displayOrdersByDateRange(orders, from, to);
        }catch (IllegalArgumentException e){
            logger.warning("Invalid date");
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
            logger.info("Success load orders");
            transactionView.viewTransactionsByUser(email, orders);
        }catch (CustomerNotFoundException e){
            logger.warning("Encountered exception: customer not found");
            transactionView.showError(e.getMessage());
        }catch (IllegalArgumentException e){
            logger.warning("Encountered exception: Email is empty");
            transactionView.showError(e.getMessage());
        }
    }
}
