package com.ecommerce.service;

import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.model.entities.Product;
import com.ecommerce.repository.OrderRepository;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderService {
    private boolean ordersLoaded = false;

    private final ProductService productService;
    private final CustomerService customerService;
    private  List<Order> orders = new ArrayList<>();
    private final OrderRepository orderRepository;
    public OrderService(ProductService productService, CustomerService customerService) {
        if (productService == null || customerService == null) {
            throw new IllegalArgumentException("❌ ProductService and CustomerService must not be null!");
        }
        this.productService = productService;
        this.customerService = customerService;
        this.orderRepository = new OrderRepository(productService, customerService);
    }


    public Order placeOrder(Customer customer, Scanner scanner) {
        List<CartItem> cartItems = customer.getCart().getCartItems();
        if (cartItems.isEmpty()) {
            System.out.println("Cannot place order: Cart is empty.");
            return null;
        }

        double total = calculateTotal(cartItems);

        if (customer.getBalance() < total) {
            System.out.printf("❌ Insufficient balance. Cart total: $%.2f | Your balance: $%.2f%n", total, customer.getBalance());
            System.out.print("Would you like to add funds to complete the purchase? (Y/N): ");
            String response = scanner.nextLine();
            if (!handleInsufficientFunds(response, customer, scanner, total)) {
                return null;
            }

                 }

        Order newOrder = new Order(cartItems, customer);
        newOrder.markAsPaid();
        customer.setBalance(customer.getBalance() - total);
        customer.getCart().clearCart();

        CartService cartService = new CartService(customer.getId(), customerService, productService);
        cartService.clearCartFileContents();

        orders.add(newOrder);
        customer.addOrder(newOrder);
        orderRepository.saveOrder(newOrder);


        return newOrder;
    }

//    public List<Order> getOrders() {
//        return orders;
//    }
//
public List<Order> getOrders() {
    return orderRepository.loadOrders();
}
    //
    private double calculateTotal(List<CartItem> cartItems) {
        double total = 0.0;
        for (CartItem item : cartItems) {
            double price = item.getProduct().getPrice();
            int quantity = item.getQuantity();
            total += price * quantity;
        }
        return total;
    }
    private boolean handleInsufficientFunds(String response, Customer customer, Scanner scanner, double total) {
        if (response.equalsIgnoreCase("Y")) {
            System.out.print("Enter amount to add: ");
            try {
                double additional = Double.parseDouble(scanner.nextLine());
                customer.setBalance(customer.getBalance() + additional);
                System.out.printf("💳 New balance: $%.2f%n", customer.getBalance());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid amount entered. Order cancelled.");
                return false;
            }

            if (customer.getBalance() < total) {
                System.out.println("🚫 Still insufficient. Please adjust your cart or add more funds.");
                return false;
            }
        } else {
            System.out.println("🕳️ Order cancelled. Feel free to come back anytime.");
            return false;
        }
        return true;
    }
    public void loadOrdersFromFile() {
        if (ordersLoaded) return;
        this.orders = orderRepository.loadOrders();
        ordersLoaded = true;
    }

public void validateManagers() {
    if (productService == null || customerService == null) {
        throw new IllegalStateException("❌ ProductManager or CustomerManager not initialized!");
    }
}
}

