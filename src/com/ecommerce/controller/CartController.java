package com.ecommerce.controller;

import com.ecommerce.exception.CartItemNotFoundException;
import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.CartService;
import com.ecommerce.view.CartView;

import java.util.List;

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
    public void getCartItems(Customer customer) {
        Cart cart = new Cart(customer.getId());
        List<CartItem> items = cartService.getLoadedItems(cart);
        cartView.display(items);
    }


    public void removeProductFromCart(Customer customer, int productId){

        try{
            cartService.removeItemFromCart(customer, productId);
            cartView.showRemovedMessage();

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



}
