package com.ecommerce.controller;
import com.ecommerce.model.entities.Customer;

import com.ecommerce.service.CustomerService;

import java.util.Scanner;
public class CustomerController {

    private  CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    public void registerCustomer(Customer customer) {

        customerService.registerCustomer(customer);
    }
    public void load() {
        customerService.loadCustomers();
    }

    public void listAllCustomers() {
        for (Customer c : customerService.listAllCustomers()) {
            System.out.println(c);
        }
    }

    public void getCustomerByEmail(String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer != null) {
            System.out.println(customer);
        } else {
            System.out.println("❌ No customer found with email: " + email);
        }
    }

    public void findCustomerById(int id) {
            Customer customer = customerService.findCustomerById(id);
            System.out.println(customer);

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

    public void delete(int customerId) {
        customerService.deleteCustomer(customerId);
    }


}
