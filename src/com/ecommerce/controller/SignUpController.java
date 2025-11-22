package com.ecommerce.controller;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.SignUpService;
import com.ecommerce.view.SignUpView;


public class SignUpController {
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
            signUpView.showSuccess(customer.getName());
            return customer;
        }
        catch (InvalidNameException|InvalidEmailFormatException | InvalidEmailException |
               InvalidPasswordException | InvalidBalanceException | InvalidAddressException e) {
            signUpView.showError(e.getMessage());
    }

        return null;
    }
}
