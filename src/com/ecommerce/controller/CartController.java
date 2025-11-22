package com.ecommerce.controller;

import com.ecommerce.exception.CartItemNotFoundException;
import com.ecommerce.exception.InvalidProductException;
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
            cartView.showSuccessMessage();
        } catch (InvalidProductException e) {
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
            cartView.showRemovedMessage();
            saveCart(customer);
        }catch (CartItemNotFoundException e){
            cartView.showErrorMessage(e.getMessage());
        }
    }

    public void updateQuantity(Customer customer, int productId, int newQuantity) {
        cartService.updateCartItemQuantity(customer,productId, newQuantity);
    }

    public void calculatePrice(Customer customer) {
        double totalPrice = cartService.calculateTotalPrice(customer);
        cartView.showTotalCartPrice(totalPrice);
    }


    public void saveCart(Customer customer) {
        cartService.saveCart(customer);
    }

    public void loadCart(Customer customer) {
        cartService.loadCart(customer);
    }


}
