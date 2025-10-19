package com.ecommerce.repository;

import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Product;
import com.ecommerce.service.ProductService;

import java.io.*;

public class CartRepository {
    private static final String DELIMITER = ",";
    
    private final ProductService productService;

    public CartRepository(ProductService productService) {
        this.productService = productService;
    }

    private String getCartFilePath(int customerId) {
        return "cart_customer_" + customerId + ".txt";
    }

//    public void saveCart(Customer customer) {
//        String filePath = getCartFilePath(customer.getId());
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//            for (CartItem item : customer.getCart().getCartItems()) {
//                writer.write(item.getProduct().getId() + DELIMITER
//                        + item.getQuantity());
//                writer.newLine();
//            }
//            System.out.println("✅ Cart saved for " + customer.getName());
//        } catch (IOException e) {
//            System.out.println("⚠️ Error saving cart: " + e.getMessage());
//        }
//    }
//
//    public void loadCart(Customer customer) {
//        String filePath = getCartFilePath(customer.getId());
//        File file = new File(filePath);
//
//        if (!file.exists()) {
//            System.out.println("📁 No saved cart found for " + customer.getName());
//            return;
//        }
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.trim().split(DELIMITER);
//                if (parts.length == 2) {
//                    int productId = Integer.parseInt(parts[0]);
//                    int quantity = Integer.parseInt(parts[1]);
//
//                    Product product = productService.getProductById(productId);
//                    if (product != null) {
//                        customer.getCart().addItem(product, quantity);
//                    }
//                }
//            }
//            System.out.println("🛒 Cart loaded for " + customer.getName());
//        } catch (IOException | NumberFormatException e) {
//            System.out.println("❌ Error loading cart: " + e.getMessage());
//        }
//    }

    public void clearCartFileContents(int customerId) {
        String filePath = getCartFilePath(customerId);
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.write("");
            System.out.println("🧹 Cart file contents cleared for ");
        } catch (IOException e) {
            System.out.println("⚠️ Failed to clear cart file: " + e.getMessage());
        }
    }
    public void saveCart(Customer customer) {
        Cart cart = customer.getCart();
        String filePath = getCartFilePath(customer.getId());
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (CartItem item : cart.getCartItems()) {
                int productId = item.getProduct().getId();
                int quantity = item.getQuantity();
                writer.write(productId + DELIMITER + quantity);
                writer.newLine();
            }
            System.out.println("💾 Cart saved for " + customer.getName());
        } catch (IOException e) {
            System.out.println("❌ Error saving cart: " + e.getMessage());
        }
    }

    public Cart loadCart(Customer customer) {
        Cart cart = new Cart();
        String filePath = getCartFilePath(customer.getId());
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("📁 No saved cart found for " + customer.getName());
            return cart; // return empty cart
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(DELIMITER);
                if (parts.length == 2) {
                    int productId = Integer.parseInt(parts[0]);
                    int quantity = Integer.parseInt(parts[1]);

                    Product product = productService.getProductById(productId);
                    if (product != null) {
                        cart.addItem(product, quantity);
                    }
                }
            }
            System.out.println("🛒 Cart loaded for " + customer.getName());
        } catch (IOException | NumberFormatException e) {
            System.out.println("❌ Error loading cart: " + e.getMessage());
        }

        return cart;
    }
}
