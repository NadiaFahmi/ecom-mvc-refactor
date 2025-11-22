package com.ecommerce.controller;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Customer;

import com.ecommerce.model.entities.Order;
import com.ecommerce.service.CustomerService;
import com.ecommerce.view.CustomerView;

import java.util.ArrayList;
import java.util.List;


public class CustomerController {

    private CustomerService customerService;
    private CustomerView customerView;

    public CustomerController(CustomerService customerService
            , CustomerView customerView
    ) {
        this.customerService = customerService;
        this.customerView = customerView;
    }

    public void loadCustomers() {
        customerService.loadCustomers();
    }


    public boolean updateCustomerEmail(Customer customer, String email)  {

        try {
            boolean success = customerService.updateCustomerEmail(customer, email);
            if (success) {
                customerView.showEmailUpdated();
            }
            return success;
        } catch (InvalidEmailException e) {
            customerView.showError(e.getMessage());
            return false;
        }
    }

    public void updateName(int customerId, String newName) {
        try {
            Customer customer = customerService.findCustomerById(customerId);
            customerService.updateCustomerName(customer, newName);
        }catch (InvalidNameException e){
            customerView.showError(e.getMessage());
        }
    }

    public void updateAddress(int customerId, String newAddress) {
        try {
            Customer customer = customerService.findCustomerById(customerId);
            customerService.updateCustomerAddress(customer, newAddress);
        }catch (InvalidAddressException e){
            customerView.showError(e.getMessage());
        }
    }

    public void updateBalance(int customerId, double amount) {
        Customer customer = customerService.findCustomerById(customerId);
        try {
            customerService.updateCustomerBalance(customer, amount);
        }catch (InsufficientBalanceException e){
            customerView.showInvalidBalance(e.getMessage());
        }
    }

    public void changePassword(int customerId, String currentPassword, String newPassword, String confirmPassword) {
        try {
            Customer customer = customerService.findCustomerById(customerId);
            customerService.updatePassword(customer, currentPassword, newPassword, confirmPassword);
            customerView.showPasswordUpdated();
        }catch (InvalidPasswordException e){
            customerView.showError(e.getMessage());
        }
    }


    public boolean deleteCustomerByEmail(String email) {
        try {

             customerService.deleteCustomer(email);
             customerView.showDeleteCustomer();
             return true;
        }catch (InvalidEmailException e){
            customerView.showError(e.getMessage());
        }
        return false;
    }

    public void showLoggedInCustomerOrders() {
        List<Order> orders = new ArrayList<>();
        Customer customer = customerService.getLoggedInCustomerWithOrders(orders);

        customerView.displayCustomerWithOrders(customer, orders);
    }
}
