package com.ecommerce.model.entities;

import java.time.LocalDateTime;
import java.util.*;

public class Order {

    private String orderId;
    private List<CartItem> cartItems;
    private Customer customer;
    private double order_total;
    private String status;
    private LocalDateTime orderDate;

    public Order(List<CartItem> cartItems, Customer customer) {

        this.orderId = UUID.randomUUID().toString();
        this.cartItems = new ArrayList<>(cartItems);
        this.customer = customer;
        this.order_total = calculateTotal();
        this.status = "pending";
        this.orderDate = LocalDateTime.now();
    }

    private double calculateTotal() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }



    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getOrder_total() {
        return order_total;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public void markAsPaid() {
        if (this.status.equalsIgnoreCase("pending")) {
            this.status = "paid";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("🆔 Order ID: ").append(orderId)
                .append(" | 📅 Date: ").append(orderDate)
                .append(" | 💵 Total: ").append(order_total)
                .append(" | 📦 Status: ").append(status);
        return sb.toString();
    }

}
