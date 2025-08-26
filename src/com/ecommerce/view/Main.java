package com.ecommerce.view;


import com.ecommerce.controller.SignUpController;
import com.ecommerce.model.entities.Admin;


import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.User;
import com.ecommerce.service.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ProductService productService = new ProductService();
        productService.loadProductsFromFile();

        CustomerService customerService = new CustomerService();
        customerService.loadCustomersFromFile();

        OrderServic orderServic = new OrderServic(productService, customerService);
        orderServic.validateManagers();
        orderServic.loadOrdersFromFile(productService, customerService, null);


        LoginService loginService = new LoginService(customerService);

        User user;
        while (true) {
            System.out.println("👤 Welcome to Nadia’s Shop!");
            System.out.println("1 - Log In");
            System.out.println("2 - Sign Up");
            System.out.println("Type 'exit' to leave the app.");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                user = loginService.login(scanner);
            } else if (choice.equals("2")) {
                SignUpService signUpService = new SignUpService(customerService);
                SignUpController signUpController = new SignUpController(signUpService);
                user = signUpController.handleSignUp(scanner);
//                user = signUpService.registerNewCustomer(scanner);
            } else if (choice.equalsIgnoreCase("exit")) {
                System.out.println("👋 Thanks for visiting Nadia’s Shop. Goodbye!");
                scanner.close();
                return;
            } else {
                System.out.println("⚠️ Invalid choice. Try again.\n");
                continue;
            }

            if (user != null) break;
        }


        if (user instanceof Admin admin) {
            AdminService adminService = new AdminService(customerService, orderServic, productService, admin);
            AdminDashboard adminDashboard = new AdminDashboard();
            adminDashboard.launch(scanner, adminService);
        } else if (user instanceof Customer customer) {
            CartService cartService = new CartService(customer, productService);
            cartService.loadCartFromFile();

            UpdateService updateService = new UpdateService(customerService);

            CustomerDashboard customerDashboard = new CustomerDashboard(
                    customer,
                    productService,
                    cartService,
                    orderServic,
                    updateService,
                    scanner
            );

            customerDashboard.launch();
        }

        scanner.close();
        System.out.println("📦 Session ended. Thank you!");
    }
}
//    admin@email.com
//    adminPass