package com.ecommerce.model.entities;


import java.util.ArrayList;
import java.util.List;

public class Cart {

    private int CustomerId;
    List<CartItem> items= new ArrayList<>();

    public Cart(int customerId){
        this.CustomerId = customerId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}

