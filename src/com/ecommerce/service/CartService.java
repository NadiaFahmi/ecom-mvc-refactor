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
        this.customer = customerService.getCustomerById(customerId);
        if (this.customer == null) {
            throw new IllegalArgumentException("❌ Customer not found for ID: " + customerId);
        }
        this.productService = productService;
        this.cartRepository = new CartRepository(productService);
    }

    public void addToCart(Product product, int quantity) {
        customer.getCart().addItem(product, quantity);
    }

    public void removeFromCart(int productId) {
        customer.getCart().removeItem(productId);
    }


    public void listCartItems() {
        customer.getCart().listItems();
    }
    public double getTotalPrice() {
        return customer.getCart().getTotalPrice();
    }


    public void clearCart(Customer customer) {
        customer.getCart().clearCart();
    }

    public boolean tryAddProductToCart(int productId, int quantity) {
        Product product = productService.getProductById(productId);
        if (product == null) return false;

        addToCart(product, quantity);
        return true;
    }

    private String getCartFilePath() {
        return "cart_customer_" + customer.getId() + ".txt";
    }

    public void updateCartItemQuantity(int productId, int newQuantity) {
        CartItem item = customer.getCart().getItemByProductId(productId);
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
//        String filePath = getCartFilePath();
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//            for (CartItem item : customer.getCart().getCartItems()) {
//                writer.write(item.getProduct().getId() + "," + item.getQuantity());
//                writer.newLine();
//            }
//            System.out.println("✅ Cart saved for " + customer.getName());
//        } catch (IOException e) {
//            System.out.println("⚠️ Error saving cart: " + e.getMessage());
//        }
    }



        public void loadCartFromFile() {
            cartRepository.loadCart(customer);
//            String filePath = getCartFilePath();
//
//            File file = new File(filePath);
//
//            if (!file.exists()) {
//                System.out.println("No saved cart found for " + customer.getName());
//                return;
//            }
//
//            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    String[] parts = line.trim().split(",");
//                    if (parts.length == 2) {
//                        int productId = Integer.parseInt(parts[0]);
//                        int quantity = Integer.parseInt(parts[1]);
//
//                        Product product = productService.getProductById(productId);
//                        if (product != null) {
//                            customer.getCart().addItem(product, quantity);
//                        }
//                    }
//                }
//                System.out.println("🛒 Cart loaded for " + customer.getName());
//            } catch (IOException | NumberFormatException e) {
//                System.out.println("❌ Error loading cart: " + e.getMessage());
//                System.out.println("📁 Tried to load from: " + filePath);
//            }
    }

    public void clearCartFileContents() {
        cartRepository.clearCartFileContents(customer.getId(), customer.getName());
//        String filePath = getCartFilePath();
//        try (PrintWriter writer = new PrintWriter(filePath)) {
//            writer.write("");
//            System.out.println("🧹 Cart file contents cleared for " + customer.getName());
//        } catch (IOException e) {
//            System.out.println("⚠️ Failed to clear cart file: " + e.getMessage());
//        }
    }
    public  void deleteCartFile(Customer customer) {
//        cartRepository.deleteCartFile(customer.getId(), customer.getUsername());
//        String filename = "cart_" + customer.getUsername() + ".txt";
//        File cartFile = new File(filename);
//
//        if (cartFile.exists()) {
//            boolean deleted = cartFile.delete();
//            if (deleted) {
//                System.out.println("Cart file deleted successfully.");
//            } else {
//                System.out.println("Failed to delete cart file.");
//            }
//        } else {
//            System.out.println("Cart file does not exist.");
//        }
    }

}


