package com.ecommerce.view;


import com.ecommerce.controller.*;
import com.ecommerce.model.entities.Admin;


import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.model.entities.User;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        ProductRepository productRepo = new ProductRepository("products.txt");
        ProductService productService = new ProductService(productRepo);
        ProductView productView = new ProductView();
        ProductController productController = new ProductController(productService, productView);

        CustomerRepository customerRepository = new CustomerRepository("customers.txt");
        CustomerService customerService = new CustomerService(customerRepository);
        CustomerController customerController = new CustomerController(customerService);
        CustomerUpdateView customerUpdateView = new CustomerUpdateView(customerController);
        customerController.load();


        LoginService loginService = new LoginService(customerService);
        User user = null;
        while (true) {
            System.out.println("👤 Welcome to Nadia’s Shop!");
            System.out.println("1 - Log In");
            System.out.println("2 - Sign Up");
            System.out.println("3 - Forgot your password");
            System.out.println("Type 'exit' to leave the app.");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                LoginView loginView = new LoginView(scanner);
                LoginController loginController = new LoginController(loginService,loginView);
                user = loginController.handleLogin();


            } else if (choice.equals("2")) {
                SignUpService signUpService = new SignUpService(customerService);
                SignUpView signUpView = new SignUpView(scanner);
                SignUpController signUpController = new SignUpController(signUpService, signUpView);
                user = signUpController.handleSignUp();
//
            } else if (choice.equals("3")) {
                LoginView loginView = new LoginView(scanner);
                LoginController loginController = new LoginController(loginService, loginView);

                System.out.print("📧 Enter your email: ");
                String email = scanner.nextLine().trim();

                Customer customer = loginService.getCustomerByEmail(email);
                loginController.handleRetry(customer,email);

            }
            else if (choice.equalsIgnoreCase("exit")) {
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
            OrderService orderService = new OrderService(productService, customerService, null);
            AdminService adminService = new AdminService(customerService, orderService);
            TransactionView transactionView = new TransactionView();
            CustomerView customerView = new CustomerView();
            AdminController adminController =new AdminController(adminService, transactionView, customerView);
            AdminDashboard adminDashboard = new AdminDashboard(adminController,productController,productView,scanner );
            adminDashboard.launch();
        } else if (user instanceof Customer customer) {
            CartRepository cartRepository = new CartRepository(productService);
            CartService cartService = new CartService(productService, cartRepository);
            OrderService orderService = new OrderService(productService, customerService, cartService);
            CartView cartView = new CartView();
            CartController cartController = new CartController(cartService,cartView);
            cartController.loadCart(customer);
            OrderView orderView = new OrderView(scanner);
            OrderController orderController = new OrderController(orderService, orderView);
            orderController.loadOrdersFromFile();

            List<Order> allOrders = orderController.getAllOrders();
            for (Order order : allOrders) {
                if (order.getCustomer().getId() == customer.getId()) {
                    customer.addOrder(order);
                }
            }

            CustomerDashboard customerDashboard = new CustomerDashboard(
                    customer,
                    productController,
                    cartController,
                    orderController,
                    customerController,
                    customerUpdateView,
                    scanner
            );

            customerDashboard.launch();
        }

        scanner.close();
        System.out.println("📦 Session ended. Thank you!");
    }
}
//    admin@gmail.com
//    adminPass