package com.ecommerce.repository;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.service.ProductService;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FakeOrderRepository extends OrderRepository{
private List<Order> orders= new ArrayList<>();

boolean ordersLoaded =false;
    public FakeOrderRepository(ProductService productService) {

        super(productService);
    }

    public void saveOrder(Order order) {
        orders.add(order);
    }


    @Override
    public List<Order> findOrdersForCustomer(int customerId) {
        return orders.stream()
                .filter(o -> o.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> loadOrders(Function<Integer, Customer> customerResolver) {
        ordersLoaded = true;
        return new ArrayList<>(orders);

    }

    public  void saveAll(List<Order> orders) {
        this.orders.addAll(orders);
    }
    public void clear(){
        orders.clear();
    }
}
