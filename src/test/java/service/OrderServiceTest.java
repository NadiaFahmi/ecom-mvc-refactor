package test.java.service;

import main.java.model.entities.Customer;
import main.java.model.entities.Order;
import main.java.repository.CartRepository;
import main.java.repository.ProductRepository;
import main.java.service.OrderService;
import main.java.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import test.java.FakeCartService;
import test.java.repository.FakeCustomerRepository;
import test.java.repository.FakeOrderRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class OrderServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;
    private FakeCartService cartService;
    private CartRepository cartRepository;
    private FakeOrderRepository orderRepository;
    private FakeCustomerRepository customerRepository;
    private OrderService orderService;


    @Before
    public void setUp() {
        productRepository = new ProductRepository("products.txt");
        productService = new ProductService(productRepository);
        cartRepository = new CartRepository();
        cartService = new FakeCartService(productService, cartRepository);
        orderRepository = new FakeOrderRepository(productService);
        customerRepository = new FakeCustomerRepository();
        orderService = new OrderService(cartService, orderRepository, customerRepository);

    }

    @Test
    public void testHasSufficientBalance() {
        //Arrange
        Customer customer = new Customer(1, "mahmoud", "mahmoud@gmail.com", "778877", 123.5, "Zayed");

        //Act
        boolean result = orderService.hasSufficientBalance(customer, 100.0);

        //Assert
        assertTrue(result);

    }

    @Test
    public void testHasInSufficientBalance() {
        //Arrange
        Customer customer = new Customer(1, "mahmoud", "mahmoud@gmail.com", "778877", 123.5, "Zayed");

        //Act
        boolean result = orderService.hasSufficientBalance(customer, 200.0);

        //Assert
        assertFalse(result);

    }

    @Test
    public void testAddFunds() {
        //Arrange
        customerRepository.load();
        Customer customer = new Customer(2, "Nadia", "nadia@gmail.com", "777777", 2000.0, "Sama");

        //Act
        orderService.addFunds(customer, 1000.0);

        //Assert
        assertEquals(3000.0, customer.getBalance(), 0.0);
    }

    @Test
    public void validateBalanceSuccess() {
        Customer customer = new Customer(2, "Nadia", "nadia@gmail.com", "777777", 2000.0, "Sama");

        //Act
        orderService.validateBalance(customer, 1500.0);

        //Assert
        assertTrue(customer.getBalance() >= 1500);

    }

    @Test
    public void createOrder() {
    }

    @Test
    public void filterCustomerOrdersByDate() {
        //Arrange
        orderRepository.clear();
        Customer customer = new Customer(2, "Nadia", "nadia@gmail.com", "777777", 2000.0, "Sama");
        customerRepository.saveCustomer(customer);

        List<Order> orders= Arrays.asList(

         new Order(1, 2, 2000.0, "pending", LocalDateTime.parse("2026-01-22T19:21:52")),
        new  Order(2, 2, 3000.0, "pending", LocalDateTime.parse("2026-01-22T19:21:52")),
        new Order(3, 2, 4000.0, "pending", LocalDateTime.parse("2024-01-22T19:21:52")),
        new Order(3, 3, 4000.0, "pending", LocalDateTime.parse("2026-01-22T19:21:52")));
        orderRepository.saveAll(orders);

        //Act
        LocalDate targetDate = LocalDate.of(2026, 1, 22);
        List<Order> result= orderService.filterCustomerOrdersByDate(customer,targetDate);


        //Assert
        assertTrue(result.stream()
                .allMatch(o -> o.getOrderDate().toLocalDate().equals(targetDate)));
      assertEquals(2,result.size());
    }

    @Test
    public void getOrders() {
        //Arrange
        List<Order> orders=Arrays.asList(
        new Order(1, 2, 2000.0, "pending", LocalDateTime.parse("2026-01-22T19:21:52")),
                new  Order(2, 2, 3000.0, "pending", LocalDateTime.parse("2026-01-22T19:21:52")),
                new Order(3, 2, 4000.0, "pending", LocalDateTime.parse("2024-01-22T19:21:52")),
                new Order(4, 3, 4000.0, "pending", LocalDateTime.parse("2026-01-22T19:21:52")));
        orderRepository.saveAll(orders);

        //Act
        List<Order>  result= orderService.getOrders();

        //Assert
        assertEquals(4,result.size());
    }

    @Test
    public void calculateTotal() {
    }

    @Test

    public void getOrdersForCustomer() {
        //Arrange
        Customer customer = new Customer(2, "Nadia", "nadia@gmail.com", "777777", 2000.0, "Sama");
        customerRepository.saveCustomer(customer);

        List<Order> orders= Arrays.asList(
                new Order(1, 2, 2000.0, "pending", LocalDateTime.now()),
                new Order(2, 2, 2300.0, "pending", LocalDateTime.now()),
                new Order(3, 2, 2400.0, "pending", LocalDateTime.now()));
        orderRepository.saveAll(orders);

        //Act
        List<Order> customerOrders = orderService.getOrdersForCustomer(customer.getId());
        assertEquals(3, customerOrders.size());
    }


    @Test
    public void getCartForCustomer() {
    }
}