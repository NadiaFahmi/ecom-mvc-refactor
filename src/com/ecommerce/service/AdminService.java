package com.ecommerce.service;

import com.ecommerce.exception.CustomerNotFoundException;
import com.ecommerce.exception.EmptyDataException;
import com.ecommerce.exception.InvalidBalanceException;
import com.ecommerce.exception.NoOrdersFoundException;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Order;
import com.ecommerce.repository.CustomerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//public class AdminService implements TransactionViewer{
public class AdminService {
    private Logger logger=Logger.getLogger(AdminService.class.getName());
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final OrderService orderService;

    public AdminService(CustomerService customerService, CustomerRepository customerRepository, OrderService orderService
    ) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.orderService = orderService;
    }


    public Collection<Customer> getAllCustomers() {
        return customerService.listAllCustomers();
    }


    public List<Customer> filterUsersByNameKeyword(String keyword) {
        Collection<Customer> allCustomers = getAllCustomers();
        List<Customer> filtered = new ArrayList<>();
        if(keyword.isEmpty()){
            throw new IllegalArgumentException("Key must not be empty");
        }
        for (Customer customer : allCustomers) {
            String name = customer.getName();
            if (name != null && name.toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(customer);
            }
        }

        return filtered;
    }

    public List<Customer> getUsersByBalanceRange(double min, double max) {

        if(min == -1 || max== -1){
            throw new InvalidBalanceException();
        }
        if(min <0 || max <0){
            throw new InvalidBalanceException();
        }
        List<Customer> matching = new ArrayList<>();
        Collection<Customer> customers = customerService.listAllCustomers();

        for (Customer customer : customers) {
            if (customer.getBalance() >= min && customer.getBalance() <= max) {
                matching.add(customer);
            }
        }

        return matching;
    }


    public List<Order> getOrdersByDateRange(LocalDate from, LocalDate to) {
        List<Order> allOrders = orderService.getOrders();
        List<Order> filteredOrders = new ArrayList<>();
       if(from == null || to ==null){
           throw new IllegalArgumentException();
       }
        for (Order order : allOrders) {
            LocalDate orderDate = order.getOrderDate().toLocalDate();
            if ((orderDate.isEqual(from) || orderDate.isAfter(from)) &&
                    (orderDate.isEqual(to) || orderDate.isBefore(to))) {
                filteredOrders.add(order);
            }
        }
        logger.log(Level.INFO,"Orders loaded successfully. from={0}  to={1}",new Object[]{from,to});
        return filteredOrders;

    }


    public List<Order> getAllTransactions() {
        return orderService.getOrders();
    }


    public List<Order> getOrdersByUser(String email) {
        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : orderService.getOrders()) {
            Customer customer = customerRepository.getCustomerById(order.getCustomerId());

            if (customer != null && customer.getEmail().equalsIgnoreCase(email)) {
                filteredOrders.add(order);
            }
        }
    logger.log(Level.INFO,"Orders loaded successfully. count={0}",filteredOrders.size());
        return filteredOrders;
    }


}