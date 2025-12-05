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


    public void updateCustomerEmail(Customer customer, String email) {

        try {
            customerService.updateCustomerEmail(customer, email);
            customerView.showEmailUpdated();
        } catch (InvalidEmailException e) {
            customerView.showError(e.getMessage());
        }
    }

    public void updateName(int customerId, String newName) {
        try {
            Customer customer = customerService.findCustomerById(customerId);
            customerService.updateCustomerName(customer, newName);
            customerView.showNameUpdated();
        } catch (InvalidNameException e) {
            customerView.showError(e.getMessage());
        }
    }

    public void updateAddress(int customerId, String newAddress) {
        try {
            Customer customer = customerService.findCustomerById(customerId);
            customerService.updateCustomerAddress(customer, newAddress);
        } catch (InvalidAddressException e) {
            customerView.showError(e.getMessage());
        }
    }

    public void updateBalance(int customerId, double amount) {
        Customer customer = customerService.findCustomerById(customerId);
        try {
            customerService.updateCustomerBalance(customer, amount);
        } catch (InsufficientBalanceException e) {
            customerView.showInvalidBalance(e.getMessage());
        }
    }


    public void deleteCustomerByEmail(String email) {
        try {
            customerService.deleteCustomer(email);
            customerView.showDeleteCustomer();

        } catch (InvalidEmailException e) {
            customerView.showError(e.getMessage());
        }
    }

    public void showLoggedInCustomerOrders(Customer customer) {
        List<Order> orders = new ArrayList<>();

        customerService.getLoggedInCustomerWithOrders(customer,orders);
        customerView.displayCustomerWithOrders(customer,orders);

    }
    public void resetPassword(int customerId, String inputEmail, String newPassword, String confirmPassword) {
        Customer customer = customerService.findCustomerById(customerId);

        try {
            customerService.resetPassword(customer, inputEmail, newPassword, confirmPassword);
            customerView.showSuccess();
        } catch (CustomerNotFoundException e) {
            customerView.showError("❌ " + e.getMessage());

        } catch(InvalidEmailException e){
            customerView.showError(e.getMessage());
        }
        catch (InvalidPasswordException e) {
            customerView.showError("⚠️ " + e.getMessage());
        } catch (Exception e) {
            customerView.showError("⚠️ Unexpected error: " + e.getMessage());
        }

    }

}
