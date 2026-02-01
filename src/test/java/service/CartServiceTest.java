package test.java.service;

import main.java.exception.InvalidProductException;
import main.java.exception.InvalidQuantityException;
import main.java.model.entities.Cart;
import main.java.model.entities.CartItem;
import main.java.model.entities.Customer;
import main.java.model.entities.Product;
import main.java.repository.ProductRepository;
import main.java.service.CartService;
import main.java.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import test.java.repository.FakeCartRepository;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CartServiceTest {

    private Cart cart;
    private CartService cartService;
    private ProductRepository productRepository;
    private ProductService productService;
    private FakeCartRepository cartRepository;

    @Before
    public void setUp() {
        cart= new Cart(4);
        productRepository= new ProductRepository("products.txt");
        productService = new ProductService(productRepository);
        cartRepository = new FakeCartRepository();
        cartService = new CartService(productService,cartRepository);

    }

    @Test
    public void testAddProductToCart() {
        // Arrange
        cartRepository.clear();
        Customer customer = new Customer(4,"Jailan","jailan@gmail.com","123456",6000.0,"Zaied");
        Product product = new Product(3,"product_3",457.5,"men");
        CartItem item= new CartItem(1,"Product_1_1",2,300.0);
        CartItem item1= new CartItem(2,"product_2",2,345.5);

       List<CartItem> items = new ArrayList<>();
       items.add(item);
       items.add(item1);
       cartRepository.saveCartItems(cart,items);
        //Act

        cartService.addProductToCart(customer,product.getId(),2);

        //Assert
        List<CartItem> updatedCart= cartRepository.loadCartItemsFromFile(cart);
        CartItem addedItem = cartService.requiredItem(updatedCart,product.getId());
        assertNotNull(addedItem);
        assertEquals(3,updatedCart.size());
        assertEquals("product_3",addedItem.getName());
        assertEquals(2,addedItem.getQuantity());
        assertEquals(3,addedItem.getProductId());
        assertEquals(457.5,addedItem.getPrice(),0.0);

    }
    @Test
    public void testAddProductToCartWithIncrementQuantity() {
        //Arrange
        cartRepository.clear();
        Customer customer = new Customer(4,"Jailan","jailan@gmail.com","123456",6000.0,"Zaied");

        Product product = new Product(3,"product_3",457.5,"men");
        CartItem item= new CartItem(product.getId(),product.getName(),2,product.getPrice());

        cartRepository.saveItem(item);
//
        //Act
        cartService.addProductToCart(customer,product.getId(),3);

        //Assert
        List<CartItem> updatedCart= cartRepository.loadCartItemsFromFile(cart);

       CartItem itemFound= cartService.findItem(updatedCart,product.getId());

        assertEquals(1,updatedCart .size());
        assertEquals(5,itemFound.getQuantity());


    }

    @Test
    public void updateCartItemQuantitySuccess() {
        // Arrange
        Customer customer = new Customer(4,"Jailan","jailan@gmail.com","123456",6000.0,"Zaied");
        Product product = new Product(2,"product_2",345.5,"women");
////
        CartItem item= new CartItem(1,"Product_1_1",2,300.0);
        CartItem item1= new CartItem(2,"Product_2",2,345.5);

        List<CartItem> items = Arrays.asList(item,item1);
        cartRepository.saveCartItems(cart,items);

        //Act
        cartRepository.loadCartItemsFromFile(cart);
        cartService.updateCartItemQuantity(customer,product.getId(),6);

        //Assert
        Cart cart = cartService.getCartForCustomer(customer);
        cartService.getLoadedItems(cart);
        CartItem targetItem =cartService.requiredItem(items,product.getId());

        assertEquals(2,targetItem.getProductId());
        assertEquals(6,targetItem.getQuantity());

    }

    @Test
    public void updateCartItemQuantityFailed() {
        // Arrange
        Customer customer = new Customer(4,"Jailan","jailan@gmail.com","123456",6000.0,"Zaied");
        Product product = new Product(2,"product_2",345.5,"women");
        //Act + Assert
        assertThrows(InvalidQuantityException.class,()->
               cartService.addProductToCart(customer,product.getId(),-3) );
    }

    @Test
    public void calculateCartItemsTotalPriceSuccess() {
        // Arrange

        CartItem item= new CartItem(1,"Product_1_1",2,300.0);
        CartItem item2 = new CartItem(2,"product_2",2,345.5);
        List<CartItem> items = Arrays.asList(item,item2);

        //Act
        double total = cartService.calculateCartItemsTotalPrice(items);

        //Assert
        assertEquals(1291.0,total,0.0);

    }

    @Test
    public void findItemSuccess() {
        // Arrange

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
        // Arrange
        CartItem item= new CartItem(1,"Product_1_1",1,300.0);
        CartItem item2 = new CartItem(2,"product_2",2,345.5);

        List<CartItem> items = Arrays.asList(item,item2);

        //Act + Assert
        assertNull(cartService.findItem(items,99));
    }
    @Test
    public void requiredItemFalse() {
        // Arrange
        CartItem item= new CartItem(1,"Product_1_1",1,300.0);
        CartItem item2 = new CartItem(2,"product_2",2,345.5);

        List<CartItem> items = Arrays.asList(item,item2);

        //Act + Assert
        assertThrows(InvalidProductException.class,(()-> cartService.requiredItem(items,99)));

    }
}