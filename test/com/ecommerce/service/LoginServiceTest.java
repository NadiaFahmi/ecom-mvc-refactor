package com.ecommerce.service;

import com.ecommerce.exception.InvalidEmailException;
import com.ecommerce.exception.InvalidPasswordException;
import com.ecommerce.model.entities.Admin;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.User;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class LoginServiceTest {

    ProductRepository productRepo;
    CustomerRepository customerRepository;
    ProductService productService;
    CartRepository cartRepository;
    CartService cartService;
    OrderRepository orderRepository;
    OrderService orderService;
    CustomerService customerService;
    LoginService loginService;

    @Before
    public void setUp() {
        productRepo = new ProductRepository("products.txt");
        customerRepository = new CustomerRepository("customers.txt");
        productService = new ProductService(productRepo);
        cartRepository = new CartRepository();
        cartService = new CartService(productService, cartRepository);
        orderRepository = new OrderRepository(productService);
        orderService = new OrderService(cartService, orderRepository, customerRepository);
        customerService = new CustomerService(customerRepository, orderService);
        loginService = new LoginService(customerService);
    }
    @Test
    public void testAdminLoginSuccess() {
        // Arrange
        String adminEmail = "admin@gmail.com";
        String adminPass = "adminPass";

        //Act
        User user = loginService.login(adminEmail, adminPass);

        //Assert
        assertNotNull(user);
        assertTrue(user instanceof Admin);
        assertEquals(adminEmail, user.getEmail());
        assertEquals(adminPass, user.getPassword());
    }

    @Test
    public void testLogin_WrongPassword() {
        // Arrange
        String adminEmail = "admin@gmail.com";
        String wrongPass = "wrongPass";

        //Act + Assert
        assertThrows(InvalidPasswordException.class, () -> {
            loginService.login(adminEmail, wrongPass);
        });


    }

    @Test
    public void testUserLoginSuccess() {

        // Arrange
        customerService.loadCustomers();
        String userEmail = "youssef@gmail.com";
        String userPassword = "123456";

        //Act
        User user = loginService.login(userEmail, userPassword);

        //Assert
        assertNotNull(user);
        assertTrue(user instanceof Customer);
        assertEquals(userEmail, user.getEmail());
        assertEquals(userPassword, user.getPassword());

    }

    @Test()
    public void testUserLoginFailed() {

        // Arrange
        customerService.loadCustomers();
        String userEmail = "yousseff@gmail.com";
        String userPassword = "123456";

        //Act + Assert
        assertThrows(InvalidEmailException.class, () ->
        {
            loginService.login(userEmail, userPassword);
        });


    }

    @Test
    public void testUserLoginPasswordFailed() {

        // Arrange
        customerService.loadCustomers();
        String userEmail = "youssef@gmail.com";
        String userPassword = "1234567";

        //Act + Assert
        assertThrows(InvalidPasswordException.class, () ->
        {
            loginService.login(userEmail, userPassword);
        });


    }

}
