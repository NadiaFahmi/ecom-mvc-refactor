package com.ecommerce.controller;

import com.ecommerce.exception.CartItemNotFoundException;
import com.ecommerce.exception.InvalidProductQuantityException;
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

    public void addProductToCart(Customer customer, int productId, int quantity) {
        try {
            cartService.addProductToCart(customer, productId, quantity);
            cartView.showSuccessMessage("Product added successfully!");
        } catch (InvalidProductQuantityException e) {
            cartView.showErrorMessage(e.getMessage());
        }
    }
    public void listCartItems(Customer customer) {
        Cart cart = cartService.loadCart(customer);
        cartView.display(cart);
    }

    public void removeProductFromCart(Customer customer, int productId){

        try{
            cartService.removeProductFromCart(customer.getCart(), productId);
            System.out.println("Product removed from cart.");
            saveCart(customer);
        }catch (CartItemNotFoundException e){
            cartView.showErrorMessage("No such Product in cart");
        }
    }

    public void updateQuantity(Customer customer, int productId, int newQuantity) {
        cartService.updateCartItemQuantity(customer,productId, newQuantity);
    }

    public void calculatePrice(Customer customer) {
        System.out.println("💰 Total: " + cartService.calculateTotalPrice(customer));
    }


    public void saveCart(Customer customer) {
        cartService.saveCart(customer);
    }

    public void loadCart(Customer customer) {
        cartService.loadCart(customer);
    }


}
