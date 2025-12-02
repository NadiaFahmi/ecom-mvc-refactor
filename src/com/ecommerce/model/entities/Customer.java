package com.ecommerce.model.entities;


public class Customer extends User{

    private double balance;
    private String address;


    public Customer(int id, String name, String email, String password,
                    double balance, String address) {
        super(id, name, email, password, UserRole.CUSTOMER);
        this.balance = balance;
        this.address = address;

    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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

