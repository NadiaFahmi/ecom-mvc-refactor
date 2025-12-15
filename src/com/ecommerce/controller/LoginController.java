package com.ecommerce.controller;

import com.ecommerce.exception.InvalidEmailException;
import com.ecommerce.exception.InvalidPasswordException;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.User;
import com.ecommerce.service.LoginService;
import com.ecommerce.view.LoginView;

import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginController {

    private Logger logger = Logger.getLogger(LoginController.class.getName());

    private final LoginService loginService;
    private final LoginView loginView;

    public LoginController(LoginService loginService, LoginView loginView) {
        this.loginService = loginService;
        this.loginView = loginView;
    }

    public User loginAuth() {
        String email = loginView.promptEmail();
        String password = loginView.promptPassword();

        try {
            User user = loginService.login(email, password);
            if (user != null) {
                logger.log(Level.INFO,"login() invoked successfully for customer email={0}: " , email);
                loginView.showWelcome(user);
                return user;
            }
        }
        catch (InvalidEmailException e){
            logger.warning("Email not found");
            loginView.showError(e.getMessage());

        }catch (InvalidPasswordException e){
            logger.warning("Incorrect password");
            loginView.showFailed(e.getMessage());
        }


        return null;
    }

    public void handleRetry(Customer customer, String email) {

        while (true) {
            String choice = loginView.promptRetryChoice();

            switch (choice) {
                case "yes" -> {
                    String newPassword = loginView.promptNewPassword();
                    String confirmPassword = loginView.promptConfirmPassword();

                    try {
                        loginService.resetPassword(customer, email, newPassword, confirmPassword);
                        loginView.showPasswordResetResult(true);
                        return;
                    } catch (InvalidPasswordException e) {
                        loginView.showError(e.getMessage());
                        loginView.showPasswordResetResult(false);
                    }

                }

                case "no" -> loginAuth();

                case "exit" -> loginView.showExitMessage();

                default -> loginView.showInvalidChoice();
            }
        }}
}
