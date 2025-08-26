package com.ecommerce.controller;

import com.ecommerce.model.entities.User;
import com.ecommerce.service.LoginService;

import java.util.Scanner;

public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    public User handleLogin(Scanner scanner) {

        User user = loginService.login(scanner);
        if (user != null) {
            System.out.println("🔓 Login successful as " + user.getRole());
        } else {
            System.out.println("❌ Login failed.");
        }
        return user;
    }

}
