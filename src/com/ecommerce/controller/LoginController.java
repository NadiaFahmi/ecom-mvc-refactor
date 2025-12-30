package com.ecommerce.controller;

import com.ecommerce.exception.InvalidEmailException;
import com.ecommerce.exception.InvalidPasswordException;
import com.ecommerce.model.entities.User;
import com.ecommerce.service.LoginService;
import com.ecommerce.view.LoginView;

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
        logger.info("Login attempt initiated.");

        String email = loginView.promptEmail();
        if(email == null){
            return null;
        }
        String password = loginView.promptPassword();

        try {
            User user = loginService.login(email, password);
            if (user != null) {
                logger.info("User logged in successfully.");

                loginView.showWelcome(user);
                return user;
            }
        }

        catch (InvalidEmailException e){
            loginView.showError(e.getMessage());

        }catch (InvalidPasswordException e){
            loginView.showFailed(e.getMessage());
        }

        logger.warning("Authentication failed");
        return null;
    }

    public void handleRetry() {
        logger.info("Login retry attempt initiated");

        while (true) {
            String choice = loginView.promptRetryChoice();

            switch (choice) {
                case "yes" -> {
                    String email = loginView.promptEmail();
                    String newPassword = loginView.promptNewPassword();
                    String confirmPassword = loginView.promptConfirmPassword();

                    try {
                        loginService.resetPassword(
                                email,
                                newPassword, confirmPassword);
                        logger.info("Password reset successfully.");
                        loginView.showPasswordResetResult(true);
                        return;
                    } catch (InvalidPasswordException e) {
                        logger.warning("Password reset failed during authentication retry");
                        loginView.showError(e.getMessage());
                        loginView.showPasswordResetResult(false);
                    }

                }

                case "no" -> loginAuth();

                case "exit" ->{ loginView.showExitMessage();
                System.exit(0);
                }

                default -> loginView.showInvalidChoice();
            }
        }

    }

}
