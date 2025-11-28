package com.ecommerce.service;

import com.ecommerce.exception.InvalidEmailException;
import com.ecommerce.exception.InvalidPasswordException;
import com.ecommerce.model.entities.Admin;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.User;

import java.util.logging.Logger;


public class LoginService {

    private static Logger logger = Logger.getLogger(LoginService.class.getName());

    private final CustomerService customerService;

    public LoginService(CustomerService customerService) {
        this.customerService = customerService;
    }


    public User login(String email, String password) {
        email = email.trim().toLowerCase();
        password = password.trim();



        if (email.equals("admin@gmail.com")) {
            if (password.equals("adminPass")) {
                logger.info("Login attempt for : " + email);
                LoggedInUser.setLoggedInEmail(email);
                return new Admin(0, "Jailan(Admin)", email, password);
            } else {
                logger.warning("Invalid admin password.");
                throw new InvalidPasswordException("❌ Invalid admin password.");
            }
        }

        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            logger.warning("Email not found " + customer);
            throw new InvalidEmailException("❌ Email not found. Please sign up first.");}

        if (customer.getPassword().equals(password)) {
            logger.info("Customer login successfully");
            LoggedInUser.setLoggedInEmail(email);
            return customer;
        }
        logger.warning("Incorrect password." + password);
        throw new InvalidPasswordException("Incorrect password.");

    }


public boolean resetPassword(Customer customer, String inputEmail, String newPassword, String confirmPassword) {
    return    customerService.resetPassword(customer, inputEmail, newPassword, confirmPassword);

}
    public Customer getCustomerByEmail(String email) {

        return customerService.getCustomerByEmail(email);
    }
}
