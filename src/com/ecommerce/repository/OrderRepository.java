package com.ecommerce.repository;

import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.model.entities.Product;
import com.ecommerce.service.CustomerService;
import com.ecommerce.service.ProductService;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OrderRepository {

    private static final String FIELD_DELIMITER = "|";
    private static final String ITEM_DELIMITER = ";";
    private static final String ITEM_PART_DELIMITER = ",";

    private final ProductService productService;
    private final File file = new File("orders.txt");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public OrderRepository(ProductService productService) {
        this.productService = productService;
    }

    public List<Order> loadOrders(Function<Integer, Customer> customerResolver) {
        List<Order> orders = new ArrayList<>();
        if (!file.exists()) return orders;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 5);
                if (parts.length < 5) continue;

                int customerId = Integer.parseInt(parts[0]);
                String orderId = parts[1];
                LocalDateTime orderDate = LocalDateTime.parse(parts[2], formatter);
                String[] itemParts = parts[3].split(ITEM_DELIMITER);
                String status = parts[4];

                List<CartItem> cartItems = new ArrayList<>();
                for (String item : itemParts) {
                    if (item.trim().isEmpty()) continue;
                    String[] itemDetails = item.split(ITEM_PART_DELIMITER);
                    int productId = Integer.parseInt(itemDetails[0]);
                    int quantity = Integer.parseInt(itemDetails[1]);

                    Product product = productService.getProductById(productId);
                    if (product != null) {
                        cartItems.add(new CartItem(product, quantity));
                    }
                }

                Customer customer = customerResolver.apply(customerId);
                if (customer != null) {
                    Order order = new Order(cartItems, customer);
                    order.setOrderId(orderId);
                    order.setOrderDate(orderDate);
                    order.setStatus(status);
                    orders.add(order);

                    customer.addOrder(order); // optional, depends on your flow
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Failed to load orders: " + e.getMessage());
        }

        return orders;
    }

    public void saveOrder(Order order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            StringBuilder orderLineBuilder = new StringBuilder();
            orderLineBuilder.append(order.getCustomer().getId()).append(FIELD_DELIMITER)
                    .append(order.getOrderId()).append(FIELD_DELIMITER)
                    .append(order.getOrderDate().format(formatter)).append(FIELD_DELIMITER);

            for (CartItem item : order.getCartItems()) {
                orderLineBuilder.append(item.getProduct().getId()).append(ITEM_PART_DELIMITER)
                        .append(item.getQuantity()).append(ITEM_DELIMITER);
            }

            orderLineBuilder.append(FIELD_DELIMITER).append(order.getStatus());
            writer.write(orderLineBuilder.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("⚠️ Failed to save order: " + e.getMessage());
        }
    }
    public List<Order> getOrdersByCustomerEmail(String email) {
        List<Order> orders = new ArrayList<>();

        if (!file.exists()) return orders;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 11) continue;

                // Match customer email
                String customerEmail = parts[3];
                if (!customerEmail.equalsIgnoreCase(email)) continue;

                // Reconstruct Customer
                int customerId = Integer.parseInt(parts[1]);
                String name = parts[2];
                String password = parts[4];
                double balance = Double.parseDouble(parts[5]);
                String address = parts[6];
                Customer customer = new Customer(customerId, name, customerEmail, password, balance, address);

                // Reconstruct Order
                String orderId = parts[0];
                LocalDateTime orderDate = LocalDateTime.parse(parts[7], formatter);
                String status = parts[8];
                String itemsRaw = parts[9];
                double total = Double.parseDouble(parts[10]);

                List<CartItem> cartItems = new ArrayList<>();
                if (!itemsRaw.isBlank()) {
                    String[] itemParts = itemsRaw.split(ITEM_DELIMITER);
                    for (String item : itemParts) {
                        String[] itemFields = item.split(ITEM_PART_DELIMITER);
                        if (itemFields.length < 2) continue;

                        int productId = Integer.parseInt(itemFields[0]);
                        try {

                            int quantity = Integer.parseInt(itemFields[1]);
                            Product product = productService.getProductById(productId);
                            if (product != null) {
                                cartItems.add(new CartItem(product, quantity));
                            } else {
                                System.out.println("⚠️ Product not found: " + productId);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("⚠️ Invalid quantity for product " + productId);
                        }
                    }
                }

                Order order = new Order(cartItems, customer);
                order.setOrderId(orderId);
                order.setOrderDate(orderDate);
                order.setStatus(status);
                order.setOrder_total(total);

                orders.add(order);
            }
        } catch (IOException | NumberFormatException | DateTimeParseException e) {
            e.printStackTrace();
        }

        return orders;
    }

}
