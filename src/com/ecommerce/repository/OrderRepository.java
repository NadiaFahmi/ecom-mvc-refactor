package com.ecommerce.repository;

import com.ecommerce.exception.NoOrdersFoundException;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.model.entities.Product;
import com.ecommerce.service.ProductService;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

public class OrderRepository {

    private static final String FIELD_DELIMITER = "|";
    private static final String ITEM_DELIMITER = ";";
    private static final String ITEM_PART_DELIMITER = ",";

    private final ProductService productService;
    private final File file = new File("orders.txt");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private int idCounter = 1;
    private Map<Integer, Order> orderMap = new HashMap<>();
    private Map<Integer, List<CartItem>> orderItemsMap = new HashMap<>();

    public OrderRepository(ProductService productService) {
        this.productService = productService;
        initializeIdCounter();
    }

    private void initializeIdCounter() {
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int maxId = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 5);
                if (parts.length < 5) continue;
                int orderId = Integer.parseInt(parts[1]);
                if (orderId > maxId) {
                    maxId = orderId;
                }
            }
            idCounter = maxId + 1;
        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Failed to initialize idCounter: " + e.getMessage());
        }
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
                int orderId = Integer.parseInt(parts[1]);
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
                        cartItems.add(new CartItem(
                                customerId,
                                product.getId(),
                                product.getName(),
                                quantity,
                                product.getPrice()
                        ));
                    }

                }

                double total = cartItems.stream()
                        .mapToDouble(ci -> {
                            Product product = productService.getProductById(ci.getProductId());
                            return product != null ? product.getPrice() * ci.getQuantity() : 0.0;
                        })
                        .sum();

                Order order = new Order(orderId, customerId, total, status, orderDate);
                orders.add(order);
                orderMap.put(orderId, order);
                orderItemsMap.put(orderId, cartItems);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Failed to load orders: " + e.getMessage());
        }

        return orders;
    }

    public Order saveOrder(Customer customer, List<CartItem> cartItems, double total) {
        int newOrderId = idCounter++;
        Order order = new Order(newOrderId, customer.getId(), total, "pending", LocalDateTime.now());

        orderMap.put(newOrderId, order);
        orderItemsMap.put(newOrderId, new ArrayList<>(cartItems));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            StringBuilder orderLineBuilder = new StringBuilder();
            orderLineBuilder.append(customer.getId()).append(FIELD_DELIMITER)
                    .append(order.getOrderId()).append(FIELD_DELIMITER)
                    .append(order.getOrderDate().format(formatter)).append(FIELD_DELIMITER);

            for (CartItem item : cartItems) {
                orderLineBuilder.append(item.getProductId()).append(ITEM_PART_DELIMITER)
                        .append(item.getQuantity()).append(ITEM_DELIMITER);
            }

            orderLineBuilder.append(FIELD_DELIMITER).append(order.getStatus());
            writer.write(orderLineBuilder.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("⚠️ Failed to save order: " + e.getMessage());
        }

        return order;
    }

    }



