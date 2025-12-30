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
        logger.info("Attempting to register new user");


        try {
            Customer customer = signUpService.registerNewCustomer(name, email, password, balance, address);
            logger.log(Level.INFO,"User registration successful for username: {0} ",name);
            signUpView.showSuccess(customer.getName());
            return customer;
        }catch (InvalidNameException e){
            signUpView.showError(e.getMessage());
        }catch (InvalidEmailFormatException e){
            signUpView.showError(e.getMessage());
            return null;

        }catch (InvalidEmailException e){
            signUpView.showError(e.getMessage());

        }catch (InvalidPasswordException e){
            signUpView.showError(e.getMessage());

        }catch (InvalidBalanceException e){
            signUpView.showError(e.getMessage());

        }catch (InvalidAddressException e){
            signUpView.showError(e.getMessage());
        }
        logger.log(Level.WARNING,"Error during user registration for username: {0}",name);
        return null;
    }
}
