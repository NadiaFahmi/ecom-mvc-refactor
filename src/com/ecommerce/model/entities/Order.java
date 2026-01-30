package com.ecommerce.model.entities;

import java.time.LocalDateTime;
import java.util.*;

public class Order {

private int orderId;
    private int customerId;
    private double orderTotal;
    private String status;
    private LocalDateTime orderDate;

    public Order(int orderId, int customerId, double orderTotal, String status, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderTotal = orderTotal;
        this.status = status;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
}
