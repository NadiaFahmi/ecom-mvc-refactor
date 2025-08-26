package com.ecommerce.controller;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.SignUpService;

import java.util.Scanner;

public class SignUpController {
    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    public Customer handleSignUp(Scanner scanner) {
        return signUpService.registerNewCustomer(scanner);
    }


}
