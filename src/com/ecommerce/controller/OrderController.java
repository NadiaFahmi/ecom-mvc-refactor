package com.ecommerce.controller;

import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.service.CustomerService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderController {
//    private final OrderService orderService;
//
//
//    public OrderController(OrderService orderService) {
//        this.orderService = orderService;
//    }
//
//public Order createOrder(Customer customer, Scanner scanner) {
//    return orderService.placeOrder(customer, scanner);
//}
//
//
//public List<Order> getAllOrders() {
//    return orderService.getOrders();
//}
//    public void loadOrdersFromFile() {
//        orderService.loadOrdersFromFile();
//    }
//    public void printCustomerOrders(Customer customer) {
//        List<Order> myOrders = customer.getOrders();
//
//        if (myOrders.isEmpty()) {
//            System.out.println("📭 You haven’t placed any orders yet.");
//            return;
//        }
//
//        for (Order ord : myOrders) {
//            System.out.println("🆔 Order ID: " + ord.getOrderId());
//            System.out.println("🗓️ Date: " + ord.getOrderDate());
//            System.out.println("🛒 Items:");
//            for (CartItem item : ord.getCartItems()) {
//                String name = item.getProduct().getName();
//                double price = item.getProduct().getPrice() * item.getQuantity();
//                System.out.printf(" - %s x%d = $%.2f%n", name, item.getQuantity(), price);
//            }
//            System.out.printf("💰 Total: $%.2f%n", ord.getOrder_total());
//            System.out.println("📌 Status: " + ord.getStatus());
//            System.out.println("------");
//        }
//    }
//
//    public void printOrderDetails(Order order, Customer customer) {
//        System.out.println("\n🎉 THANK YOU FOR YOUR PURCHASE, " + customer.getName() + "!");
//        System.out.println("🆔 Order ID: " + order.getOrderId());
//        System.out.println("📅 Date: " + order.getOrderDate());
//        System.out.println("📦 Status: " + order.getStatus());
//        System.out.println("🛒 Items:");
//
//        double total = 0.0;
//        for (CartItem item : order.getCartItems()) {
//            String name = item.getProduct().getName();
//            int quantity = item.getQuantity();
//            double price = item.getProduct().getPrice() * quantity;
//            total += price;
//            System.out.printf(" - %s x%d = $%.2f%n", name, quantity, price);
//        }
//
//        System.out.printf("💰 TOTAL: $%.2f%n", total);
//        System.out.printf("💳 Remaining Balance: $%.2f%n", customer.getBalance());
//        System.out.println("✨ Your order is confirmed and being processed!\n");
//    }
//    public void filterCustomerOrdersByDate(Customer customer, LocalDate date) {
//        List<Order> filteredOrders = new ArrayList<Order>();
//
//        for (Order order : customer.getOrders()) {
//            if (order.getOrderDate().toLocalDate().equals(date)) {
//                filteredOrders.add(order);
//            }
//        }
//
//        if (filteredOrders.isEmpty()) {
//            System.out.println("📭 No orders found for " + date);
//            return;
//        }
//
//        for (Order order : filteredOrders) {
//            printOrderDetails(order, customer);
//            System.out.println("–––––––––––––––––––––––");
//        }
//    }
private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    public void handlePlaceOrder(Customer customer, Scanner scanner) {
        List<CartItem> cartItems = customer.getCart().getCartItems();
        if (cartItems.isEmpty()) {
            System.out.println("🛒 Your cart is empty. Add items before placing an order.");
            return;
        }

        double total = orderService.calculateTotal(cartItems);
        if (customer.getBalance() < total) {
            System.out.printf("❌ Insufficient balance. Cart total: $%.2f | Your balance: $%.2f%n", total, customer.getBalance());
            System.out.print("Would you like to add funds to complete the purchase? (Y/N): ");
            String response = scanner.nextLine().trim();

            if (response.equalsIgnoreCase("Y")) {
                System.out.print("Enter amount to add: ");
                try {
                    double additional = Double.parseDouble(scanner.nextLine());
                    boolean success = orderService.tryAddFunds(customer, additional, total);

                    if (!success) {
                        System.out.println("🚫 Still insufficient. Please adjust your cart or add more funds.");
                        return;
                    }

                    System.out.printf("💳 New balance: $%.2f%n", customer.getBalance());
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid amount entered. Order cancelled.");
                    return;
                }
            } else {
                System.out.println("🕳️ Order cancelled. Feel free to come back anytime.");
                return;
            }
        }

        Order order = orderService.placeOrder(customer);
        if (order != null) {
            System.out.println("✅ Order placed successfully!");
        } else {
            System.out.println("❌ Order could not be placed.");
        }
    }


    public List<Order> getAllOrders() {
        List<Order> orders=orderService.getOrders();
        return orders;
//        return orders;
    }

    public void loadOrdersFromFile() {
        orderService.getOrders();
    }

    public  void printCustomerOrders( Customer customer){

        List<Order> allOrders = orderService.getOrders();
        boolean found = false;

        for (Order order : allOrders) {
            if (order.getCustomer().getEmail().equals(customer.getEmail())) {
                found = true;
                System.out.println("🆔 Order ID: " + order.getOrderId());
                System.out.println("🗓️ Date: " + order.getOrderDate());
                System.out.println("🛒 Items:");
                for (CartItem item : order.getCartItems()) {
                    String name = item.getProduct().getName();
                    double price = item.getProduct().getPrice() * item.getQuantity();
                    System.out.printf(" - %s x%d = $%.2f%n", name, item.getQuantity(), price);
                }
                System.out.printf("💰 Total: $%.2f%n", order.getOrder_total());
                System.out.println("📌 Status: " + order.getStatus());
                System.out.println("------");
            }
        }

        if (!found) {
            System.out.println("📭 You haven’t placed any orders yet.");
        }
    }


    public void printOrderDetails(Order order, Customer customer) {
        System.out.println("\n🎉 THANK YOU FOR YOUR PURCHASE, " + customer.getName() + "!");
        System.out.println("🆔 Order ID: " + order.getOrderId());
        System.out.println("📅 Date: " + order.getOrderDate());
        System.out.println("📦 Status: " + order.getStatus());
        System.out.println("🛒 Items:");

        double total = 0.0;
        for (CartItem item : order.getCartItems()) {
            String name = item.getProduct().getName();
            int quantity = item.getQuantity();
            double price = item.getProduct().getPrice() * quantity;
            total += price;
            System.out.printf(" - %s x%d = $%.2f%n", name, quantity, price);
        }

        System.out.printf("💰 TOTAL: $%.2f%n", total);
        System.out.printf("💳 Remaining Balance: $%.2f%n", customer.getBalance());
        System.out.println("✨ Your order is confirmed and being processed!\n");
    }
    public void filterCustomerOrdersByDate(Customer customer, LocalDate date) {
        List<Order> filteredOrders = new ArrayList<Order>();

        for (Order order : customer.getOrders()) {
            if (order.getOrderDate().toLocalDate().equals(date)) {
                filteredOrders.add(order);
            }
        }

        if (filteredOrders.isEmpty()) {
            System.out.println("📭 No orders found for " + date);
            return;
        }

        for (Order order : filteredOrders) {
            printOrderDetails(order, customer);
            System.out.println("–––––––––––––––––––––––");
        }
    }
}
