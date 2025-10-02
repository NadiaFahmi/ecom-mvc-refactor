package com.ecommerce.controller;

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
            return handleRetry(email); // return result of retry
        } else {
            loginView.showWelcome(user);
            return user;
        }
    }


    private User handleRetry(String email) {
        while (true) {
            String choice = loginView.promptRetryChoice();

            switch (choice) {
                case "yes" -> {
                    loginService.resetPassword(email);
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
        }
    }
}
