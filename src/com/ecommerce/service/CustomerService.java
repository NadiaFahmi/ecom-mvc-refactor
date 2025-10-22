package com.ecommerce.service;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.repository.CustomerRepository;

import java.util.*;
import java.util.NoSuchElementException;

public class CustomerService {

    private static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";

    private final CustomerRepository repository;
    private final OrderService orderService;
    public CustomerService(CustomerRepository repository, OrderService orderService) {
        this.repository = repository;
        this.orderService = orderService;

    }
    public void registerCustomer(Customer customer) {
        repository.addCustomer(customer);
        repository.saveAll();
    }

    public Customer getCustomerByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be empty");
        }
        return repository.getCustomerByEmail(email);
    }

    public Collection<Customer> listAllCustomers() {
        return repository.getAllCustomers();
    }


    public void loadCustomers() {
        repository.load();
    }

    public Customer findCustomerById(int id) {

        return repository.getCustomerById(id);
    }

    //
    public boolean updateCustomerEmail(Customer customer, String newEmail) {
        if (customer == null) {
            System.out.println("❌ Customer not found.");
            return false;
        }

        if (newEmail == null || newEmail.trim().isEmpty()) {
            System.out.println("⚠️ Email cannot be empty.");
            return false;
        }

        newEmail = newEmail.trim();
        if (!isValidEmail(newEmail)) {
            System.out.println("⚠️ Invalid email format.");
            return false;
        }

        customer.setEmail(newEmail);
        repository.updateCustomer(customer);
        repository.saveAll();
        System.out.println("✅ Email updated successfully.");
        return true;
    }


    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        return email.matches(EMAIL_REGEX);
    }


    public boolean updatePassword(Customer customer, String currentPassword, String newPassword, String confirmPassword) {
        if (customer == null) {
            System.out.println("❌ Customer not found.");
            return false;
        }

        if (currentPassword == null || !isCurrentPasswordCorrect(customer, currentPassword.trim())) {
            System.out.println("❌ Current password is incorrect.");
            return false;
        }

        if (newPassword == null || !isValidLength(newPassword.trim())) {
            System.out.println("⚠️ New password must be at least 6 characters.");
            return false;
        }

        if (!newPassword.trim().equals(confirmPassword == null ? "" : confirmPassword.trim())) {
            System.out.println("⚠️ Password confirmation does not match.");
            return false;
        }

        customer.setPassword(newPassword.trim());
        repository.updateCustomer(customer);
        repository.saveAll();
        System.out.println("✅ Password updated successfully.");
        return true;
    }


    public boolean resetPassword(Customer customer, String inputEmail, String newPassword, String confirmPassword) {
        if (customer == null) {
            System.out.println("❌ Customer not found.");
            return false;
        }

        if (!isEmailMatching(customer, inputEmail)) {
            System.out.println("❌ Email does not match our records.");
            return false;
        }

        if (newPassword == null || !isValidLength(newPassword.trim())) {
            System.out.println("⚠️ New password must be at least 6 characters.");
            return false;
        }

        if (!isPasswordConfirmed(newPassword, confirmPassword)) {
            System.out.println("⚠️ Password confirmation does not match.");
            return false;
        }

        customer.setPassword(newPassword.trim());
        repository.updateCustomer(customer);
        repository.saveAll();
        System.out.println("✅ Password reset successfully.");
        return true;
    }

    private boolean isEmailMatching(Customer customer, String inputEmail) {
        return inputEmail != null && customer.getEmail().equalsIgnoreCase(inputEmail.trim());
    }

    private boolean isPasswordConfirmed(String newPassword, String confirmPassword) {
        return newPassword != null && newPassword.trim().equals(confirmPassword == null ? "" : confirmPassword.trim());
    }

    private boolean isCurrentPasswordCorrect(Customer customer, String inputPassword) {
//        return customer.getPassword().equals(inputPassword);
        return inputPassword != null && inputPassword.equals(customer.getPassword());

    }

    private boolean isValidLength(String password) {
        return password != null && password.length() >= 6;
    }

    public boolean updateCustomerName(Customer customer, String newName) {
        if (customer == null) {
            System.out.println("❌ Customer not found.");
            return false;
        }

        if (newName == null || newName.trim().isBlank()) {
            System.out.println("⚠️ Name can't be empty.");
            return false;
        }

        newName = newName.trim();
        customer.setName(newName);
        repository.updateCustomer(customer);
        repository.saveAll();
        System.out.println("✅ Name updated.");
        return true;
    }


    public boolean updateCustomerAddress(Customer customer, String newAddress) {
        if (customer == null) {
            System.out.println("❌ Customer not found.");
            return false;
        }

        if (newAddress == null || newAddress.trim().isEmpty()) {
            System.out.println("⚠️ Address cannot be empty.");
            return false;
        }

        newAddress = newAddress.trim();
        customer.setAddress(newAddress);
        repository.updateCustomer(customer);
        repository.saveAll();
        System.out.println("✅ Address updated successfully.");
        return true;
    }

    public boolean updateCustomerBalance(Customer customer, double amount) {
        if (customer == null) {
            System.out.println("❌ Customer not found.");
            return false;
        }

        double currentBalance = customer.getBalance();
        double newBalance = currentBalance + amount;

        if (newBalance < 0) {
            System.out.println("⚠️ Insufficient funds. Cannot set negative balance.");
            return false;
        }

        customer.setBalance(newBalance);
        repository.updateCustomer(customer);
        repository.saveAll();
        System.out.println("✅ Balance updated successfully. New balance: " + newBalance);
        return true;
    }


    public boolean deleteCustomer(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("⚠️ email cannot be empty.");
            return false;
        }
        try {
            repository.deleteByEmail(email);
            repository.saveAll();
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("❌ " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("⚠️ Unexpected error: " + e.getMessage());
            return false;
        }
    }
    public Customer getLoggedInCustomerWithOrders(List<Order> outputOrders) {
        Customer customer = orderService.getLoggedInCustomer();
        if (customer == null) return null;

        List<Order> orders = orderService.getOrdersForCustomer(customer.getEmail());
        outputOrders.clear();
        outputOrders.addAll(orders);

        return customer;
    }

}

