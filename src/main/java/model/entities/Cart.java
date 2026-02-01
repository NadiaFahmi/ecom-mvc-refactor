package main.java.model.entities;


import java.util.ArrayList;
import java.util.List;

public class Cart {

    private int customerId;
    List<CartItem> items= new ArrayList<>();

    public Cart(int customerId){
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}

