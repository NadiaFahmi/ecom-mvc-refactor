package com.ecommerce.service;

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

        if (email.equals("admin@email.com")) {
            if (password.equals("adminPass")) {
                System.out.println("✅ Welcome back, Admin!");
                return new Admin(0, "Jailan(Admin)", email, password);
            } else {
                System.out.println("❌ Invalid admin password.");
                return null;
            }
        }

        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            System.out.println("❌ Email not found. Please sign up first.");
            return null;
        }

        if (customer.getPassword().equals(password)) {
            System.out.println("✅ Welcome back, " + customer.getName() + "!");
            return customer;
        }

        System.out.println("⚠️ Incorrect password.");
        return null;
    }


public boolean resetPassword(Customer customer, String inputEmail, String newPassword, String confirmPassword) {
    return    customerService.resetPassword(customer, inputEmail, newPassword, confirmPassword);

}
    public Customer getCustomerByEmail(String email) {

        return customerService.getCustomerByEmail(email);
    }
}
