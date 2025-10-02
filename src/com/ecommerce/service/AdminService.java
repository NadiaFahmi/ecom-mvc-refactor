package com.ecommerce.service;

import com.ecommerce.model.entities.Admin;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import java.util.List;

//public class AdminService implements TransactionViewer{
    public class AdminService {

    private final CustomerService customerService;
    private final OrderService orderService;
    private final Admin admin;


    public AdminService(CustomerService customerService, OrderService orderService,
                        Admin admin) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.admin = admin;
    }
    public Admin getAdmin() {
        return admin;
    }


    public Collection<Customer> getAllCustomers() {
        return customerService.listAllCustomers();
    }


    public void filterUsersByNameKeyword(String keyword) {
        Collection<Customer> customers = customerService.listAllCustomers();
        System.out.println("Users with name containing: \"" + keyword + "\"");
        for (Customer customer : customers) {
            if (customer.getName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println("🆔 " + customer.getId() + " | " + customer.getName());
            }
        }
    }
    public List<Customer> getUsersByBalanceRange(double min, double max) {
        List<Customer> matching = new ArrayList<>();
        Collection<Customer> customers = customerService.listAllCustomers();

        for (Customer customer : customers) {
            if (customer.getBalance() >= min && customer.getBalance() <= max) {
                matching.add(customer);
            }
        }

        return matching;
    }


    public List<Order> getOrdersByDateRange(LocalDate from, LocalDate to) {
        List<Order> allOrders = orderService.getOrders();
        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : allOrders) {
            LocalDate orderDate = order.getOrderDate().toLocalDate();
            if ((orderDate.isEqual(from) || orderDate.isAfter(from)) &&
                    (orderDate.isEqual(to) || orderDate.isBefore(to))) {
                filteredOrders.add(order);
            }
        }

        return filteredOrders;
    }


    public List<Order> getAllTransactions() {
        return orderService.getOrders();
    }


public List<Order> getTransactionsByUser(String email) {
    List<Order> filteredOrders = new ArrayList<>();

    for (Order order : orderService.getOrders()) {
        if (order.getCustomer().getEmail().equalsIgnoreCase(email)) {
            filteredOrders.add(order);
        }
    }

    return filteredOrders;
}


}