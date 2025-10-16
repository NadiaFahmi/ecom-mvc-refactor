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
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private static final String FIELD_DELIMITER = "|";
    private static final String ITEM_DELIMITER = ";";
    private static final String ITEM_PART_DELIMITER = ",";


    private final ProductService productService;
    private final CustomerService customerService;
    private final File file = new File("orders.txt");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public OrderRepository(ProductService productService, CustomerService customerService) {
        this.productService = productService;
        this.customerService = customerService;
    }
    public List<Order> loadOrders() {
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
                //
                String status = parts[4];
                //

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

                Customer customer = customerService.findCustomerById(customerId);
                if (customer != null) {
                    Order order = new Order(cartItems, customer);
                    order.setOrderId(orderId);
                    order.setOrderDate(orderDate);
                    order.setStatus(status);
                    orders.add(order);
                    customer.addOrder(order);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Failed to load orders: " + e.getMessage());
        }

        return orders;
    }
    public void saveOrder(Order order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(order.getCustomer().getId()).append(FIELD_DELIMITER)
                    .append(order.getOrderId()).append(FIELD_DELIMITER)
                    .append(order.getOrderDate().format(formatter)).append(FIELD_DELIMITER);

            for (CartItem item : order.getCartItems()) {
                sb.append(item.getProduct().getId()).append(ITEM_PART_DELIMITER)
                        .append(item.getQuantity()).append("ITEM_DELIMITER");

            }
            sb.append(FIELD_DELIMITER).append(order.getStatus());

            writer.write(sb.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("⚠️ Failed to save order: " + e.getMessage());
        }
    }

}
