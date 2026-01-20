package com.ecommerce.service;

import com.ecommerce.exception.CartItemNotFoundException;
import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Product;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;


import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CartServiceTest {

    private Cart cart;
    private CartService cartService;
    private ProductRepository productRepository;
    private ProductService productService;

    @Before
    public void setUp() {
        cart= new Cart(1);
        productRepository= new ProductRepository("products.txt");
        productService = new ProductService(productRepository);
        cartService = new CartService(productService,new CartRepository());

    }

    @Test
    public void testAddProductToCart() {
        // Arrange (implicit: CartService)
        Customer customer = new Customer(7,"Jailan","jailan@gmail.com","123456",6000.0,"Zaied");
        Product product = new Product(11,"Product_NadiaFahmi",40000.0,"women");

        //Act
        cartService.addProductToCart(customer,product.getId(),2);

        //Assert
        Cart cart = cartService.getCartForCustomer(customer);
        List<CartItem> items = cartService.getLoadedItems(cart);

        CartItem targetItem = items.stream()
                .filter(i -> i.getProductId() == 11L)
                .findFirst()
                .orElseThrow();


        assertNotNull("Cart should not be null", cart);
        assertNotNull("Items list should not be null", items);

        assertEquals(11,targetItem.getProductId());
        assertEquals(2,targetItem.getQuantity());

    }

    @Test
    public void updateCartItemQuantity() {
        // Arrange (implicit: CartService)
        Customer customer = new Customer(7,"Jailan","jailan@gmail.com","123456",6000.0,"Zaied");
        Product product = new Product(11,"Product_NadiaFahmi",40000.0,"women");


        //Act
        cartService.updateCartItemQuantity(customer,product.getId(),4);

        //Assert

        Cart cart = cartService.getCartForCustomer(customer);
        List<CartItem> items = cartService.getLoadedItems(cart);

        CartItem targetItem= items.stream().filter(i ->i.getProductId()== 11 )
                .findFirst()
                .orElseThrow();

        assertEquals(11,targetItem.getProductId());
        assertEquals(4,targetItem.getQuantity());

    }

    @Test
    public void updateCartItemQuantityFailed() {
        // Arrange (implicit: CartService)
        Customer customer = new Customer(7,"Jailan","jailan@gmail.com","123456",6000.0,"Zaied");

        //Act + Assert
        assertThrows(CartItemNotFoundException.class,()->

               cartService.addProductToCart(customer,99,3) );
    }

    @Test
    public void testCalculateItemsTotalPrice() {

        // Arrange (implicit: CartService)
        Product product1=new Product(20,"test",99.0,"kids");
        Product product2=new Product(21,"test1",100.0,"kids");

        CartItem item= new CartItem(cart.getId(),product1.getId(),product1.getName(),1,product1.getPrice());
        CartItem item2= new CartItem(cart.getId(),product2.getId(),product2.getName(),1,product2.getPrice());
        List<CartItem> items = Arrays.asList(item,item2);

        //Act
        double total = cartService.calculateItemsTotalPrice(items);

        //Assert
        assertEquals(199.0,total,0.0);

    }

    @Test
    public void findItem_ItemFound() {
        // Arrange (implicit: CartService)
        Product product1= new Product(101,"product_test1",100.0,"men");
        Product product2= new Product(102,"product_test2",200.0,"women");

        CartItem item= new CartItem(cart.getId(),product1.getId(),product1.getName(),1,product1.getPrice());
        CartItem item2= new CartItem(cart.getId(),product2.getId(),product2.getName(),2,product2.getPrice());

        List<CartItem> items = Arrays.asList(item,item2);

        //Act
        CartItem result = cartService.findItem(items,102);

        //Assert
        assertNotNull(result);
        assertEquals(102,result.getProductId());
        assertEquals("product_test2", result.getName());
        assertEquals(2, result.getQuantity());
        assertEquals(200.0, result.getPrice(),0.0);

    }
    @Test
    public void findItem_ItemNotFound() {
        // Arrange (implicit: CartService)
        Product product1= new Product(101,"product_test1",100.0,"men");
        Product product2= new Product(102,"product_test2",200.0,"women");

        CartItem item= new CartItem(cart.getId(),product1.getId(),product1.getName(),1,product1.getPrice());
        CartItem item2 = new CartItem(cart.getId(),product2.getId(),product2.getName(),1,product2.getPrice());

        List<CartItem> items = Arrays.asList(item,item2);

        //Assert+ Act
        assertThrows(CartItemNotFoundException.class,()->
                cartService.findItem(items,99));
    }
}