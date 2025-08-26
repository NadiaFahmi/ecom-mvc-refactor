package com.ecommerce.model.entities;


import java.util.*;


public class Cart {
    private List<CartItem> cartItems = new ArrayList<>();



    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            System.out.println("Invalid product or quantity.");
            return;
        }
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                System.out.println("Updated quantity for: " + product.getName());
                return;
            }
        }

        cartItems.add(new CartItem(product, quantity));
        System.out.println("Added to cart: " + product.getName() + " (x" + quantity + ")");
    }

    public CartItem getItemByProductId(int productId) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                return item;
            }
        }
        return null;
    }

    public void removeItem(int productId) {
        cartItems.removeIf(item -> item.getProduct().getId() == productId);
        System.out.println("Removed product ID " + productId + " from cart.");
    }

    public void listItems() {
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("Cart Contents:");
        for (CartItem item : cartItems) {
            Product p = item.getProduct();
            System.out.printf("- %s x%d = $%.2f%n", p.getName(), item.getQuantity(), p.getPrice() * item.getQuantity());
        }
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
        System.out.println("Cart cleared.");
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }


}

