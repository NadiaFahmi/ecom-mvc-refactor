package com.ecommerce.service;

import com.ecommerce.exception.CartItemNotFoundException;
import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Product;
import com.ecommerce.repository.CartRepository;

import java.util.Collections;
import java.util.List;

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

        Cart cart =cartRepository.findByCustomerId(customer.getId());
        if (cart == null) {
            cart = cartRepository.createCartForCustomer(customer.getId());
        }
        List<CartItem> items = getLoadedItems(cart);

        CartItem item = findItem(items, productId);
        if (item != null) {
            increaseItemQuantity(item, quantity);
        } else {
            CartItem newItem = new CartItem(
                    cart.getId(),
                    product.getId(),
                    product.getName(),
                    quantity,
                    product.getPrice()
            );
            items.add(newItem);
        }

        cartRepository.saveCartItems(cart, items);
    }

    public void removeItemFromCart(Customer customer, int productId) {
        Cart cart =cartRepository.findByCustomerId(customer.getId());
        List<CartItem> items = getLoadedItems(cart);
        CartItem item = findItem(items, productId);
        if (item == null) {
            throw new CartItemNotFoundException("Product not found in cart.");
        }
        items.remove(item);
        saveCartItems(cart, items);
    }

    public void updateCartItemQuantity(Customer customer, int productId, int newQuantity) {
        Cart cart =cartRepository.findByCustomerId(customer.getId());
        List<CartItem> items = getLoadedItems(cart);
        CartItem item = findItem(items, productId);

        if (item == null) {
            throw new CartItemNotFoundException("Product ID " + productId + " not found in cart.");
        }
        if (newQuantity > 0) {
            item.setQuantity(newQuantity);
            saveCartItems(cart, items);
        } else {
            removeItemFromCart(customer, productId);
        }
        }


public void saveCartItems(Cart cart, List<CartItem> items) {
    cartRepository.saveCartItems(cart, items);

}
    public void clearCartFileContents(Cart cart) {
        cartRepository.clearCartFileContents(cart);

    }

    public double calculateTotalPrice(Customer customer) {

        Cart cart= cartRepository.findByCustomerId(customer.getId());
        List<CartItem> items = cartRepository.loadCartItemsFromFile(cart);

        double total = 0.0;
        for (CartItem item : items) {
            Product product = productService.getProductById(item.getProductId());
            if (product != null) {
                total += product.getPrice() * item.getQuantity();
            }
        }
        return total;
    }

    public CartItem findItem(List<CartItem> items, int productId) {
        for (CartItem item :items) {
            if (item.getProductId() == productId) {
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
public List<CartItem> getLoadedItems(Cart cart) {
    return cartRepository.loadCartItemsFromFile(cart);
}
    public Cart getCartForCustomer(Customer customer) {

        Cart  cart = cartRepository.findByCustomerId(customer.getId());
        if (cart == null) {
            cart = new Cart(customer.getId());
            cartRepository.saveCart(cart);
        }
        return cart;
    }

}


