package com.ecommerce.service;

import com.ecommerce.exception.InvalidEmailException;
import com.ecommerce.exception.InvalidPasswordException;
import com.ecommerce.model.entities.Admin;
import com.ecommerce.model.entities.Customer;


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
            logger.info("Admin login attempt detected");
            if (password.equals("adminPass")) {
                logger.info( "Admin logged in successful");
                return new Admin(0, "Jailan(Admin)", email, password);
            } else {
                logger.warning("Failed admin login attempt - incorrect password");
                throw new InvalidPasswordException(" Invalid admin password.");
            }
        }

        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            logger.warning("Login failed - email not found");
            throw new InvalidEmailException(" Email not found. Please sign up first.");
        }

        if (customer.getPassword().equals(password)) {
            logger.info("User login successful");
            return customer;
        }
        logger.warning("Login failed - incorrect password");
        throw new InvalidPasswordException("Incorrect password.");

    }

    public void resetPassword(String inputEmail, String newPassword, String confirmPassword) {
        customerService.resetPassword(inputEmail, newPassword, confirmPassword);
    }


}
