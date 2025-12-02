package com.ecommerce.model.entities;



public class Cart {

    private int id;


    public Cart(int customerId){
        this.id = customerId;
    }

    public int getId() {
        return id;
    }


}

