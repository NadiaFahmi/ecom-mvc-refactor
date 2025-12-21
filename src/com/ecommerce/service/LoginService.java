package com.ecommerce.service;

import com.ecommerce.exception.InvalidEmailException;
import com.ecommerce.exception.InvalidPasswordException;
import com.ecommerce.model.entities.Admin;
import com.ecommerce.model.entities.Customer;

import java.util.logging.Level;

import com.ecommerce.model.entities.User;


import java.util.logging.Logger;


public class LoginService {

    private Logger logger = Logger.getLogger(LoginService.class.getName());

    private final CustomerService customerService;

    public LoginService(CustomerService customerService) {
        this.customerService = customerService;
    }


    public User login(String email, String password) {
        email = email.trim().toLowerCase();
        password = password.trim();

        if (email.equals("admin@gmail.com")) {
            logger.info("Admin login attempt detected for email: " + email);
            if (password.equals("adminPass")) {
                logger.log(Level.INFO, "Admin password validated for email:{0} ", email);
                return new Admin(0, "Jailan(Admin)", email, password);
            } else {
                logger.warning("Incorrect admin password.");
                throw new InvalidPasswordException("❌ Invalid admin password.");
            }
        }

        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            logger.log(Level.WARNING, "Email not found: {0} ", email);
            throw new InvalidEmailException("❌ Email not found. Please sign up first.");
        }

        if (customer.getPassword().equals(password)) {
            logger.log(Level.INFO, "Login success - correct password for email={0} ", email);
            return customer;
        }
        logger.log(Level.WARNING, "Login failed - incorrect password for email={0} ", email);
        throw new InvalidPasswordException("Incorrect password.");

    }

    public void resetPassword(String inputEmail, String newPassword, String confirmPassword) {
        customerService.resetPassword(inputEmail, newPassword, confirmPassword);
    }

    public Customer getCustomerByEmail(String email) {

        return customerService.getCustomerByEmail(email);
    }
}
