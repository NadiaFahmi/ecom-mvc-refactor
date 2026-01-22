package com.ecommerce.repository;

import com.ecommerce.exception.CartItemNotFoundException;
import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private static final String DELIMITER = ",";

public CartRepository(){


}
    private String getCartFilePath(int customerId) {
        return "cart_customer_" + customerId + ".txt";
    }

    public List<CartItem> loadCartItemsFromFile(Cart cart) {
        List<CartItem> items = new ArrayList<>();

        if(cart == null){
            throw new CartItemNotFoundException("No items in cart.");
        }
        File file = new File(getCartFilePath(cart.getCustomerId()));

        if (!file.exists()) return items;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(DELIMITER);
                if (parts.length == 4) { // productId,name,quantity,price
                    int productId = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);
                    items.add(new CartItem(
//                            cart.getCustomerId(),
                            productId, name, quantity, price));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to load cart items: " + e.getMessage());
        }
        return items;
    }

    // --- Save Items ---
    public void saveCartItems(Cart cart, List<CartItem> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getCartFilePath(cart.getCustomerId())))) {
            for (CartItem item : items) {
                writer.write(item.getProductId() + DELIMITER
                        + item.getName() + DELIMITER
                        + item.getQuantity() + DELIMITER
                        + item.getPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save cart items: " + e.getMessage());
        }
    }

    // --- Initialize Cart File ---
    public void saveCart(Cart cart) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getCartFilePath(cart.getCustomerId())))) {
        } catch (IOException e) {
            System.err.println("Failed to initialize cart file: " + e.getMessage());
        }
    }

    // --- Clear Cart ---
    public void clearCartFileContents(Cart cart) {
        try (PrintWriter writer = new PrintWriter(getCartFilePath(cart.getCustomerId()))) {
            writer.write("");
            System.out.println("Cart file cleared for cartId=" + cart.getCustomerId());
        } catch (IOException e) {
            System.err.println("Failed to clear cart file: " + e.getMessage());
        }
    }

    public Cart findByCustomerId(int customerId) {
        File file = new File(getCartFilePath(customerId));
        if (file.exists()) {
            return new Cart(customerId);
        }
        return null;
    }
    public Cart createCartForCustomer(int customerId) {
        Cart cart = new Cart(customerId);   // cartId = customerId
        saveCart(cart);                     // initialize empty cart file
        return cart;
    }
}
