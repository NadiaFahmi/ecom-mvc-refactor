package com.ecommerce.controller;

import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.CartService;
import com.ecommerce.view.CartView;

public class CartController {

    private final CartService cartService;
    private final CartView cartView;

    public CartController(CartService cartService, CartView cartView) {

        this.cartService = cartService;
        this.cartView = cartView;
    }

    public void addToCart(Customer customer, int productId, int quantity) {
        boolean success = cartService.addProductToCart(customer,productId, quantity);
        if (success) {
            System.out.println("✅ Product added to cart.");
            cartService.saveCart(customer);
        } else {
            System.out.println("❌ Product not found.");
        }
    }
    public void listCartItems(Customer customer) {
        Cart cart = cartService.loadCart(customer);
        cartView.display(cart);
    }

    public void removeFromCart(Customer customer, int productId) {
        cartService.removeFromCart(customer,productId);
        System.out.println("🗑️ Product removed from cart.");
    }

    public void updateQuantity(Customer customer, int productId, int newQuantity) {
        cartService.updateCartItemQuantity(customer,productId, newQuantity);
    }

    public void totalPrice(Customer customer) {
        System.out.println("💰 Total: " + cartService.getTotalPrice(customer));
    }


    public void saveCart(Customer customer) {
        cartService.saveCart(customer);
    }

    public void loadCart(Customer customer) {
        cartService.loadCart(customer);
    }


}
