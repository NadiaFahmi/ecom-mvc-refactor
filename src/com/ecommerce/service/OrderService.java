package com.ecommerce.service;

import com.ecommerce.exception.CartItemNotFoundException;
import com.ecommerce.exception.InsufficientBalanceException;
import com.ecommerce.model.entities.*;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.OrderRepository;

import java.util.*;

public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductService productService;
    private boolean ordersLoaded = false;

    public OrderService(CartService cartService, OrderRepository orderRepository, CustomerRepository customerRepository
            , ProductService productService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.customerRepository = customerRepository;
        this.productService = productService;
    }

    public boolean hasSufficientBalance(Customer customer, double amount, double requiredTotal) {
        customer.setBalance(customer.getBalance() + amount);
        return customer.getBalance() >= requiredTotal;
    }

//    public Order createOrder() {
        public Order createOrder(Customer customer) {
//        Customer customer = getLoggedInCustomer();
        customer = customerRepository.getCustomerById(customer.getId());
        if (customer == null) return null;
        Cart cart = cartService.getCartForCustomer(customer);

        List<CartItem> cartItems = cartService.getLoadedItems(cart);
        if (cartItems.isEmpty()) {
            throw new CartItemNotFoundException("Cart is empty");
        }

        double total = calculateTotal(cartItems);
        if (customer.getBalance() < total) {
            throw new InsufficientBalanceException(" Insufficient Balance");

        }

        Order newOrder = orderRepository.saveOrder(customer, cartItems, total);
        customer.setBalance(customer.getBalance() - total);

        customerRepository.saveCustomer(customer);
        orders.add(newOrder);
        cartService.clearCartFileContents(cart);

        return newOrder;
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
        for (CartItem item : cartItems) {
            Product product = productService.getProductById(item.getProductId());
            if (product != null) {
                total += product.getPrice() * item.getQuantity();
            }
        }
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

    public List<CartItem> getCartItems(Cart cart) {
        return cartService.getLoadedItems(cart);
    }
}

