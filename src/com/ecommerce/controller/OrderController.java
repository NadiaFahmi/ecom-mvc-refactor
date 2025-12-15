package com.ecommerce.controller;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Cart;
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
        Cart cart = orderService.getCartForCustomer(customer);
        List<CartItem> cartItems = orderService.getCartItems(cart);

        try {
            Order order = orderService.createOrder(customer);
            orderView.showOrderSuccess(order);
        } catch (CartItemNotFoundException e) {
            orderView.showCartEmpty();
        } catch (InvalidBalanceException e) {
            double total = orderService.calculateTotal(cartItems
            );
            if (orderView.confirmAddFunds(total, customer.getBalance())) {
                double additional = orderView.promptAmountToAdd();
                if (additional <= 0 || !orderService.hasSufficientBalance(customer, additional, total)) {
                    orderView.showInsufficientAfterAdd();
                    return;
                }
                orderView.showNewBalance(customer.getBalance());
                try {
                    Order order = orderService.createOrder(customer);
                    orderView.showOrderSuccess(order);
                } catch (InvalidBalanceException ex) {
                    orderView.showInsufficientAfterAdd();
                }
            } else {
                orderView.showOrderCancelled();
            }
        }
    }

    public void loadOrdersFromFile() {
        orderService.getOrders();
    }

    public void filterCustomerOrdersByDate(Customer customer, LocalDate date) {

        List<Order> allOrders = orderService.getOrders();

        List<Order> filteredOrders = new ArrayList<Order>();

        for (Order order : allOrders) {
            if (order.getCustomerId() == customer.getId() && order.getOrderDate().toLocalDate().equals(date)) {
                filteredOrders.add(order);
            }
        }

        if (filteredOrders.isEmpty()) {
            System.out.println("📭 No orders found for " + date);
            return;
        }

        orderView.displayOrders(filteredOrders);

            }
        }



