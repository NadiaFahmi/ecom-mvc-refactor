package com.ecommerce.service;

import com.ecommerce.exception.CartItemNotFoundException;
import com.ecommerce.exception.InvalidBalanceException;
import com.ecommerce.model.entities.*;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.OrderRepository;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderService {
    private   Logger logger = Logger.getLogger(OrderService.class.getName());
    private final List<Order> orders = new ArrayList<>();
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private boolean ordersLoaded = false;

    public OrderService(CartService cartService, OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.customerRepository = customerRepository;
    }

    public boolean hasSufficientBalance(Customer customer
            ,double requiredTotal) {
        return customer.getBalance() >= requiredTotal;
    }
    public void addFunds(Customer customer, double amount){
        customer.setBalance(customer.getBalance() + amount);
        customerRepository.saveCustomer(customer);


    }
    public double getCartTotal(Customer customer){
        Cart cart = getCartForCustomer(customer);
        List<CartItem> cartItems = cartService.getLoadedItems(cart);
        return calculateTotal(cartItems);
    }
        public Order createOrder(Customer customer) {

        customer = customerRepository.getCustomerById(customer.getId());
        if (customer == null) return null;
        Cart cart = getCartForCustomer(customer);

        List<CartItem> cartItems = cartService.getLoadedItems(cart);
        if (cartItems.isEmpty()) {
            throw new CartItemNotFoundException("!");
        }

        double total = calculateTotal(cartItems);


        if (customer.getBalance() < total) {
          throw new InvalidBalanceException();

        }

        Order newOrder = orderRepository.saveOrder(customer, cartItems, total);
        newOrder.setOrderTotal(total);
        customer.setBalance(customer.getBalance() - total);

        customerRepository.saveCustomer(customer);
        orders.add(newOrder);
        logger.info( "createOrder called");
        cartService.clearCartFileContents(cart);

        logger.log(Level.INFO,"user={0}, orderId={1}, price={2}, status={3}",new Object[]{customer.getName(),newOrder.getOrderId(),total,newOrder.getStatus()});
        return newOrder;
    }

    public List<Order> filterCustomerOrdersByDate(Customer customer, LocalDate date) {
        List<Order> allOrders = getOrders();

        if (date== null){
            throw new DateTimeException("Invalid date");
        }
        logger.log(Level.INFO,"Attempt to get order date={0}",date.toString());

        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.getCustomerId() == customer.getId() && order.getOrderDate().toLocalDate().equals(date)) {
                filteredOrders.add(order);
            }
        }

        return filteredOrders;
    }
    public List<Order> getOrders() {
        if (!ordersLoaded) {
            List<Order> loadedOrders = orderRepository.loadOrders(id -> customerRepository.getCustomerById(id));
            if (loadedOrders == null || loadedOrders.isEmpty()) {
                return Collections.emptyList();

            }
            orders.addAll(loadedOrders);
            ordersLoaded = true;

        }
        return orders;
    }

    public double calculateTotal(List<CartItem> cartItems) {
        double total = 0.0;
        total = cartService.calculateTotal(cartItems);

        return total;
    }

    public List<Order> getOrdersForCustomer(String email) {
        List<Order> customerOrders = new ArrayList<>();

        for (Order order : orders) {
            Customer customer = customerRepository.getCustomerById(order.getCustomerId());
            if (customer != null && customer.getEmail().equals(email)) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }

    public Cart getCartForCustomer(Customer customer) {
        return cartService.getCartForCustomer(customer);
    }


}

