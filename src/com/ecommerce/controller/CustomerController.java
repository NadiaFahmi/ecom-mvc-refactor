package com.ecommerce.controller;

import com.ecommerce.model.entities.Customer;

import com.ecommerce.service.CustomerService;


public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void load() {
        customerService.loadCustomers();
    }


    public void updateEmail(int customerId, String newEmail) {
        Customer customer = customerService.findCustomerById(customerId);
        customerService.updateCustomerEmail(customer, newEmail);
    }

    public void updateName(int customerId, String newName) {
        Customer customer = customerService.findCustomerById(customerId);
        customerService.updateCustomerName(customer, newName);
    }

    public void updateAddress(int customerId, String newAddress) {
        Customer customer = customerService.findCustomerById(customerId);
        customerService.updateCustomerAddress(customer, newAddress);
    }

    public void updateBalance(int customerId, double amount) {
        Customer customer = customerService.findCustomerById(customerId);
        customerService.updateCustomerBalance(customer, amount);
    }

    public void changePassword(int customerId, String currentPassword, String newPassword, String confirmPassword) {
        Customer customer = customerService.findCustomerById(customerId);
        customerService.updatePassword(customer, currentPassword, newPassword, confirmPassword);
    }

    public void resetPassword(int customerId, String inputEmail, String newPassword, String confirmPassword) {
        Customer customer = customerService.findCustomerById(customerId);
        customerService.resetPassword(customer, inputEmail, newPassword, confirmPassword);
    }

    public boolean handleDeleteCustomer(String email) {
        return customerService.deleteCustomer(email);
    }

}
