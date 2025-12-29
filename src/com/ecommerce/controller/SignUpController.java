package com.ecommerce.controller;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.SignUpService;
import com.ecommerce.view.SignUpView;

import java.util.logging.Level;
import java.util.logging.Logger;


public class SignUpController {
    private Logger logger = Logger.getLogger(SignUpController.class.getName());
    private final SignUpService signUpService;
    private final SignUpView signUpView;

    public SignUpController(SignUpService signUpService, SignUpView signUpView) {
        this.signUpService = signUpService;
        this.signUpView = signUpView;
    }

    public Customer handleSignUp() {

        String name = signUpView.promptName();
        String email = signUpView.promptEmail();
        String password = signUpView.promptPassword();
        double balance = signUpView.promptBalance();
        String address = signUpView.promptAddress();


        try {
            Customer customer = signUpService.registerNewCustomer(name, email, password, balance, address);
            logger.log(Level.INFO,"Customer registration successful for: {0} ",name);
            signUpView.showSuccess(customer.getName());
            return customer;
        }catch (InvalidNameException e){

            logger.log(Level.WARNING,"Invalid name: {0}",name);
            signUpView.showError(e.getMessage());
        }catch (InvalidEmailFormatException e){
            logger.log(Level.WARNING,"Invalid email format: {0}",email);
            signUpView.showError(e.getMessage());
            return null;

        }catch (InvalidEmailException e){
            logger.warning("Signup attempt with duplicate email address");
            signUpView.showError(e.getMessage());

        }catch (InvalidPasswordException e){
            logger.warning("Invalid  password requirements");
            signUpView.showError(e.getMessage());

        }catch (InvalidBalanceException e){
            logger.log(Level.WARNING,"Invalid  balance: {0}",balance);
            signUpView.showError(e.getMessage());

        }catch (InvalidAddressException e){
            logger.log(Level.WARNING,"Invalid address: {0}",address);
            signUpView.showError(e.getMessage());
        }

        return null;
    }
}
