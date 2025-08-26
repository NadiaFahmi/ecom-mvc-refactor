package com.ecommerce.service;

import com.ecommerce.model.entities.Admin;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.model.entities.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import java.util.List;

public class AdminService implements TransactionViewer{

    private final CustomerService customerService;
    private final OrderServic orderServic;
    private final ProductService productService;
    private final Admin admin;


    public AdminService(CustomerService customerService, OrderServic orderServic, ProductService productService,
                        Admin admin) {
        this.customerService = customerService;
        this.orderServic = orderServic;
        this.productService = productService;
        this.admin = admin;
    }
    public Admin getAdmin() {
        return admin;
    }


    public ProductService getProductService() {
        return this.productService;
    }

    public void viewAllUsers() {
        Collection<Customer> customers = customerService.getAllCustomers();
        System.out.println("👥 Total Users: " + customers.size());
        for (Customer customer : customers) {
            System.out.println("🆔 " + customer.getId() + " | " + customer.getName() +
                    " | 📧 " + customer.getEmail() + " | 💰 $" + customer.getBalance());
        }
    }

    public void filterUsersByNameKeyword(String keyword) {
        Collection<Customer> customers = customerService.getAllCustomers();
        System.out.println("🔍 Users with name containing: \"" + keyword + "\"");
        for (Customer customer : customers) {
            if (customer.getName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println("🆔 " + customer.getId() + " | " + customer.getName());
            }
        }
    }
    public List<Customer> getUsersByBalanceRange(double min, double max) {
        List<Customer> matching = new ArrayList<>();
        Collection<Customer> customers = customerService.getAllCustomers();

        for (Customer customer : customers) {
            if (customer.getBalance() >= min && customer.getBalance() <= max) {
                matching.add(customer);
            }
        }

        return matching;
    }

    public void filterProductsByCategory(String category) {
        ProductFilter filter = new CategoryFilter();
        List<Product> filtered = productService.getFilteredProducts(filter, category);

        if (filtered.isEmpty()) {
            System.out.println("🚫 No products found in category: " + category);
        } else {
            System.out.println("📦 Products in category: " + category);
            for (Product p : filtered) {
                System.out.println("🔹 ID: " + p.getId() +
                        ", Name: " + p.getName() +
                        ", Price: $" + p.getPrice());
            }
        }
    }
    public void filterOrdersByDateRange(LocalDate from, LocalDate to) {
        List<Order> orders = orderServic.getOrders();
        boolean found = false;

        System.out.println("📅 Admin Orders from " + from + " to " + to);

        for (Order order : orders) {
            LocalDate orderDate = order.getOrderDate().toLocalDate();

            if ((orderDate.isEqual(from) || orderDate.isAfter(from)) &&
                    (orderDate.isEqual(to) || orderDate.isBefore(to))) {

                System.out.println("🆔 Order ID: " + order.getOrderId() +
                        ", Customer: " + order.getCustomer().getName() +
                        ", Total: $" + order.getOrder_total() +
                        ", Status: " + order.getStatus());
                found = true;
            }
        }

        if (!found) {
            System.out.println("🚫 No orders found in the specified date range.");
        }
    }


    public void viewAllTransactions() {
            List<Order> orders = orderServic.getOrders();
            System.out.println("📦 All Transactions Overview: " + orders.size());

            for (Order order : orders) {
                System.out.println("🆔 Order ID: " + order.getOrderId() +
                        ", Customer: " + order.getCustomer().getName() +
                        ", Email: " + order.getCustomer().getEmail() +
                        ", Total: $" + order.getOrder_total() +
                        ", Status: " + order.getStatus());
            }
        }


    @Override
    public void viewTransactionsByUser(String email) {
        List<Order> orders = orderServic.getOrders();
        boolean found = false;

        for (Order order : orders) {
            if (order.getCustomer().getEmail().equalsIgnoreCase(email)) {
                found = true;
                System.out.println("🆔 Order ID: " + order.getOrderId() +
                        ", Total: $" + order.getOrder_total() +
                        ", Status: " + order.getStatus() +
                        ", Date: " + order.getOrderDate());
            }
        }

        if (!found) {
            System.out.println("❌ No transactions found for user: " + email);
        }
    }
}