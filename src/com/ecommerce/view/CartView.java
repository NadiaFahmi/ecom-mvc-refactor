package com.ecommerce.view;

import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Product;

import java.util.List;

public class CartView {

    public List<CartItem> display(List<CartItem> items) {
        System.out.println("🛍️ Cart Contents:");
        for (CartItem item : items) {
            Product p = item.getProduct();
            double total = p.getPrice() * item.getQuantity();
            System.out.printf("- %s x%d = $%.2f%n", p.getName(), item.getQuantity(), total);
        }
        return items;
    }
    public void showSuccessMessage() {
        System.out.println("Product Added");
    }
    public void showErrorMessage(String message){
        System.out.println(message);
    }
    public void showTotalCartPrice(double total){
        System.out.println("Total cart price = "+"$"+total);
    }
    public void showRemovedMessage(){
        System.out.println("Product removed successfully");
    }
}
