package com.ecommerce.model.entities;


import java.util.*;


public class Cart {
    private List<CartItem> cartItems = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        //
        if (!isValid(product, quantity)) {
            System.out.println("⚠️ Invalid product or quantity.");
            return;
        }
        CartItem existingItem = findItem(product.getId());
        if (existingItem != null) {
            existingItem.increaseQuantity(quantity);
            System.out.println("🔄 Updated quantity for: " + product.getName());
        } else {
            cartItems.add(new CartItem(product, quantity));
            System.out.println("✅ Added to cart: " + product.getName() + " (x" + quantity + ")");

        }

    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }


    public CartItem findItem(int productId) {

        System.out.println("🔎 Looking for product ID: " + productId);

        for (CartItem item : cartItems) {
            if (item.getProduct() == null) {
                System.out.println("⚠️ CartItem has null product.");
                continue;
            }
            System.out.println("🔍 Checking item with product ID: " + item.getProduct().getId());
            if (item.getProduct().getId() == productId) {
                System.out.println("✅ Match found for product ID: " + productId);
                return item;
            }

            System.out.println("❌ No matching product ID found in cart.");
        }
        return null;

    }

    public void removeItem(int productId) {
        CartItem item = findItem(productId);

        if (item == null) {
            System.out.println("⚠️ Product ID " + productId + " not found in cart.");
            return;
        }

        cartItems.remove(item);
        System.out.println("🗑️ Removed product: " + item.getProduct().getName());
    }

    public void clearCart() {
        cartItems.clear();
        System.out.println("Cart cleared.");
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    private boolean isValid(Product product, int quantity) {
        return product != null && quantity > 0;
    }
}

