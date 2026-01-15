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

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class LoginServiceTest {

    ProductRepository productRepo = new ProductRepository("products.txt");
    CustomerRepository customerRepository = new CustomerRepository("customers.txt");

    ProductService productService = new ProductService(productRepo);
    CartRepository cartRepository = new CartRepository();
    CartService cartService = new CartService(productService, cartRepository);
    OrderRepository orderRepository = new OrderRepository(productService);
    OrderService orderService = new OrderService(cartService, orderRepository, customerRepository);
    CustomerService customerService = new CustomerService(customerRepository, orderService);


    @Test
    public void testAdminLoginSuccess() {
        LoginService loginService = new LoginService(customerService);
        String adminEmail = "admin@gmail.com";
        String adminPass = "adminPass";

        User user = loginService.login(adminEmail, adminPass);

        assertNotNull(user);
        assertTrue(user instanceof Admin);
        assertEquals(adminEmail, user.getEmail());
        assertEquals(adminPass, user.getPassword());
    }

    @Test
    public void testLogin_WrongPassword() {
        LoginService loginService = new LoginService(customerService);

        String adminEmail = "admin@gmail.com";
        String wrongPass = "wrongPass";

        assertThrows(InvalidPasswordException.class, () -> {
            loginService.login(adminEmail, wrongPass);
        });


    }

    @Test
    public void testUserLoginSuccess() {
        LoginService loginService = new LoginService(customerService);
        customerService.loadCustomers();
        String userEmail = "rona@gmail.com";
        String userPassword = "123456";

        User user = loginService.login(userEmail, userPassword);

        assertNotNull(user);
        assertTrue(user instanceof Customer);
        assertEquals(userEmail, user.getEmail());
        assertEquals(userPassword, user.getPassword());

    }

    @Test()
    public void testUserLoginFailed() {
        LoginService loginService = new LoginService(customerService);
        customerService.loadCustomers();
        String userEmail = "ron@gmail.com";
        String userPassword = "123456";


        assertThrows(InvalidEmailException.class, () ->
        {
            loginService.login(userEmail, userPassword);
        });


    }

    @Test
    public void testUserLoginPasswordFailed() {
        LoginService loginService = new LoginService(customerService);
        customerService.loadCustomers();
        String userEmail = "rona@gmail.com";
        String userPassword = "1234567";

        assertThrows(InvalidPasswordException.class, () ->
        {
            loginService.login(userEmail, userPassword);
        });


    }

    public static void main(String[] args) {

    }
}
