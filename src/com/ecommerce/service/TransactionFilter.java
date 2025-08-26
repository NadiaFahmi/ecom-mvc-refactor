package com.ecommerce.service;

import com.ecommerce.model.entities.Order;
import com.ecommerce.model.entities.User;

import java.util.List;

public interface TransactionFilter {
    List<Order> filter(List<Order> orders, User user, String userName);

}
