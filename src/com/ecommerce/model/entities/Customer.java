package com.ecommerce.model.entities;


import java.util.ArrayList;
import java.util.List;

public class Customer extends User{

    private  List<Order> orders;
//
//    private static int  idCounter = 1;
    private double balance;
    private String address;
    private Cart cart;

    public Customer(int id, String name, String email, String password,
                    double balance, String address) {
        super(id, name, email, password, UserRole.CUSTOMER);
        this.balance = balance;
        this.address = address;
        this.orders  = new ArrayList<>();

        this.cart = new Cart();
        this.cart.setCustomerId(id);


    }

//    public Customer(String name, String email, String password,
//                    double balance, String address) {
//        super(idCounter++, name, email, password, UserRole.CUSTOMER);
//        this.balance = balance;
//        this.address = address;
//        this.cart = new Cart();
//        this.cart.setCustomerId(id);
//        this.orders  = new ArrayList<>();
//    }
//
//    public static void setIdCounter(int nextId) {
//        idCounter = nextId;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }



    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Cart getCart() {
        return cart;
    }



    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}

