package com.ecommerce.service;

import com.ecommerce.model.entities.Customer;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;
    private CartService cartService;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private  CustomerRepository customerRepository;
    private OrderService orderService;

    @Before
    public void setUp(){
        productRepository =new ProductRepository("products.txt");
        productService = new ProductService(productRepository);
        cartRepository= new CartRepository();
        cartService =new CartService(productService,cartRepository);
        orderRepository = new OrderRepository(productService);
        customerRepository=new CustomerRepository("customers.txt");
        orderService = new OrderService(cartService,orderRepository,customerRepository);
    }

    @Test
    public void testHasSufficientBalance() {
        //Arrange
        Customer customer = new Customer(1,"mahmoud","mahmoud@gmail.com","778877",123.5,"Zayed");

        //Act
        boolean result =orderService.hasSufficientBalance(customer,100.0);

        //Assert
        assertTrue(result);

    }
    @Test
    public void testHasInSufficientBalance() {
        //Arrange
        Customer customer = new Customer(1,"mahmoud","mahmoud@gmail.com","778877",123.5,"Zayed");

        //Act
        boolean result =orderService.hasSufficientBalance(customer,200.0);

        //Assert
        assertFalse(result);

    }

    @Test
    public void testAddFunds() {
        //Arrange
        Customer customer = new Customer(3,"Nadia","nadia@gmail.com","777777",7000.0,"Sama");

        //Act
        orderService.addFunds(customer,1000.0);

        //Assert
        assertEquals(8000.0,customer.getBalance(),0.0);
    }

    @Test
    public void getCartTotal() {
    }

    @Test
    public void createOrder() {
    }

    @Test
    public void filterCustomerOrdersByDate() {
    }

    @Test
    public void getOrders() {
    }

    @Test
    public void calculateTotal() {
    }

    @Test
    public void getOrdersForCustomer() {
    }

    @Test
    public void getCartForCustomer() {
    }
}