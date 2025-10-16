package com.ecommerce.service;

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

    public AdminService(CustomerService customerService, OrderService orderService
                        ) {
        this.customerService = customerService;
        this.orderService = orderService;
    }


    public Collection<Customer> getAllCustomers() {
        return customerService.listAllCustomers();
    }


    public List<Customer> filterUsersByNameKeyword(String keyword) {
        Collection<Customer> allCustomers = getAllCustomers();
        List<Customer> filtered = new ArrayList<>();

        for (Customer customer : allCustomers) {
            String name = customer.getName();
            if (name != null && name.toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(customer);
            }
        }

        return filtered;
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


public List<Order> getOrdersByUser(String email) {
    List<Order> filteredOrders = new ArrayList<>();

    for (Order order : orderService.getOrders()) {
        if (order.getCustomer().getEmail().equalsIgnoreCase(email)) {
            filteredOrders.add(order);
        }
    }

    return filteredOrders;
}


}