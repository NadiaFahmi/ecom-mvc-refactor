package com.ecommerce.controller;

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
        if (customer.getBalance() < total) {


            if (orderView.confirmAddFunds(total, customer.getBalance())) {
                double additional = orderView.promptAmountToAdd();
                if (additional <= 0 || !orderService.tryAddFunds(customer, additional, total)) {
                    orderView.showInsufficientAfterAdd();
                    return;
                }
                orderView.showNewBalance(customer.getBalance());
            } else {
                orderView.showOrderCancelled();
                return;
            }
        }

        Order order = orderService.placeOrder();
        if (order != null) {
            orderView.showOrderSuccess();
        } else {
            orderView.showOrderFailure();
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderService.getOrders();
        return orders;
    }

    public void loadOrdersFromFile() {
        orderService.getOrders();
    }

    public void printCustomerOrders(Customer customer) {

        List<Order> allOrders = orderService.getOrders();
        List<Order> customerOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.getCustomer().getEmail().equals(customer.getEmail())) {
                customerOrders.add(order);
            }
        }
        orderView.displayOrders(customerOrders);
    }


    public void printOrderDetails(Order order, Customer customer) {

        orderView.renderOrderDetails(order, customer);
    }

    public void filterCustomerOrdersByDate(Customer customer, LocalDate date) {
        List<Order> filteredOrders = new ArrayList<Order>();

        for (Order order : customer.getOrders()) {
            if (order.getOrderDate().toLocalDate().equals(date)) {
                filteredOrders.add(order);
            }
        }

        if (filteredOrders.isEmpty()) {
            System.out.println("📭 No orders found for " + date);
            return;
        }

        for (Order order : filteredOrders) {
            printOrderDetails(order, customer);
            System.out.println("–––––––––––––––––––––––");
        }
    }
}
