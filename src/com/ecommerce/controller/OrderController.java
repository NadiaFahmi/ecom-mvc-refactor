package com.ecommerce.controller;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.service.OrderService;
import com.ecommerce.view.OrderView;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OrderController {
    private Logger logger = Logger.getLogger(OrderController.class.getName());
    private final OrderService orderService;
    private final OrderView orderView;

    public OrderController(OrderService orderService, OrderView orderView) {
        this.orderService = orderService;
        this.orderView = orderView;
    }

    public void handlePlaceOrder(Customer customer) {

        double requiredTotal =orderService.getCartTotal(customer);

        if(!orderService.hasSufficientBalance(customer,requiredTotal)) {

            boolean confirmed = orderView.confirmAddFunds(requiredTotal, customer.getBalance());
            if (!confirmed) {
                orderView.showOrderCancelled();
                return;
            }
            double additional = orderView.promptAmountToAdd();
            orderService.addFunds(customer, additional);
            orderView.showNewBalance(customer.getBalance());
            if (additional <= 0){
                orderView.showOrderCancelled();
            }
            if (!orderService.hasSufficientBalance(customer, requiredTotal)) {
                orderView.showInsufficientAfterAdd();
                return;
            }
            attemptCreateOrder(customer);

        }else {
            attemptCreateOrder(customer);

        }
    }
    public void attemptCreateOrder(Customer customer){
        try {
            Order order = orderService.createOrder(customer);
            orderView.showOrderSuccess(order);
        } catch (InvalidBalanceException ex) {
            orderView.showInsufficientAfterAdd();
        }catch (CartItemNotFoundException e){
            orderView.showCartEmpty();
        }

    }
    public void loadOrdersFromFile() {
        orderService.getOrders();
    }

    public void filterCustomerOrdersByDate(Customer customer) {

        LocalDate date = orderView.showDatePrompt();

        try {
            List<Order> filteredOrders =  orderService.filterCustomerOrdersByDate(customer, date);
            logger.log(Level.INFO,"user={0}, date={1}",new Object[]{customer.getName(),date.toString()});
            orderView.displayFilterOrders(filteredOrders);
        }catch (DateTimeException e){
            logger.warning("Invalid date");
            orderView.showErrorMessage(e.getMessage());
        }

            }

    public void getTotalOrderPrice(Customer customer) {
        List<Order> orders = orderService.getOrdersForCustomer(customer.getEmail());
        orderView.showOrdersPriceTotal(orders);

    }
    public void getOrdersForCustomer(Customer customer){

        List<Order> orders = orderService.getOrdersForCustomer(customer.getEmail());
        orderView.displayOrders(orders);

    }
        }



