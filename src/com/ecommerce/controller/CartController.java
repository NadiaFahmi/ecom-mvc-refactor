package com.ecommerce.controller;

import com.ecommerce.exception.CartItemNotFoundException;
import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.exception.InvalidQuantityException;
import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.service.CartService;
import com.ecommerce.view.CartView;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartController {
    private Logger logger = Logger.getLogger(CartController.class.getName());
    private final CartService cartService;
    private final CartView cartView;

    public CartController(CartService cartService, CartView cartView) {

        this.cartService = cartService;
        this.cartView = cartView;
    }

    public void addProductToCart(Customer customer) {

        try {
            int productId = cartView.productIdPrompt();
            if (productId == -1) {
                return;
            }
            int quantity = cartView.quantityPrompt();
            if (quantity == -1){
                return;
            }
            try {
                cartService.addProductToCart(customer, productId, quantity);
                logger.info("Product added successfully to cart");
                cartView.showSuccessMessage();
            } catch (InvalidQuantityException e) {
                logger.log(Level.SEVERE, "Exception encountered {0} ", e.getMessage());
                cartView.showErrorMessage(e.getMessage());
            } catch (CartItemNotFoundException e) {
                logger.log(Level.SEVERE, "Exception encountered {0} ", e.getMessage());
                cartView.showProductError();

            }catch(InvalidProductException e){
                cartView.showErrorMessage(e.getMessage());
            }
        }catch (NumberFormatException e) {
            cartView.showErrorMessage(e.getMessage());
        }
    }

    public void getCartItems(Customer customer) {
        Cart cart = new Cart(customer.getId());
        List<CartItem> items = cartService.getLoadedItems(cart);
        cartView.display(items);
    }


    public void removeProductFromCart(Customer customer) {

        try {
            int productId = cartView.productIdRemovePrompt();
            if (productId == -1) {
                return;
            }
            cartService.removeItemFromCart(customer, productId);
            cartView.showRemovedMessage();

        }catch (InvalidProductException e){
            logger.warning("No such item");
            cartView.showErrorMessage(e.getMessage());
        }
//        catch (CartItemNotFoundException e) {
//            logger.warning("No such item");
//            cartView.showErrorMessage(e.getMessage());
//        }
    }

    public void UpdateQuantity(Customer customer) {
        try {
            int productId = cartView.productIdPrompt();
            if (productId == -1) {
                return;
            }
            int newQuantity = cartView.newQuantityPrompt();

            cartService.updateCartItemQuantity(customer, productId, newQuantity);
            cartView.showQuantityUpdated();
        } catch (NumberFormatException e) {
            cartView.showErrorMessage("Invalid input. Please enter valid number");
        } catch (InvalidQuantityException e) {
            cartView.showErrorMessage(e.getMessage());
        } catch (CartItemNotFoundException e) {
            logger.warning("no item found");
            cartView.showErrorMessage(e.getMessage());
        }
        catch (InvalidProductException e){
            logger.warning("Product invalid");
            cartView.showErrorMessage("product not found");
        }

    }

    public void calculatePrice(Customer customer) {
        try {
            double totalPrice = cartService.calculateTotalPrice(customer);
            cartView.showTotalCartPrice(totalPrice);
        } catch (CartItemNotFoundException e) {
            logger.warning(e.getMessage());
            cartView.showErrorMessage(e.getMessage());
        }
    }
}




