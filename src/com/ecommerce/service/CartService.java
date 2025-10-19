package com.ecommerce.service;

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

    public boolean addProductToCart(Customer customer, int productId, int quantity) {
        Product product = productService.getProductById(productId);
        if (product == null) return false;

        customer.getCart().addItem(product, quantity);
        return true;
    }

    public void removeFromCart(Customer customer, int productId) {
        customer.getCart().removeItem(productId);
    }

    public double getTotalPrice(Customer customer) {

        return customer.getCart().getTotalPrice();
    }

    public void updateCartItemQuantity(Customer customer, int productId, int newQuantity) {
        CartItem item = customer.getCart().findItem(productId);
        if (item != null) {
            if (newQuantity > 0) {
                item.setQuantity(newQuantity);
                System.out.println("🔄 Quantity updated for product ID: " + productId);
            } else {
                customer.getCart().removeItem(productId);
                System.out.println("🗑️ Product removed from cart (quantity set to 0).");
            }
        } else {
            System.out.println("❌ Product not found in cart.");
        }
        saveCart(customer);
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

}


