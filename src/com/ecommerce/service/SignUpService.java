package com.ecommerce.service;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Customer;

public class SignUpService {

private final CustomerService customerService;

    public SignUpService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public boolean isEmailValid(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");

    }

    public boolean isEmailTaken(String email) {
        return customerService.getCustomerByEmail(email.toLowerCase()) != null;
    }

    public boolean isPasswordValid(String password) {
        return password != null && password.length() >= 6;
    }

    public boolean isBalanceValid(double balance) {
        return balance >= 0;
    }

    public boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public boolean isAddressValid(String address) {
        return address != null && !address.trim().isEmpty();
    }

    public Customer registerNewCustomer(String name, String email, String password, double balance, String address) {

        if (!isNameValid(name)) {
            throw new InvalidNameException("❌ Invalid name. Please enter a non-empty name.");

        }

        if (!isEmailValid(email)) {
            throw new InvalidEmailFormatException("❌ Invalid email format. Please enter a valid email address.");
        }

        if (isEmailTaken(email)) {
            throw new InvalidEmailException("⚠ Email already registered. Try logging in or use a different email.");
        }

        if (!isPasswordValid(password)) {
            throw new InvalidPasswordException("❌ Password must be at least 6 characters long.");
        }

        if (!isBalanceValid(balance)) {
            throw  new InvalidBalanceException("❌ Invalid balance. Must be zero or positive.");

        }

        if (!isAddressValid(address)) {
            throw new InvalidAddressException("❌ Invalid address. Please enter a non-empty address.");

        }

        Customer customer = new Customer(name, email.toLowerCase(), password, balance, address);
        customerService.registerCustomer(customer);
        SessionContext.setLoggedInEmail(customer.getEmail());
        return customer;
    }

}
