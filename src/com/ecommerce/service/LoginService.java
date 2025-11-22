package com.ecommerce.service;

import com.ecommerce.exception.InvalidEmailException;
import com.ecommerce.exception.InvalidPasswordException;
import com.ecommerce.model.entities.Admin;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.User;


public class LoginService {

    private final CustomerService customerService;

    public LoginService(CustomerService customerService) {
        this.customerService = customerService;
    }


    public User login(String email, String password) {
        email = email.trim().toLowerCase();
        password = password.trim();



        if (email.equals("admin@gmail.com")) {
            if (password.equals("adminPass")) {
                SessionContext.setLoggedInEmail(email);
                return new Admin(0, "Jailan(Admin)", email, password);
            } else {
                throw new InvalidPasswordException("❌ Invalid admin password.");
            }
        }

        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            throw new InvalidEmailException("❌ Email not found. Please sign up first.");}

        if (customer.getPassword().equals(password)) {
            SessionContext.setLoggedInEmail(email);
            return customer;
        }
        throw new InvalidPasswordException("Incorrect password.");

    }


public boolean resetPassword(Customer customer, String inputEmail, String newPassword, String confirmPassword) {
    return    customerService.resetPassword(customer, inputEmail, newPassword, confirmPassword);

}
    public Customer getCustomerByEmail(String email) {

        return customerService.getCustomerByEmail(email);
    }
}
