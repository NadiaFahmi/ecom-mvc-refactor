package com.ecommerce.service;

import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Product;
import com.ecommerce.repository.CartRepository;

import java.io.*;

public class CartService {
    private final ProductService productService;
    private final Customer customer;
    private final CartRepository cartRepository;

    public CartService(int customerId, CustomerService customerService, ProductService productService) {
        this.customer = customerService.findCustomerById(customerId);
        if (this.customer == null) {
            throw new IllegalArgumentException("❌ Customer not found for ID: " + customerId);
        }
        this.productService = productService;
        this.cartRepository = new CartRepository(productService);
    }


    public void addToCart(Product product, int quantity) {
        customer.getCart().addItem(product, quantity);
        saveCart();
    }

    public boolean tryAddProductToCart(int productId, int quantity) {
        Product product = productService.getProductById(productId);
        if (product == null) return false;

        addToCart(product, quantity);
        return true;
    }
    //
    public void removeFromCart(int productId) {
        customer.getCart().removeItem(productId);
        saveCart();
    }


    public void listCartItems() {
        customer.getCart().listItems();
    }
    public double getTotalPrice() {

        return customer.getCart().getTotalPrice();
    }



    private String getCartFilePath() {
        return "cart_customer_" + customer.getId() + ".txt";
    }

    public void updateCartItemQuantity(int productId, int newQuantity) {
        CartItem item = customer.getCart().findItemByProductId(productId);
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
        saveCart();
    }


    public void saveCart() {
        cartRepository.saveCart(customer);
    }


    public void loadCart() {
        cartRepository.loadCart(customer);

    }
    public void clearCart() {
        customer.getCart().clearCart();
        clearCartFileContents();
    }
    public void clearCartFileContents() {
        cartRepository.clearCartFileContents(customer.getId());


    }
    public  void deleteCartFile() {
        cartRepository.deleteCartFile(customer.getId(), customer.getUsername());
//
    }

}


