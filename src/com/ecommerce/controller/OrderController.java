package com.ecommerce.controller;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.CustomerService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;

import java.time.LocalDate;
import java.util.Scanner;

public class OrderController {
    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void handlePlaceOrder(Customer customer, Scanner scanner) {
        orderService.placeOrder(customer, scanner);
        System.out.println("✅ Order placed successfully.");
    }

    public void handlePrintCustomerOrders(Customer customer) {
        orderService.printCustomerOrders(customer);
        System.out.println("📦 Displayed all orders for customer: " + customer.getName());
    }

    public void handleFilterCustomerOrdersByDate(Customer customer, LocalDate date) {
        orderService.filterCustomerOrdersByDate(customer, date);
        System.out.println("📅 Orders filtered by date: " + date);
    }

    public void initializeOrders(ProductService productService, CustomerService customerService, Customer loggedInCustomer) {
        orderService.loadOrdersFromFile(productService, customerService, loggedInCustomer);
    }
}
