package com.ecommerce.service;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.repository.CustomerRepository;

import java.util.*;

public class CustomerService {

    private static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";

    private final CustomerRepository repository;
    private final OrderService orderService;
    public CustomerService(CustomerRepository repository, OrderService orderService) {
        this.repository = repository;
        this.orderService = orderService;

    }

    public Customer registerCustomer(String name, String email, String password,
                                     double balance, String address) {
        return repository.createCustomer(name, email, password, balance, address);
    }

    public Customer getCustomerByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidEmailException("Email must not be empty");
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

        if (newEmail == null || newEmail.trim().isEmpty()) {
            throw new InvalidEmailException("Email cannot be empty.");
        }
        newEmail = newEmail.trim();
        if (!isValidEmail(newEmail)) {
            throw new InvalidEmailException("Invalid email format.");
        }

        customer.setEmail(newEmail);
        repository.updateCustomer(customer);
        repository.saveAll();
        return true;
    }


    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        return email.matches(EMAIL_REGEX);
    }


    public boolean updatePassword(Customer customer, String currentPassword, String newPassword, String confirmPassword) {
        if (customer == null) {
            throw new CustomerNotFoundException("❌ Customer not found.");
        }

        if (currentPassword == null || !isCurrentPasswordCorrect(customer, currentPassword.trim())) {
            throw new InvalidPasswordException("❌ Current password is incorrect.");
        }

        if (newPassword == null || !isValidLength(newPassword.trim())) {
            throw new InvalidPasswordException("⚠️ New password must be at least 6 characters.");
        }

        if (!newPassword.trim().equals(confirmPassword == null ? "" : confirmPassword.trim())) {
            throw new InvalidPasswordException("⚠️ Password confirmation does not match.");
        }

        customer.setPassword(newPassword.trim());
        repository.updateCustomer(customer);
        repository.saveAll();
        return true;
    }


    public boolean resetPassword(Customer customer, String inputEmail, String newPassword, String confirmPassword) {
        if (customer == null) {
            throw new CustomerNotFoundException("❌ Customer not found.");
        }

        if (!isEmailMatching(customer, inputEmail)) {
            throw new InvalidEmailException("❌ Email does not match our records.");
        }

        if (newPassword == null || !isValidLength(newPassword.trim())) {
            throw new InvalidPasswordException("⚠️ New password must be at least 6 characters.");
        }

        if (!isPasswordConfirmed(newPassword, confirmPassword)) {
            throw new InvalidPasswordException("⚠️ Password confirmation does not match.");
        }

        customer.setPassword(newPassword.trim());
        repository.updateCustomer(customer);
        repository.saveAll();
        return true;
    }

    private boolean isEmailMatching(Customer customer, String inputEmail) {
        return inputEmail != null && customer.getEmail().equalsIgnoreCase(inputEmail.trim());
    }

    private boolean isPasswordConfirmed(String newPassword, String confirmPassword) {
        return newPassword != null && newPassword.trim().equals(confirmPassword == null ? "" : confirmPassword.trim());
    }

    private boolean isCurrentPasswordCorrect(Customer customer, String inputPassword) {
        return inputPassword != null && inputPassword.equals(customer.getPassword());

    }

    private boolean isValidLength(String password) {
        return password != null && password.length() >= 6;
    }

    public boolean updateCustomerName(Customer customer, String newName) {
        if (customer == null) {
            throw new  CustomerNotFoundException("❌ Customer not found.");
        }

        if (newName == null || newName.trim().isBlank()) {
            throw new InvalidNameException("⚠️ Name can't be empty.");
        }

        newName = newName.trim();
        customer.setName(newName);
        repository.updateCustomer(customer);
        repository.saveAll();
        return true;
    }


    public boolean updateCustomerAddress(Customer customer, String newAddress) {
        if (customer == null) {
            throw new CustomerNotFoundException("❌ Customer not found.");
        }

        if (newAddress == null || newAddress.trim().isEmpty()) {
            throw new InvalidAddressException("⚠️ Address cannot be empty.");
        }

        newAddress = newAddress.trim();
        customer.setAddress(newAddress);
        repository.updateCustomer(customer);
        repository.saveAll();
        return true;
    }

    public boolean updateCustomerBalance(Customer customer, double amount) {

        double currentBalance = customer.getBalance();
        double newBalance = currentBalance + amount;

        if (newBalance < 0) {
            throw new InsufficientBalanceException("⚠️ Insufficient funds. Cannot set negative balance.");

        }

        customer.setBalance(newBalance);
        repository.updateCustomer(customer);
        repository.saveAll();
        return true;
    }


    public boolean deleteCustomer(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidEmailException("⚠️ email cannot be empty.");

        }
        repository.deleteByEmail(email);
        repository.saveAll();
        return true;

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

