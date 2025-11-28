package com.ecommerce.service;

import com.ecommerce.exception.CartItemNotFoundException;
import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Product;
import com.ecommerce.repository.CartRepository;

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
        List<CartItem> items = getLoadedItems(customer);
        CartItem item = findItem(items, productId);
        if (item != null) {

            increaseItemQuantity(item, quantity);

        } else {
            items.add(new CartItem(product,quantity));
        }
        saveCartItems(customer,items);

    }

    public void removeItemFromCart(Customer customer, int productId) {
        List<CartItem> items = getLoadedItems(customer);
        CartItem item = findItem(items, productId);
        if (item == null) {
            throw new CartItemNotFoundException("Product not found in cart.");
        }
        items.remove(item);
        saveCartItems(customer,items);
    }

    public void updateCartItemQuantity(Customer customer, int productId, int newQuantity) {
        List<CartItem> items= getLoadedItems(customer);
        CartItem item = findItem(items, productId);

        if (item == null) {
            throw new CartItemNotFoundException("Product ID " + productId + " not found in cart.");
        }
            if (newQuantity > 0) {
                item.setQuantity(newQuantity);
                saveCartItems(customer, items);
            } else {
                removeItemFromCart(customer, productId);
            }
        }


public void saveCartItems(Customer customer, List<CartItem> items) {
    cartRepository.saveCartItems(customer,items);

}

    public void clearCartFileContents(Customer customer) {
        cartRepository.clearCartFileContents(customer.getId());
    }

    public double calculateTotalPrice(Customer customer) {

        List<CartItem> items = getLoadedItems(customer);

        double total = 0.0;
        for (CartItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    public CartItem findItem(List<CartItem> items, int productId) {
        for (CartItem item :items) {
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
    public List<CartItem> getLoadedItems(Customer customer) {
        return cartRepository.loadCartItems(customer);
    }
}


