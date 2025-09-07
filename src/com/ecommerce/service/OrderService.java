package com.ecommerce.service;

import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.model.entities.Product;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderService {
    private boolean ordersLoaded = false;

    private final ProductService productService;
    private final CustomerService customerService;
    private  List<Order> orders = new ArrayList<>();

    public OrderService(ProductService productService, CustomerService customerService) {
        if (productService == null || customerService == null) {
            throw new IllegalArgumentException("❌ ProductService and CustomerService must not be null!");
        }
        this.productService = productService;
        this.customerService = customerService;
    }


    public Order placeOrder(Customer customer, Scanner scanner) {
        List<CartItem> cartItems = customer.getCart().getCartItems();
        if (cartItems.isEmpty()) {
            System.out.println("Cannot place order: Cart is empty.");
            return null;
        }

        double total = calculateTotal(cartItems);

        if (customer.getBalance() < total) {
            System.out.printf("❌ Insufficient balance. Cart total: $%.2f | Your balance: $%.2f%n", total, customer.getBalance());
            System.out.print("Would you like to add funds to complete the purchase? (Y/N): ");
            String response = scanner.nextLine();

            if (response.equalsIgnoreCase("Y")) {
                System.out.print("Enter amount to add: ");
                try {
                    double additional = Double.parseDouble(scanner.nextLine());
                    customer.setBalance(customer.getBalance() + additional);
                    System.out.printf("💳 New balance: $%.2f%n", customer.getBalance());
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid amount entered. Order cancelled.");
                    return null;
                }

                if (customer.getBalance() < total) {
                    System.out.println("🚫 Still insufficient. Please adjust your cart or add more funds.");
                    return null;
                }
            } else {
                System.out.println("🕳️ Order cancelled. Feel free to come back anytime.");
                return null;
            }
        }

        Order newOrder = new Order(cartItems, customer);
        newOrder.markAsPaid();
        customer.setBalance(customer.getBalance() - total);
        customer.getCart().clearCart();

        CartService cartService = new CartService(customer.getId(), customerService, productService);
        cartService.clearCartFileContents();

        orders.add(newOrder);
        customer.addOrder(newOrder);
        appendOrderToFile(newOrder);

        printOrderDetails(newOrder, customer);

        return newOrder;
    }

    public List<Order> getOrders() {
        return orders;
    }

    private double calculateTotal(List<CartItem> cartItems) {
        double total = 0.0;
        for (CartItem item : cartItems) {
            double price = item.getProduct().getPrice();
            int quantity = item.getQuantity();
            total += price * quantity;
        }
        return total;
    }

    public void saveOrdersToFile() {
        File file = new File("orders.txt");
        if (!file.exists()) return;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 4);
                if (parts.length < 4) {
                    System.out.println("⚠️ Malformed line skipped: " + line);
                    continue;
                }

                int customerId = Integer.parseInt(parts[0]);
                String orderId = parts[1];
                LocalDateTime orderDate = LocalDateTime.parse(parts[2], formatter);
                String[] itemParts = parts[3].split(";");

                List<CartItem> cartItems = new ArrayList<>();
                for (String item : itemParts) {
                    if (item.trim().isEmpty()) continue;
                    String[] itemDetails = item.split(",");
                    int productId = Integer.parseInt(itemDetails[0]);
                    int quantity = Integer.parseInt(itemDetails[1]);

                    Product product = productService.getProductById(productId);
                    if (product != null) {
                        cartItems.add(new CartItem(product, quantity));
                    } else {
                        System.out.println("⚠️ Product not found for ID: " + productId);
                    }
                }

                Customer customer = customerService.getCustomerById(customerId);
                if (customer != null) {
                    Order order = new Order(cartItems, customer);
                    order.setOrderId(orderId);
                    order.setOrderDate(orderDate);
                    orders.add(order);
                    customer.addOrder(order);
                }
            }
            System.out.println("📂 Orders loaded successfully.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Failed to load orders: " + e.getMessage());
        }
    }

    public void loadOrdersFromFile(ProductService productService, CustomerService customerService, Customer loggedInCustomer) {
        if (ordersLoaded) return;
        orders.clear();
        ordersLoaded = true;

        File file = new File("orders.txt");
        if (!file.exists()) {
            System.out.println("📂 No existing orders found.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            Set<Integer> unknownCustomerIds = new HashSet<>();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 4);
                if (parts.length < 4) {
                    System.out.println("⚠️ Malformed line skipped: " + line);
                    continue;
                }

                int customerId = Integer.parseInt(parts[0]);
                Customer customer = customerService.getCustomerById(customerId);

                String orderId = parts[1];
                LocalDateTime orderDate = LocalDateTime.parse(parts[2], formatter);
                String[] itemParts = parts[3].split(";");

                List<CartItem> cartItems = new ArrayList<>();
                for (String item : itemParts) {
                    if (item.trim().isEmpty()) continue;
                    String[] itemDetails = item.split(",");
                    int productId = Integer.parseInt(itemDetails[0]);
                    int quantity = Integer.parseInt(itemDetails[1]);

                    Product product = productService.getProductById(productId);
                    if (product != null) {
                        cartItems.add(new CartItem(product, quantity));
                    } else {
                        System.out.println("⚠️ Product not found for ID: " + productId);

                    }
                }
                if (customer != null) {
                    Order order = new Order(cartItems, customer);
                    order.setOrderId(orderId);
                    order.setOrderDate(orderDate);
                    orders.add(order);
                    customer.addOrder(order);

                    if (loggedInCustomer == null ||
                            (loggedInCustomer.getEmail() != null &&
                                    customer.getEmail() != null &&
                                    customer.getEmail().trim().equalsIgnoreCase(loggedInCustomer.getEmail().trim()))) {

                        System.out.println("🚚 Loaded order: " + order.getOrderId() + " for customer: " + customer.getName());
                    }

                } else {
                    System.out.println("⚠️ Customer not found for ID: " + customerId);
                    unknownCustomerIds.add(customerId);
                }

            }
            if (!unknownCustomerIds.isEmpty()) {
                System.out.println("⚠️ Orders skipped for unknown customers: " + unknownCustomerIds);
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Failed to load orders: " + e.getMessage());
        }
        System.out.println("📂 Orders loaded successfully.");

    }

    public void appendOrderToFile(Order order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt", true))) {
            StringBuilder items = new StringBuilder();
            for (CartItem item : order.getCartItems()) {
                items.append(item.getProduct().getId())
                        .append(",")
                        .append(item.getQuantity())
                        .append(";");
            }

            String orderLine = String.format("%d|%s|%s|%s",
                    order.getCustomer().getId(),
                    order.getOrderId(),
                    order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    items.toString());

            writer.write(orderLine);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("❌ Failed to write order: " + e.getMessage());
        }
    }

    public void filterCustomerOrdersByDate(Customer customer, LocalDate date) {
        for (Order order : customer.getOrders()) {
            if (order.getOrderDate().toLocalDate().equals(date)) {
                displayOrderSummary(order);
            }
        }
    }
    private void displayOrderSummary(Order order) {
        System.out.println("🆔 Order ID: " + order.getOrderId());
        System.out.println("📅 Date: " + order.getOrderDate());
        System.out.println("💲 Total: $" + order.getOrder_total());
        System.out.println("📦 Status: " + order.getStatus());
        System.out.println("🛒 Items:");
        for (CartItem item : order.getCartItems()) {
            System.out.println(" - " + item.getProduct().getName() + " x" + item.getQuantity());
        }
        System.out.println("–––––––––––––––––––––––");
    }

    public void validateManagers() {
        if (productService == null || customerService == null) {
            throw new IllegalStateException("❌ ProductManager or CustomerManager not initialized!");
        }
    }

    public void printCustomerOrders(Customer customer) {
        List<Order> myOrders = customer.getOrders();

        if (myOrders.isEmpty()) {
            System.out.println("📭 You haven’t placed any orders yet.");
            return;
        }

        for (Order ord : myOrders) {
            System.out.println("🆔 Order ID: " + ord.getOrderId());
            System.out.println("🗓️ Date: " + ord.getOrderDate());
            System.out.println("🛒 Items:");
            for (CartItem item : ord.getCartItems()) {
                String name = item.getProduct().getName();
                double price = item.getProduct().getPrice() * item.getQuantity();
                System.out.printf(" - %s x%d = $%.2f%n", name, item.getQuantity(), price);
            }
            System.out.printf("💰 Total: $%.2f%n", ord.getOrder_total());
            System.out.println("📌 Status: " + ord.getStatus());
            System.out.println("------");
        }
    }
    public void printOrderDetails(Order order, Customer customer) {
        System.out.println("\n🎉 THANK YOU FOR YOUR PURCHASE, " + customer.getName() + "!");
        System.out.println("🆔 Order ID: " + order.getOrderId());
        System.out.println("📅 Date: " + order.getOrderDate());
        System.out.println("📦 Status: " + order.getStatus());
        System.out.println("🛒 Items:");

        double total = 0.0;
        for (CartItem item : order.getCartItems()) {
            String name = item.getProduct().getName();
            int quantity = item.getQuantity();
            double price = item.getProduct().getPrice() * quantity;
            total += price;
            System.out.printf(" - %s x%d = $%.2f%n", name, quantity, price);
        }

        System.out.printf("💰 TOTAL: $%.2f%n", total);
        System.out.printf("💳 Remaining Balance: $%.2f%n", customer.getBalance());
        System.out.println("✨ Your order is confirmed and being processed!\n");
    }
}

