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


    public void updateCustomerEmail(Customer customer, String email) {
        logger.log(Level.INFO,"Attempting update customer email={0}",email);
        try {
            customerService.updateCustomerEmail(customer, email);
            logger.log(Level.INFO,"Successfully updated for customer with email={0}",email);
            customerView.showEmailUpdated();
        } catch (InvalidEmailException e) {
            logger.log(Level.WARNING,"Failed invalid email={0}",email);
            customerView.showError(e.getMessage());
        }
    }

    public void updateName(int customerId, String newName) {
        try {
            Customer customer = customerService.findCustomerById(customerId);
            customerService.updateCustomerName(customer, newName);
            logger.info("Successfully updated name" );
            customerView.showNameUpdated();
        } catch (InvalidNameException e) {
            logger.warning("Failed invalid name" );
            customerView.showError(e.getMessage());
        }
    }

    public void updateAddress(int customerId, String newAddress) {
        try {
            Customer customer = customerService.findCustomerById(customerId);
            customerService.updateCustomerAddress(customer, newAddress);
            logger.info("Successfully updated address" );
        } catch (InvalidAddressException e) {
            logger.warning("Failed invalid address" );
            customerView.showError(e.getMessage());
        }
    }

    public void updateBalance(int customerId, double amount) {
        Customer customer = customerService.findCustomerById(customerId);
        logger.log(Level.INFO,"Attempting to update balance for customer email={0}",customer.getEmail());
        try {
            customerService.updateCustomerBalance(customer, amount);
            logger.log(Level.INFO,"Successfully updated balance for customer email={0}",customer.getEmail());
        } catch (InvalidBalanceException e) {
            logger.warning("Cannot set negative balance");
            customerView.showInvalidBalance(e.getMessage());
        }
    }


    public void deleteCustomerByEmail(String email) {
        logger.log(Level.INFO,"Attempting to delete customer email={0}",email);
        try {
            customerService.deleteCustomer(email);
            logger.log(Level.INFO,"Successfully deleted customer with email={0}",email);
            customerView.showDeleteCustomer();

        } catch (InvalidEmailException |CustomerNotFoundException e) {
            logger.log(Level.WARNING,"Failed delete customer. Reason:{0} ",e.getMessage());
            customerView.showError(e.getMessage());
        }
    }

    public void showLoggedInCustomerOrders(Customer customer) {
        List<Order> orders = new ArrayList<>();

        customerService.getLoggedInCustomerWithOrders(customer,orders);
        logger.log(Level.INFO,"getLoggedInCustomerWithOrders() invoked for customer email={0}",customer.getEmail());
        customerView.displayCustomerWithOrders(customer,orders);

    }
    public void resetPassword(int customerId, String inputEmail, String newPassword, String confirmPassword) {
        Customer customer = customerService.findCustomerById(customerId);

        try {
            customerService.resetPassword(customer, inputEmail, newPassword, confirmPassword);
            logger.log(Level.INFO,"resetPassword() invoked for customer id={0}",customerId);
            customerView.showSuccess();
        } catch (CustomerNotFoundException e) {
            logger.warning("Reset failed: customer null");
            customerView.showError("❌ " + e.getMessage());

        } catch(InvalidEmailException e){
            logger.warning("Invalid email");
            customerView.showError(e.getMessage());
        }
        catch (InvalidPasswordException e) {
            logger.warning("Invalid password");
            customerView.showError("⚠️ " + e.getMessage());
        } catch (Exception e) {
            logger.warning("Error Unexpected");
            customerView.showError("⚠️ Unexpected error: " + e.getMessage());
        }

    }

}
