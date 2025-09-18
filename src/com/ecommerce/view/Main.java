package com.ecommerce.view;


import com.ecommerce.controller.*;
import com.ecommerce.model.entities.Admin;


import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.model.entities.User;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        CustomerRepository customerRepository = new CustomerRepository("customers.txt");

        ProductRepository productRepo = new ProductRepository("products.txt");
        ProductService productService = new ProductService(productRepo);

        CustomerService customerService = new CustomerService(customerRepository);
        customerService.loadCustomers();

        OrderService orderService = new OrderService(productService, customerService);
        CustomerController customerController = new CustomerController(customerService);
        orderService.validateManagers();


        User user;
        while (true) {
            System.out.println("👤 Welcome to Nadia’s Shop!");
            System.out.println("1 - Log In");
            System.out.println("2 - Sign Up");
            System.out.println("Type 'exit' to leave the app.");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                LoginService loginService =new LoginService(customerService);
                LoginController loginController = new LoginController(loginService);
                user = loginController.handleLogin(scanner);


            } else if (choice.equals("2")) {
                SignUpService signUpService = new SignUpService(customerService);
                SignUpController signUpController = new SignUpController(signUpService);
                user = signUpController.handleSignUp(scanner);
//
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
            AdminService adminService = new AdminService(customerService, orderService, productService, admin);
            AdminDashboard adminDashboard = new AdminDashboard();
            adminDashboard.launch(scanner, adminService);
        } else if (user instanceof Customer customer) {
            //
            Customer loggedInCustomer = customerService.findCustomerById(user.getId());
            CartService cartService = new CartService(loggedInCustomer.getId(), customerService, productService);
            cartService.loadCartFromFile();


            CartController cartController = new CartController(cartService);
            OrderController orderController = new OrderController(orderService);

            orderController.loadOrdersFromFile();
            List<Order> allOrders = orderController.getAllOrders();

            for (Order order : allOrders) {
                if (order.getCustomer().getId() == loggedInCustomer.getId()) {
                    order.setCustomer(loggedInCustomer);
                    customer.addOrder(order);
                }
            }

            CustomerDashboard customerDashboard = new CustomerDashboard(
                    customer,
                    productService,
                    cartController,
                    orderService,
                    orderController,
                    customerController,
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