package main.java.service;

import main.java.exception.CustomerNotFoundException;
import main.java.exception.EmptyCartException;
import main.java.exception.InvalidBalanceException;
import main.java.model.entities.*;
import main.java.repository.CustomerRepository;

import main.java.repository.OrderRepository;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderService {
    private Logger logger = Logger.getLogger(OrderService.class.getName());
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
    ///
    ///

    public Order createOrder(Customer customer) {
     Customer   persistedCustomer = validateCustomer(customer);

        Cart cart = getCartForCustomer(persistedCustomer);

        List<CartItem> cartItems =validateCartItems(cart);

        double total = calculateTotal(cartItems);

        validateBalance(persistedCustomer,total);

        Order newOrder = persistOrder(persistedCustomer,cartItems,total);

        cartService.clearCartFileContents(cart);

        logger.log(Level.INFO, "user={0}, orderId={1}, price={2}, status={3}", new Object[]{customer.getName(), newOrder.getOrderId(), total, newOrder.getStatus()});
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

public List<Order> getOrdersForCustomer(int customerId) {

    List<Order> customerOrders =orderRepository.findOrdersForCustomer(customerId);

    return customerOrders;
}

    public double getCartTotal(Customer customer) {
        Cart cart = getCartForCustomer(customer);
        List<CartItem> cartItems = cartService.getLoadedItems(cart);
        return calculateTotal(cartItems);
    }
    public boolean hasSufficientBalance(Customer customer
            , double requiredTotal) {
        return customer.getBalance() >= requiredTotal;
    }
    public void addFunds(Customer customer, double amount) {
        customer.setBalance(customer.getBalance() + amount);
        customerRepository.saveCustomer(customer);

    }

    public Cart getCartForCustomer(Customer customer) {
        return cartService.getCartForCustomer(customer);
    }

    public List<Order> filterCustomerOrdersByDate(Customer customer, LocalDate date) {
        List<Order> allOrders = getOrders();

        if (date == null) {
            throw new DateTimeException("Invalid date");
        }
        logger.log(Level.INFO, "Attempt to get order date={0} for customerId={1}", new Object[]{date.toString(),customer.getId()});

        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.getCustomerId() == customer.getId() && order.getOrderDate().toLocalDate().equals(date)) {
                filteredOrders.add(order);
            }
        }

        return filteredOrders;
    }

    //validation
    public Customer validateCustomer(Customer customer) {
        Customer persistedCustomer = customerRepository.getCustomerById(customer.getId());
        if (persistedCustomer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        return persistedCustomer;
    }

    public List<CartItem> validateCartItems(Cart cart){
        List<CartItem> items = cartService.getLoadedItems(cart);
        if (items.isEmpty()) {
            throw new EmptyCartException("Cart is empty");
        }
        return items;
    }

    public void validateBalance(Customer customer, double total){
        if (customer.getBalance() < total) {
            throw new InvalidBalanceException("Balance cannot be negative");
        }
    }

    //helper methods
    public Order persistOrder(Customer customer,List<CartItem> cartItems, double total){
        Order order = orderRepository.saveOrder(customer, cartItems, total);
        order.setOrderTotal(total);
        customer.setBalance(customer.getBalance() - total);
        customerRepository.saveCustomer(customer);
        orders.add(order);
        return order;
    }
    public double calculateTotal(List<CartItem> cartItems) {
        double total = 0.0;
        total = cartService.calculateCartItemsTotalPrice(cartItems);

        return total;
    }

}

