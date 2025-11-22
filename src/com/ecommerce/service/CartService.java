package com.ecommerce.service;

import com.ecommerce.exception.CartItemNotFoundException;
import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Product;
import com.ecommerce.repository.CartRepository;

public class CartService {
    private final ProductService productService;
    private final CartRepository cartRepository;

    public CartService(ProductService productService, CartRepository cartRepository) {
        this.productService = productService;
        this.cartRepository = cartRepository;
    }

    public void addProductToCart(Customer customer, int productId, int quantity) {
        Product product = productService.getProductById(productId);
        if (product == null || quantity <= 0) {
            throw new InvalidProductException("Invalid product or quantity.");
        }
        Cart cart = customer.getCart();
        CartItem item = findItem(cart, productId);
        if (item != null) {

            increaseItemQuantity(item, quantity);

        } else {
            cart.getCartItems().add(new CartItem(product, quantity));
        }
        saveCart(customer);

    }

    public void removeProductFromCart(Cart cart, int productId) {

        CartItem item = findItem(cart, productId);
        if (item == null) {
            throw new CartItemNotFoundException("Product not found in cart.");
        }
        cart.getCartItems().remove(item);
    }

    public void updateCartItemQuantity(Customer customer, int productId, int newQuantity) {
        Cart cart = customer.getCart();
        CartItem item = findItem(cart, productId);

        if (item == null) {
            throw new CartItemNotFoundException("Product ID " + productId + " not found in cart.");
        }
            if (newQuantity > 0) {
                item.setQuantity(newQuantity);
                saveCart(customer);
            } else {
                removeProductFromCart(cart, productId);
            }
        }


    public void saveCart(Customer customer) {
        cartRepository.saveCart(customer);

    }

    public Cart loadCart(Customer customer) {
        return cartRepository.loadCart(customer);
    }

    public void clearCartFileContents(Customer customer) {
        cartRepository.clearCartFileContents(customer.getId());
    }

    public double calculateTotalPrice(Customer customer) {

        Cart cart = customer.getCart();

        double total = 0.0;
        for (CartItem item : cart.getCartItems()) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    public CartItem findItem(Cart cart, int productId) {
        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct() != null && item.getProduct().getId() == productId) {
                return item;
            }
        }
        return null;
    }

    public void increaseItemQuantity(CartItem item, int amount) {

        if (amount > 0) {
            item.setQuantity(item.getQuantity() + amount);

        }
    }
}


