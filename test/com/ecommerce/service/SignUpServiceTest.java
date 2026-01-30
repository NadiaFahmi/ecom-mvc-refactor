package com.ecommerce.service;

import com.ecommerce.exception.*;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.repository.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class SignUpServiceTest {

    ProductRepository productRepo;
    FakeCustomerRepository customerRepository;
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
        customerRepository = new FakeCustomerRepository();
        productService = new ProductService(productRepo);
        cartRepository = new CartRepository();
        cartService = new CartService(productService, cartRepository);
        orderRepository = new OrderRepository(productService);
        orderService = new OrderService(cartService, orderRepository, customerRepository);
        customerService = new CustomerService(customerRepository, orderService);
        loginService = new LoginService(customerService);
        signUpService = new SignUpService(customerService);
        customerRepository.clear();
    }


    @Test
    public void testRegisterCustomerSuccess(){
        // Arrange
        String name="Jailan";
        String email="jailann@gmail.com";
        String password ="123456";
        double balance = 6000;
        String address = "Zaied";
        
        //Act
        Customer actual = signUpService.registerNewCustomer(name, email, password, balance, address);

        //Assert
        assertTrue(actual.getId() >0);
        assertEquals(name,actual.getName());
        assertEquals(email,actual.getEmail());
        assertEquals(password,actual.getPassword());
        assertEquals(balance,actual.getBalance(),0.0);
        assertEquals(address,actual.getAddress());
    }
    @Test
    public void testRegisterFailedName(){
        // Arrange

        String name="";
        String email="mahmoud@gmail.com";
        String password ="778877";
        double balance = 123.50;
        String address = "Zayed";

        //Act + Assert
        assertThrows(InvalidNameException.class, () -> signUpService.registerNewCustomer(name, email, password, balance, address));

    }

    @Test
    public void testDuplicateEmail(){
        // Arrange
        String name="mahmoud";
        String email="mahmoud@gmail.com";
        String password ="778877";
        double balance = 123.50;
        String address = "Zayed";
       signUpService.registerNewCustomer(name, email, password, balance, address);
        //Act

        assertThrows(DuplicateEmailException.class, () -> signUpService.registerNewCustomer(name, email, password, balance, address));

    }
    @Test()
    public void testRegisterFailedPassword(){
        // Arrange
        String name="Salwa";
        String email="salwaa@gmail.com";
        String password ="8888";
        double balance = 123.50;
        String address = "New Giza";

        //Act + Assert
        assertThrows(InvalidPasswordException.class,
                ()-> signUpService.registerNewCustomer(name,email,password,balance,address));

    }
    @Test
    public void testRegisterFailedEmail(){
        // Arrange
        String name="Salwa";
        String email="nadia.com";
        String password ="888888";
        double balance = 123.50;
        String address = "New Giza";

        //Act + Assert
        assertThrows(InvalidEmailFormatException.class,
                ()-> signUpService.registerNewCustomer(name,email,password,balance,address));
    }
    @Test
    public void testRegisterEmptyEmail(){
        // Arrange
        String name="Salwa";
        String email="";
        String password ="888888";
        double balance = 123.50;
        String address = "New Giza";

        //Act + Assert
        assertThrows(InvalidEmailException.class,
                ()-> signUpService.registerNewCustomer(name,email,password,balance,address));
    }
    @Test
    public void testRegisterFailedBalance(){
        // Arrange
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
    public void testRegisterFailedAddress(){

        // Arrange
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
