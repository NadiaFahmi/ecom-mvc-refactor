package com.ecommerce.controller;

import com.ecommerce.exception.InvalidEmailException;
import com.ecommerce.exception.InvalidPasswordException;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.User;
import com.ecommerce.service.LoginService;
import com.ecommerce.view.LoginView;


public class LoginController {

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
                loginView.showWelcome(user);
                return user;
            }
        }catch (InvalidEmailException | InvalidPasswordException e){
            loginView.showError(e.getMessage());
        }

        return null;
    }

    public User handleRetry(Customer customer, String email) {

        while (true) {
            String choice = loginView.promptRetryChoice();

            switch (choice) {
                case "yes" -> {
                    String newPassword = loginView.promptNewPassword();
                    String confirmPassword = loginView.promptConfirmPassword();

                    try {
                        loginService.resetPassword(customer, email, newPassword, confirmPassword);
                        loginView.showPasswordResetResult(true);
                    } catch (InvalidPasswordException e) {
                        loginView.showError(e.getMessage());
                        loginView.showPasswordResetResult(false);
                    }
                    return null;

                }

                case "no" -> {
                    loginAuth(); // retry login flow
                    break;
                }

                case "exit" -> {
                    loginView.showExitMessage();
                    return null;
                }

                default -> loginView.showInvalidChoice();
            }
        }}
}
