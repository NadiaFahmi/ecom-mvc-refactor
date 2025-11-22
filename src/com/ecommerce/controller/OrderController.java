package com.ecommerce.controller;

import com.ecommerce.exception.InsufficientBalanceException;
import com.ecommerce.exception.NoOrdersFoundException;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.service.OrderService;
import com.ecommerce.view.OrderView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class OrderController {

    private final OrderService orderService;
    private final OrderView orderView;

    public OrderController(OrderService orderService, OrderView orderView) {
        this.orderService = orderService;
        this.orderView = orderView;
    }

    public void handlePlaceOrder(Customer customer) {
        List<CartItem> cartItems = customer.getCart().getCartItems();
        if (cartItems.isEmpty()) {
            orderView.showCartEmpty();
            return;
        }

        double total = orderService.calculateTotal(cartItems);
            if(!orderService.hasSufficientBalance(customer)){
            if (orderView.confirmAddFunds(total, customer.getBalance())) {
                double additional = orderView.promptAmountToAdd();
                if (additional <= 0 || !orderService.increaseBalance(customer, additional, total)) {
                    orderView.showInsufficientAfterAdd();
                    return;
                }
                orderView.showNewBalance(customer.getBalance());
            } else {
                orderView.showOrderCancelled();
                return;
            }
        }
        try {
            Order order = orderService.createOrder();
            if (order != null) {
            orderView.showOrderSuccess();}
        }catch (InsufficientBalanceException e){
            orderView.showErrorMessage(e.getMessage());
        }

    }

    public List<Order> getAllOrders() {
        return orderService.getOrders();
    }

    public void loadOrdersFromFile() {
        orderService.getOrders();
    }

    public void getCustomerOrders(Customer customer) {

        List<Order> allOrders = orderService.getOrders();
        List<Order> customerOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.getCustomer().getEmail().equals(customer.getEmail())) {
                customerOrders.add(order);
            }
        }
        orderView.displayOrders(customerOrders);
    }


    public void getOrderDetails(Order order, Customer customer) {

        orderView.renderOrderDetails(order, customer);
    }

    public void filterCustomerOrdersByDate(Customer customer, LocalDate date) {


        try {
            List<Order> orders = orderService.filterCustomerOrdersByDate(customer, date);
            for (Order order : orders) {
                getOrderDetails(order, customer);
            }
        }catch (NoOrdersFoundException e){
            orderView.showErrorMessage(e.getMessage());
        }
            }
        }



