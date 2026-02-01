package main.java.controller;

import main.java.exception.CartItemNotFoundException;
import main.java.exception.EmptyCartException;
import main.java.exception.InvalidProductException;
import main.java.exception.InvalidQuantityException;
import main.java.model.entities.Cart;
import main.java.model.entities.CartItem;
import main.java.model.entities.Customer;
import main.java.service.CartService;
import main.java.view.CartView;

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
        }catch (EmptyCartException e){
            logger.log(Level.SEVERE, "Exception encountered {0} ", e.getMessage());
            cartView.showErrorMessage(e.getMessage());
        }
    }

    public void getCartItems(Customer customer) {
        //

        Cart cart = new Cart(customer.getId());
        try {
            List<CartItem> items = cartService.getLoadedItems(cart);
            if(items == null || items.isEmpty()){
                cartView.showErrorMessage("No items found in your cart");
            }else {

                cartView.display(items);
            }
        }catch (EmptyCartException e){
            cartView.showErrorMessage(e.getMessage());
        }
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
        catch (EmptyCartException e) {
            logger.warning("No such item");
            cartView.showErrorMessage(e.getMessage());
        }
    }

    public void UpdateQuantity(Customer customer){
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
            } catch (InvalidProductException e) {
                logger.warning("Product invalid");
                cartView.showErrorMessage("product not found");
            } catch (EmptyCartException e) {
                cartView.showErrorMessage(e.getMessage());
            }

    }

    public void calculatePrice(Customer customer) {
        try {
            double totalPrice = cartService.calculateCartTotalPrice(customer);
            cartView.showTotalCartPrice(totalPrice);
        } catch (EmptyCartException e) {
            logger.warning(e.getMessage());
            cartView.showErrorMessage(e.getMessage());
        }
    }
}




