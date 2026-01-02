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
            logger.log(Level.INFO, "User registration successful for username: {0} ", name);
            signUpView.showSuccess(customer.getName());
            return customer;
        }
        catch (InvalidNameException e){
            logger.warning("Name validation failed: Input empty");
            signUpView.showError(e.getMessage());
        }
        catch (InvalidEmailFormatException e){
            logger.severe("Email validation failed: Invalid format");
            signUpView.showError(e.getMessage());
            return null;

        }
        catch ( InvalidEmailException e){
            logger.warning("Email validation failed: Email already registered.");
            signUpView.showError(e.getMessage());
        }
////               | InvalidEmailException | InvalidPasswordException | InvalidBalanceException |
////               InvalidAddressException
//                ;}


        catch (InvalidPasswordException e){
            logger.severe("Password validation failed: Invalid format");
            signUpView.showError(e.getMessage());

        }catch (InvalidBalanceException e){
            logger.warning("Balance validation failed: Invalid input");
            signUpView.showError(e.getMessage());

        }catch (InvalidAddressException e){
            logger.warning("Address validation failed: Input empty");
            signUpView.showError(e.getMessage());
        }
            logger.log(Level.WARNING, "Error during  registration");
//            signUpView.showError(e.getMessage());
            return null;
        }
    }
