package com.ecommerce.service;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.repository.CustomerRepository;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerService {

    private Logger logger = Logger.getLogger(CustomerService.class.getName());

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

    public void updateCustomerEmail(Customer customer, String newEmail) {

        isValidEmail(newEmail);
        ensureEmailIsAvailable(customer.getId(),newEmail);
        customer.setEmail(newEmail);
        repository.updateCustomer(customer);
        logger.log(Level.INFO,"Successfully updated email. CustomerId={0}",customer.getId());
        repository.saveAll();

    }
    public void ensureEmailIsAvailable(int customerId, String email){
        boolean existEmail=repository.isEmailTakenByAnother(email,customerId);
        if (existEmail){
            throw new IllegalArgumentException("Email already exist.");
        }
    }
    private void isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new InvalidEmailException("Email cannot be empty.");
        }
        if(!email.matches(EMAIL_REGEX)) {
            throw new InvalidEmailException("Invalid email format!");
        }

    }

    public void resetPassword(String inputEmail, String newPassword, String confirmPassword) {
        Customer customer = repository.getCustomerByEmail(inputEmail);
        if (customer == null) {
            throw new CustomerNotFoundException("❌ Customer not found.");
        }
        isEmailMatching(customer, inputEmail);
        isValidLength(newPassword);
        isPasswordConfirmed(newPassword, confirmPassword);

        customer.setPassword(newPassword.trim());
        repository.updateCustomer(customer);
        repository.saveAll();
        logger.log(Level.INFO,"Password reset for customer id={0}", customer.getId());
    }
    public void updatePassword(Customer customer, String currentPassword, String newPassword, String confirmPassword) {


        if (customer == null) {
            throw new CustomerNotFoundException("❌ Customer not found.");
        }

        isCurrentPasswordCorrect(customer,currentPassword);
        isValidLength(newPassword);
        isPasswordConfirmed(newPassword,confirmPassword);

        customer.setPassword(newPassword.trim());
        repository.updateCustomer(customer);
        repository.saveAll();
    }
    private void isCurrentPasswordCorrect(Customer customer, String inputPassword) {
        if( inputPassword == null || !inputPassword.equals(customer.getPassword())){
            throw new InvalidPasswordException("Invalid Password");
        }
    }

    private void isEmailMatching(Customer customer, String inputEmail) {
        if(customer == null ){
            throw new CustomerNotFoundException("Customer not found");
        }
        if(inputEmail == null || inputEmail.trim().isEmpty()){
            throw new InvalidEmailException("Email can not be null");

        }if( !customer.getEmail().equalsIgnoreCase(inputEmail.trim())){
            throw new InvalidEmailException("Email does not match our record");
        }
    }

    private void isPasswordConfirmed(String newPassword, String confirmPassword) {
        if(newPassword == null){
            throw new InvalidPasswordException("New newPassword can not be null");
        }
        if(!newPassword.trim().equals(confirmPassword == null ? "" : confirmPassword.trim())){
            throw new InvalidPasswordException("⚠️ Password confirmation does not match.");
        }
    }

    private void isValidLength(String password) {

        if(password == null || password.length() < 6){
            throw new InvalidPasswordException("⚠️ New password must be at least 6 characters.");
        }

    }

    public void updateCustomerName(Customer customer, String newName) {
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

    }

    public void updateCustomerAddress(Customer customer, String newAddress) {
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
    }

    public void updateCustomerBalance(Customer customer, double amount) {

        double currentBalance = customer.getBalance();
        double newBalance = currentBalance + amount;
        if (newBalance < 0) {
            throw new InvalidBalanceException("Balance cannot be negative");
        }
        customer.setBalance(newBalance);
        repository.updateCustomer(customer);
        logger.log(Level.INFO,"Updated current balance={0} for customer name={1} ",new Object[]{currentBalance,customer.getName()});
        repository.saveAll();

    }


    public void deleteCustomer(String email) {

        if (email == null || email.trim().isEmpty()) {
            throw new InvalidEmailException("⚠️ email cannot be empty.");

        }
        repository.deleteByEmail(email);
        repository.saveAll();
    }

    public void getCustomerProfileWithOrders(Customer input, List<Order> outputOrders) {
        logger.log(Level.INFO,"Fetching customer by Id={0}",input.getId());
        input = repository.getCustomerById(input.getId());

        List<Order> orders = orderService.getOrdersForCustomer(input.getEmail());
        logger.log(Level.INFO,"Fetched {0} orders for customer name={1}", new Object[]{orders.size(), input.getName()});

        outputOrders.clear();
        outputOrders.addAll(orders);

    }


}

