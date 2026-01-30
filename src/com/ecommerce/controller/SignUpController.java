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
            return handleError(Level.WARNING,"Name validation failed: Input empty",e);
        }
        catch (InvalidEmailFormatException e){
            return handleError(Level.SEVERE,"Email validation failed: Invalid format",e);
        } catch (InvalidEmailException e){
            return handleError(Level.SEVERE,"Email validation failed: Invalid input",e);
        }
        catch ( DuplicateEmailException e){
            return handleError(Level.WARNING, "Email validation failed: Email already registered.",e);
        }
        catch (InvalidPasswordException e){
            return handleError(Level.SEVERE,"Password validation failed: Invalid format",e);
        }catch (InvalidBalanceException e){
            return handleError(Level.WARNING,"Balance validation failed: Invalid input",e);
        }catch (InvalidAddressException e){
            return handleError(Level.WARNING,"Address validation failed: Input empty",e);
        }
        }

        private Customer handleError(Level level,String logMessage, Exception e){
        logger.log(level,logMessage,e);
        signUpView.showError(e.getMessage());
        return null;
        }

    }
