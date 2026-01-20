package com.ecommerce.view;

import com.ecommerce.controller.CartController;
import com.ecommerce.controller.CustomerController;
import com.ecommerce.controller.OrderController;
import com.ecommerce.controller.ProductController;
import com.ecommerce.model.entities.Customer;

import java.util.Scanner;

public class CustomerDashboard {
    private  Customer customer;
    private CartController cartController;
    private ProductController productController;
    private CustomerController customerController;
    private  OrderController orderController;
    private CustomerUpdateView customerUpdateView;
    private final Scanner scanner;

    public CustomerDashboard(Customer customer,
                             ProductController productController,
                             CartController cartController,
                             OrderController orderController,
                             CustomerController customerController,
                             CustomerUpdateView customerUpdateView,
                             Scanner scanner) {
        this.customer = customer;
        this.cartController = cartController;
        this.productController = productController;
        this.orderController = orderController;
        this.customerController = customerController;
        this.customerUpdateView = customerUpdateView;
        this.scanner = scanner;

    }

    public void launch() {

        boolean running = true;

        while (running) {
            System.out.println("\n🛒 Customer Dashboard — Choose an action:");
            System.out.println("🛍️ --- Product & Cart Actions ---");
            System.out.println("1 - View Products");
            System.out.println("2 - Add Product to Cart");
            System.out.println("3 - View Cart");
            System.out.println("4 - Remove Item from Cart");
            System.out.println("5 - Check Total Cart Price");
            System.out.println("6 - Update Cart Item");
            System.out.println("📦 --- Order Processing ---");
            System.out.println("7 - Place Order");
            System.out.println("8 - View My Orders");
            System.out.println("9 - Filter My Orders By Date");
            System.out.println("10 - Check Total Order Price");
            System.out.println("👤 --- Account ---");
            System.out.println("11 - Update My Account Info");
            System.out.println("12 - Delete My Account");
            System.out.println("13 - View My Info");
            System.out.println("exit - Logout and Exit Dashboard");

            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> productController.getProducts();

                case "2" ->  cartController.addProductToCart(customer);

                case "3" -> cartController.getCartItems(customer);

                case "4" ->  cartController.removeProductFromCart(customer);

                case "5" -> cartController.calculatePrice(customer);

                case "6" -> cartController.UpdateQuantity(customer);

                case "7" -> orderController.handlePlaceOrder(customer);

                case "8" -> orderController.getOrdersForCustomer(customer);

                case "9" ->orderController.filterCustomerOrdersByDate(customer);

                case "10" -> orderController.getTotalOrderPrice(customer);

                case "11" -> customerUpdateView.launchUpdateMenu(customer, scanner);

                case "12" -> customerController.deleteCustomerByEmail();

                case "13" -> customerController.getCustomerProfileWithOrders(customer);

                case "exit" -> {
                    System.out.println("👋 Logging out. See you soon, " + customer.getName() + "!");
                    running = false;
                }
                default -> System.out.println("⚠️ Invalid choice. Please try again.");
            }
        }
    }

}
