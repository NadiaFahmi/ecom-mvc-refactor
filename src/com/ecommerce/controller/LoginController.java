package com.ecommerce.controller;

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

    public User handleLogin() {
        String email = loginView.promptEmail();
        String password = loginView.promptPassword();

        User user = loginService.login(email, password);
        if (user == null) {
            loginView.showLoginFailed();
        } else {
            loginView.showWelcome(user);
            return user;
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

                    boolean success = loginService.resetPassword(customer, email, newPassword, confirmPassword);
                    loginView.showPasswordResetResult(success);
                    return null;
                }

                case "no" -> {
                    String password = loginView.promptPassword();
                    User user = loginService.login(email, password);

                    if (user != null) {
                        loginView.showWelcome(user);
                        return user;
                    } else {
                        loginView.showIncorrectPassword();
                    }
                }

                case "exit" -> {
                    loginView.showExitMessage();
                    return null;
                }

                default -> loginView.showInvalidChoice();
            }
        }}
}
