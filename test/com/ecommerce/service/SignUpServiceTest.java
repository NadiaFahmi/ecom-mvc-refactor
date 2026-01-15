package com.ecommerce.service;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpServiceTest {
    ProductRepository productRepo = new ProductRepository("products.txt");
    CustomerRepository customerRepository = new CustomerRepository("customers.txt");
    ProductService productService = new ProductService(productRepo);
    CartRepository cartRepository = new CartRepository();
    CartService cartService = new CartService(productService, cartRepository);
    OrderRepository orderRepository = new OrderRepository(productService);
    OrderService orderService = new OrderService(cartService, orderRepository, customerRepository);
    CustomerService customerService = new CustomerService(customerRepository, orderService);

    @Test
    public void testRegisterCustomerSuccess(){

        customerService.loadCustomers();
        SignUpService signUpService = new SignUpService(customerService);

        String name="Suzan";
        String email="suzan@gmail.com";
        String password ="123456";
        double balance = 4000;
        String address = "tagamo3";

        int id=customerRepository.getIdCounter();
        Customer expected = new Customer(id,name, email, password, balance, address);
        Customer actual = signUpService.registerNewCustomer(name, email, password, balance, address);

        assertNotNull(actual);
        assertTrue(actual.getId() >0);
        assertEquals(name,actual.getName());
        assertEquals(expected.getId(),actual.getId());
        assertEquals(email,actual.getEmail());
        assertEquals(password,actual.getPassword());
        assertEquals(balance,actual.getBalance(),0.0);
        assertEquals(address,actual.getAddress());
    }
    @Test
    public void testRegisterFailedName(){
        SignUpService signUpService = new SignUpService(customerService);
        customerService.loadCustomers();
        String name="";
        String email="mahmoud@gmail.com";
        String password ="778877";
        double balance = 123.50;
        String address = "Zayed";

        assertThrows(InvalidNameException.class, () -> signUpService.registerNewCustomer(name, email, password, balance, address));

    }

    @Test
    public void testRegisterFailedEmail(){

        SignUpService signUpService = new SignUpService(customerService);
        customerService.loadCustomers();
        String name="mahmoud";
        String email="mahmoud@gmail.com";
        String password ="778877";
        double balance = 123.50;
        String address = "Zayed";

        assertThrows(InvalidEmailException.class, () -> signUpService.registerNewCustomer(name, email, password, balance, address));

    }
    @Test()
    public void testRegisterFailedPassword(){
        customerService.loadCustomers();
        SignUpService signUpService = new SignUpService(customerService);

        String name="Salwa";
        String email="salwaa@gmail.com";
        String password ="8888";
        double balance = 123.50;
        String address = "New Giza";

        assertThrows(InvalidPasswordException.class, ()-> signUpService.registerNewCustomer(name,email,password,balance,address));

    }
    @Test
    public void testRegisterFailedBalance(){
        customerService.loadCustomers();
        SignUpService signUpService = new SignUpService(customerService);

        String name="Salwa";
        String email="salwaaa@gmail.com";
        String password ="888887";
        double balance = -2;
        String address = "New Giza";

        assertThrows(InvalidBalanceException.class,()-> signUpService.registerNewCustomer(name, email, password, balance, address));

    }
    @Test
    public void testRegisterFailedAdress(){
        customerService.loadCustomers();
        SignUpService signUpService = new SignUpService(customerService);

        String name="Salwa";
        String email="salwaaa@gmail.com";
        String password ="888887";
        double balance = 200;
        String address = "";


        assertThrows(InvalidAddressException.class,()-> signUpService.registerNewCustomer(name, email, password, balance, address));

    }
}
