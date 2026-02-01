package test.java.repository;

import main.java.model.entities.Customer;
import main.java.model.entities.Order;
import main.java.repository.OrderRepository;
import main.java.service.ProductService;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FakeOrderRepository extends OrderRepository {
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
