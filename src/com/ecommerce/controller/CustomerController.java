package com.ecommerce.controller;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Customer;

import com.ecommerce.model.entities.Order;
import com.ecommerce.service.CustomerService;
import com.ecommerce.view.CustomerView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CustomerController {

    private Logger logger = Logger.getLogger(CustomerController.class.getName());

    private CustomerService customerService;
    private CustomerView customerView;

    public CustomerController(CustomerService customerService
            , CustomerView customerView
    ) {
        this.customerService = customerService;
        this.customerView = customerView;
    }

    public void loadCustomers()
    {
        customerService.loadCustomers();
    }


    public void updateCustomerEmail(Customer customer) {
        String email =customerView.promptNewEmail();

        try {
            customerService.updateCustomerEmail(customer, email);
            logger.info("Successfully updated email");
            customerView.showEmailUpdated();
        } catch (InvalidEmailException e) {
            logger.log(Level.WARNING,"Failed invalid email={0}",email);
            customerView.showError(e.getMessage());
        }catch (IllegalArgumentException e){
            logger.warning("Email already in use.");
            customerView.showError(e.getMessage());
        }
    }

        public void updateName(Customer customer) {
        String newName = customerView.promptNewName();
        try {
            customerService.updateCustomerName(customer, newName);
            logger.info("Successfully updated name" );
            customerView.showNameUpdated();
        } catch (InvalidNameException e) {
            logger.warning("Failed invalid name" );
            customerView.showError(e.getMessage());
        }
    }

        public void updateAddress(Customer customer) {
        String newAddress =customerView.promptNewAddress();
        try {
            customerService.updateCustomerAddress(customer, newAddress);
            logger.info("Successfully updated address" );
        } catch (InvalidAddressException e) {
            logger.warning("Failed invalid address" );
            customerView.showError(e.getMessage());
        }
    }

        public void updateBalance(Customer customer) {

        double amount =customerView.promptNewBalance();


        try {
            customerService.updateCustomerBalance(customer, amount);
            logger.log(Level.INFO,"Successfully updated balance for customer name={0}",customer.getName());
        }catch (NumberFormatException e){
            customerView.showError(e.getMessage());
        }
        catch (InvalidBalanceException e) {
            logger.warning("Cannot set negative balance");
            customerView.showError(e.getMessage());
        }
    }

    public void deleteCustomerByEmail() {

        String email= customerView.promptEmail();

        try {
            customerService.deleteCustomer(email);
            logger.log(Level.INFO,"Deleted successful");
            customerView.showDeleteCustomer();

        } catch (InvalidEmailException |CustomerNotFoundException e) {
            logger.log(Level.WARNING,"Failed delete customer. Reason:{0} ",e.getMessage());
            customerView.showError(e.getMessage());
        }
    }

    public void getCustomerProfileWithOrders(Customer customer) {
        List<Order> orders = new ArrayList<>();

        customerService.getCustomerProfileWithOrders(customer,orders);
        customerView.displayCustomerWithOrders(customer,orders);

    }

        public void updatePassword(Customer customer
    ) {
        String currentPassword = customerView.promptCurrentPassword();
        String newPassword = customerView.promptNewPassword();
        String confirmPassword = customerView.promptConfirmedPassword();
        customerService.updatePassword(customer, currentPassword, newPassword, confirmPassword);

    }


}
