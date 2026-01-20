package com.ecommerce.service;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpServiceTest {

    ProductRepository productRepo;
    CustomerRepository customerRepository;
    ProductService productService;
    CartRepository cartRepository;
    CartService cartService;
    OrderRepository orderRepository;
    OrderService orderService;
    CustomerService customerService;
    LoginService loginService;
    SignUpService signUpService;

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
        signUpService = new SignUpService(customerService);
    }


    @Test
    public void testRegisterCustomerSuccess(){
        // Arrange (implicit: CustomerService, CustomerRepository and SignUpService ready)
        customerService.loadCustomers();
        String name="Jailan";
        String email="jailan@gmail.com";
        String password ="123456";
        double balance = 6000;
        String address = "Zaied";

        //Act
        int id=customerRepository.getIdCounter();
        Customer expected = new Customer(id,name, email, password, balance, address);
        Customer actual = signUpService.registerNewCustomer(name, email, password, balance, address);

        //Assert
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
        // Arrange (implicit: CustomerService and SignUpService ready)
        customerService.loadCustomers();
        String name="";
        String email="mahmoud@gmail.com";
        String password ="778877";
        double balance = 123.50;
        String address = "Zayed";

        //Act + Assert
        assertThrows(InvalidNameException.class, () -> signUpService.registerNewCustomer(name, email, password, balance, address));

    }

    @Test
    public void testRegisterFailedEmail(){
        // Arrange (implicit: CustomerService and SignUpService ready)
        customerService.loadCustomers();
        String name="mahmoud";
        String email="mahmoud@gmail.com";
        String password ="778877";
        double balance = 123.50;
        String address = "Zayed";

        //Act + Assert
        assertThrows(InvalidEmailException.class, () -> signUpService.registerNewCustomer(name, email, password, balance, address));

    }
    @Test()
    public void testRegisterFailedPassword(){
        // Arrange (implicit: CustomerService and SignUpService ready)
        customerService.loadCustomers();
        String name="Salwa";
        String email="salwaa@gmail.com";
        String password ="8888";
        double balance = 123.50;
        String address = "New Giza";

        //Act + Assert
        assertThrows(InvalidPasswordException.class, ()-> signUpService.registerNewCustomer(name,email,password,balance,address));

    }
    @Test
    public void testRegisterFailedBalance(){
        // Arrange (implicit: CustomerService and SignUpService ready)
        customerService.loadCustomers();
        String name="Salwa";
        String email="salwaaa@gmail.com";
        String password ="888887";
        double balance = -2;
        String address = "New Giza";

        //Act + Assert
        assertThrows(InvalidBalanceException.class,
                ()-> signUpService.registerNewCustomer(name, email, password, balance, address));

    }
    @Test
    public void testRegisterFailedAdress(){

        // Arrange (implicit: CustomerService and SignUpService ready)
        customerService.loadCustomers();
        String name="Salwa";
        String email="salwaaa@gmail.com";
        String password ="888887";
        double balance = 200;
        String address = "";

       //Act + Assert
        assertThrows(InvalidAddressException.class,
                ()-> signUpService.registerNewCustomer(name, email, password, balance, address));

    }
}
