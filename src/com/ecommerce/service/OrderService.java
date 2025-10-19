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
//


    private List<Order> orders = new ArrayList<>();
    private CartService cartService;
    private  OrderRepository orderRepository;
    private boolean ordersLoaded = false;


    public OrderService(
            ProductService productService,
            CustomerService customerService,
            CartService cartService) {
        this.cartService = cartService;

        this.orderRepository = new OrderRepository(productService, customerService);
    }

    public Order placeOrder(Customer customer) {
        List<CartItem> cartItems = customer.getCart().getCartItems();
        if (cartItems.isEmpty()) return null;

        double total = calculateTotal(cartItems);
        if (customer.getBalance() < total) return null;

        Order newOrder = new Order(cartItems, customer);
        newOrder.markAsPaid();
        customer.setBalance(customer.getBalance() - total);
        customer.getCart().clearCart();

        orderRepository.saveOrder(newOrder);
        customer.addOrder(newOrder);
        orders.add(newOrder);
        cartService.clearCartFileContents(customer);
        return newOrder;
    }
    public boolean tryAddFunds(Customer customer, double amount, double requiredTotal) {
        customer.setBalance(customer.getBalance() + amount);
        return customer.getBalance() >= requiredTotal;
    }

    public List<Order> getOrders() {
        if (!ordersLoaded) {
            List<Order> loadedOrders = orderRepository.loadOrders();
            orders.addAll(loadedOrders);
            ordersLoaded = true;
        }
        return orders;
    }

    public double calculateTotal(List<CartItem> cartItems) {
        double total = 0.0;
        for (CartItem item : cartItems) {
            double price = item.getProduct().getPrice();
            int quantity = item.getQuantity();
            total += price * quantity;
        }
        return total;
    }

}

