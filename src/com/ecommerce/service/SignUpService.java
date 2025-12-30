package com.ecommerce.service;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Customer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUpService {

    private Logger logger = Logger.getLogger(SignUpService.class.getName());

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
            logger.warning("Name validation failed: Input empty");
            throw new InvalidNameException("❌ Invalid name. Please enter a non-empty name.");

        }

        if (!isEmailValid(email)) {
            logger.severe("Email validation failed: Invalid format");
            throw new InvalidEmailFormatException("❌ Invalid email format. Please enter a valid email address.");
        }

        if (isEmailTaken(email)) {
            logger.warning("Email validation failed: Email already registered.");
            throw new InvalidEmailException("Email already registered. Try logging in or use a different email.");
        }

        if (!isPasswordValid(password)) {
            logger.severe("Password validation failed: Invalid format");
            throw new InvalidPasswordException("❌ Password must be at least 6 characters long.");
        }

        if (!isBalanceValid(balance)) {
            logger.warning("Balance validation failed: Invalid input");
            throw  new InvalidBalanceException();

        }

        if (!isAddressValid(address)) {
            logger.warning("Address validation failed: Input empty");
            throw new InvalidAddressException("❌ Invalid address. Please enter a non-empty address.");

        }

        Customer customer = customerService.registerCustomer(name, email, password, balance, address);
       logger.log(Level.INFO, "registerCustomer() invoked for name={0}", name);
        return customer;
    }

}
