package com.ecommerce.view;

import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Product;

public class CartView {

    public void display(Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("🛒 Cart is empty.");
            return;
        }

        System.out.println("🛍️ Cart Contents:");
        for (CartItem item : cart.getCartItems()) {
            Product p = item.getProduct();
            double total = p.getPrice() * item.getQuantity();
            System.out.printf("- %s x%d = $%.2f%n", p.getName(), item.getQuantity(), total);
        }
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
