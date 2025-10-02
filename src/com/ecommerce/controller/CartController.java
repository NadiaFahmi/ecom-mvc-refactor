package com.ecommerce.controller;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.CartService;

public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {

        this.cartService = cartService;
    }


    public void handleAddToCart(int productId, int quantity) {
        boolean success = cartService.tryAddProductToCart(productId, quantity);
        if (success) {
            System.out.println("✅ Product added to cart.");
        } else {
            System.out.println("❌ Product not found.");
        }
    }

    public void handleRemoveFromCart(int productId) {
        cartService.removeFromCart(productId);
        System.out.println("🗑️ Product removed from cart.");
    }

    public void handleUpdateQuantity(int productId, int newQuantity) {
        cartService.updateCartItemQuantity(productId, newQuantity);
    }

    public void handleTotalPrice() {
        System.out.println("💰 Total: " + cartService.getTotalPrice());
    }

    public void handlelistCartItems() {
        cartService.listCartItems();
    }

    public void handleSaveCart() {
        cartService.saveCart();
    }

    public void handleLoadCart() {
        cartService.loadCart();
    }


}
