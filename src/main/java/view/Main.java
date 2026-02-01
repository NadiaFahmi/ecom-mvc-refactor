package main.java.view;


import main.java.controller.*;
import main.java.model.entities.Admin;


import main.java.model.entities.Customer;
import main.java.model.entities.User;
import main.java.repository.CartRepository;
import main.java.repository.CustomerRepository;
import main.java.repository.OrderRepository;
import main.java.repository.ProductRepository;
import main.java.service.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ProductRepository productRepo = new ProductRepository("products.txt");
        CustomerRepository customerRepository = new CustomerRepository("customers.txt");

        // Services
        ProductService productService = new ProductService(productRepo);
        CartRepository cartRepository = new CartRepository();
        CartService cartService = new CartService(productService, cartRepository);
        OrderRepository orderRepository = new OrderRepository(productService);
        OrderService orderService = new OrderService(cartService, orderRepository, customerRepository);
        CustomerService customerService = new CustomerService(customerRepository, orderService);

        // Views and Controllers
        ProductView productView = new ProductView(scanner);
        ProductController productController = new ProductController(productService, productView);

        CustomerView customerView = new CustomerView(scanner);
        CustomerController customerController = new CustomerController(customerService, customerView);
        CustomerUpdateView customerUpdateView = new CustomerUpdateView(customerController);
        customerController.loadCustomers();


        LoginService loginService = new LoginService(customerService);
        User user = null;
        while (true) {

            System.out.println("1 - Log In");
            System.out.println("2 - Sign Up");
            System.out.println("3 - Forgot your password");
            System.out.println("Type 'exit' to leave the app.");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                LoginView loginView = new LoginView(scanner);
                LoginController loginController = new LoginController(loginService,loginView);
                user = loginController.loginAuth();

            } else if (choice.equals("2")) {
                SignUpService signUpService = new SignUpService(customerService);
                SignUpView signUpView = new SignUpView(scanner);
                SignUpController signUpController = new SignUpController(signUpService, signUpView);
                user = signUpController.handleSignUp();
//
            } else if (choice.equals("3")) {
                LoginView loginView = new LoginView(scanner);
                LoginController loginController = new LoginController(loginService, loginView);
                loginController.handleRetry();
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

            if (user instanceof Admin) {

            AdminService adminService = new AdminService(customerService,customerRepository, orderService);
            TransactionView transactionView = new TransactionView(scanner);

            AdminController adminController = new AdminController(adminService, transactionView, customerView);
            AdminDashboard adminDashboard = new AdminDashboard(adminController, productController, scanner);
            adminDashboard.launch();

        } else if (user instanceof Customer customer) {

            CartController cartController = new CartController(cartService, new CartView(scanner));
            OrderController orderController = new OrderController(orderService, new OrderView(scanner));
            orderController.loadOrdersFromFile();

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