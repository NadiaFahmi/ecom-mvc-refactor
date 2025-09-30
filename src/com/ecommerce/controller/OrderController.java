package com.ecommerce.controller;

import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.service.CustomerService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.view.OrderView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderController {

private final OrderService orderService;

    private final OrderView orderView;
    public OrderController(OrderService orderService, OrderView orderView) {
        this.orderService = orderService;
        this.orderView = orderView;
    }
    public void handlePlaceOrder(Customer customer, Scanner scanner) {
        List<CartItem> cartItems = customer.getCart().getCartItems();
        if (cartItems.isEmpty()) {
            orderView.showCartEmpty();
            return;
        }

        double total = orderService.calculateTotal(cartItems);
        if (customer.getBalance() < total) {


            if (orderView.confirmAddFunds(total, customer.getBalance())) {
                double additional = orderView.promptAmountToAdd();
                if (additional <= 0 || !orderService.tryAddFunds(customer, additional, total)) {
                    orderView.showInsufficientAfterAdd();
                    return;
                }
                orderView.showNewBalance(customer.getBalance());
            } else {
                orderView.showOrderCancelled();
                return;
            }
        }

        Order order = orderService.placeOrder(customer);
        if (order != null) {
            orderView.showOrderSuccess();
        } else {
            orderView.showOrderFailure();
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
