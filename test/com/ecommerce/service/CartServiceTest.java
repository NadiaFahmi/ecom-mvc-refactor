package com.ecommerce.service;

import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.exception.InvalidQuantityException;
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
        Customer customer = new Customer(4,"Jailan","jailan@gmail.com","123456",6000.0,"Zaied");
        Product product = new Product(11,"Product_NadiaFahmi",40000.0,"women");

        //Act
        cartService.addProductToCart(customer,product.getId(),2);

        //Assert
        Cart cart = cartService.getCartForCustomer(customer);
        List<CartItem> items = cartService.getLoadedItems(cart);

        CartItem targetItem =cartService.requiredItem(items,product.getId());

        assertNotNull(cart);
        assertNotNull(items);

        assertEquals(11,targetItem.getProductId());
        assertEquals(2,targetItem.getQuantity());
        assertEquals("Product_NadiaFahmi",targetItem.getName());
        assertEquals(40000.0,targetItem.getPrice(),0.0);

    }

    @Test
    public void updateCartItemQuantitySuccess() {
        // Arrange (implicit: CartService)
        Customer customer = new Customer(4,"Jailan","jailan@gmail.com","123456",6000.0,"Zaied");
        Product product = new Product(10,"product_super",200.0,"men");

        //Act
        cartService.updateCartItemQuantity(customer,product.getId(),4);

        //Assert
        Cart cart = cartService.getCartForCustomer(customer);
        List<CartItem> items = cartService.getLoadedItems(cart);
        CartItem targetItem =cartService.requiredItem(items,product.getId());

        assertEquals(10,targetItem.getProductId());
        assertEquals(4,targetItem.getQuantity());

    }

    @Test
    public void updateCartItemQuantityFailed() {
        // Arrange (implicit: CartService)
        Customer customer = new Customer(4,"Jailan","jailan@gmail.com","123456",6000.0,"Zaied");
        Product product = new Product(10,"product_super",200.0,"men");
        //Act + Assert
        assertThrows(InvalidQuantityException.class,()->
               cartService.addProductToCart(customer,product.getId(),-3) );
    }

    @Test
    public void calculateItemsTotalPriceSuccess() {
        // Arrange (implicit: CartService)

        CartItem item= new CartItem(1,"Product_1_1",2,300.0);
        CartItem item2 = new CartItem(2,"product_2",2,345.5);
        List<CartItem> items = Arrays.asList(item,item2);

        //Act
        double total = cartService.calculateItemsTotalPrice(items);

        //Assert
        assertEquals(1291.0,total,0.0);

    }

    @Test
    public void findItemSuccess() {
        // Arrange (implicit: CartService)

        CartItem item= new CartItem(1,"Product_1_1",2,300.0);
        CartItem item2 = new CartItem(2,"product_2",2,345.5);

        List<CartItem> items = Arrays.asList(item,item2);

        //Act
        CartItem result = cartService.findItem(items,1);

        //Assert
        assertNotNull(result);
        assertEquals(1,result.getProductId());
        assertEquals("Product_1_1", result.getName());
        assertEquals(2, result.getQuantity());
        assertEquals(300.0, result.getPrice(),0.0);

    }
    @Test
    public void findItem_ItemNotFound() {
        // Arrange (implicit: CartService)
        CartItem item= new CartItem(1,"Product_1_1",1,300.0);
        CartItem item2 = new CartItem(2,"product_2",2,345.5);

        List<CartItem> items = Arrays.asList(item,item2);

        //Act + Assert
        assertNull(cartService.findItem(items,99));
    }
    @Test
    public void requiredItemFalse() {
        // Arrange (implicit: CartService)
        CartItem item= new CartItem(1,"Product_1_1",1,300.0);
        CartItem item2 = new CartItem(2,"product_2",2,345.5);

        List<CartItem> items = Arrays.asList(item,item2);

        //Act + Assert
        assertThrows(InvalidProductException.class,(()-> cartService.requiredItem(items,99)));

    }
}