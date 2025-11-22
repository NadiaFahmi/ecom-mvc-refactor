package com.ecommerce.service;

import com.ecommerce.exception.InsufficientBalanceException;
import com.ecommerce.exception.NoOrdersFoundException;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.OrderRepository;

import java.time.LocalDate;
import java.util.*;

public class OrderService {
//


    private final List<Order> orders = new ArrayList<>();
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private boolean ordersLoaded = false;

    public OrderService(CartService cartService, OrderRepository orderRepository, CustomerRepository customerRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.customerRepository = customerRepository;
    }

    public boolean hasSufficientBalance(Customer customer){
        List<CartItem> cartItems = customer.getCart().getCartItems();
        double total = calculateTotal(cartItems);
        return customer.getBalance() >= total;
    }

    public Order createOrder() {
        Customer customer = getLoggedInCustomer();
        if (customer == null) return null;
        List<CartItem> cartItems = customer.getCart().getCartItems();
        if (cartItems.isEmpty()) return null;

        double total = calculateTotal(cartItems);
        if (customer.getBalance() < total) {
            throw new InsufficientBalanceException(" Insufficient Balance");

        };

        Order newOrder = new Order(cartItems, customer);
        newOrder.markAsPaid();
        customer.setBalance(customer.getBalance() - total);

        orderRepository.saveOrder(newOrder);
        customer.addOrder(newOrder);
        orders.add(newOrder);
        cartService.clearCartFileContents(customer);
        customerRepository.saveCustomer(customer);
        return newOrder;
    }
    public boolean increaseBalance(Customer customer, double amount, double requiredTotal) {
        customer.setBalance(customer.getBalance() + amount);
        return customer.getBalance() >= requiredTotal;
    }

    public Customer getLoggedInCustomer() {
        String email = SessionContext.getLoggedInEmail(); // ✅ static access
        return customerRepository.getCustomerByEmail(email);
    }

    public List<Order> getOrders() {
        if (!ordersLoaded) {
            List<Order> loadedOrders = orderRepository.loadOrders(id -> customerRepository.getCustomerById(id));

            orders.addAll(loadedOrders);
            ordersLoaded = true;
//        }

        }
        return orders;
    }

    public double calculateTotal(List<CartItem> cartItems) {
        double total = 0.0;
        for (CartItem item : cartItems) {
            double price = item.getProduct().getPrice();
            int quantity = item.getQuantity();
            total += price * quantity;
        }
        return total;
    }

    public List<Order> getOrdersForCustomer(String email) {
        List<Order> customerOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomer().getEmail().equals(email)) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }

///////////
//public void filterCustomerOrdersByDate(Customer customer, LocalDate date)
public List<Order> filterCustomerOrdersByDate(Customer customer, LocalDate date) {
    List<Order> filteredOrders = new ArrayList<>();

    for (Order order : customer.getOrders()) {
        if (order.getOrderDate().toLocalDate().equals(date)) {
            filteredOrders.add(order);
        }
    }

    if (filteredOrders.isEmpty()) {
        throw new NoOrdersFoundException(date);

    }


return filteredOrders;
}
}

