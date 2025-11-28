package com.ecommerce.repository;

import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Product;
import com.ecommerce.service.ProductService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private static final String DELIMITER = ",";
    
    private final ProductService productService;

    public CartRepository(ProductService productService) {
        this.productService = productService;
    }

    private String getCartFilePath(int customerId) {
        return "cart_customer_" + customerId + ".txt";
    }


    public void clearCartFileContents(int customerId) {
        String filePath = getCartFilePath(customerId);
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.write("");
            System.out.println("🧹 Cart file contents cleared for ");
        } catch (IOException e) {
            System.out.println("⚠️ Failed to clear cart file: " + e.getMessage());
        }
    }


    public List<CartItem> loadCartItemsFromFile(int customerId) {
        List<CartItem> items = new ArrayList<>();
        String filePath = getCartFilePath(customerId);
        File file = new File(filePath);

        if (!file.exists()) return items;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(DELIMITER);
                if (parts.length == 2) {
                    int productId = Integer.parseInt(parts[0]);
                    int quantity = Integer.parseInt(parts[1]);

                    Product product = productService.getProductById(productId);
                    if (product != null) {
                        CartItem item = new CartItem(product, quantity);
                        items.add(item);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("❌ Error loading cart: " + e.getMessage());
        }

        return items;
    }


    public List<CartItem> loadCartItems(Customer customer) {
        List<CartItem> fileItems = loadCartItemsFromFile(customer.getId());
        List<CartItem> resolvedItems = new ArrayList<>();

        for (CartItem item : fileItems) {
            Product product = productService.getProductById(item.getProduct().getId());
            if (product != null) {
                resolvedItems.add(new CartItem(product, item.getQuantity()));
            }
        }
        return resolvedItems;
    }


    public void saveCartItems(Customer customer, List<CartItem> items) {
        String filePath = getCartFilePath(customer.getId());
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (CartItem item : items) {
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
}
