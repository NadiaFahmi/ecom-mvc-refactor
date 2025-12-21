package com.ecommerce.service;

import com.ecommerce.exception.CartItemNotFoundException;
import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.exception.InvalidQuantityException;
import com.ecommerce.model.entities.Cart;
import com.ecommerce.model.entities.CartItem;
import com.ecommerce.model.entities.Customer;
import com.ecommerce.model.entities.Product;
import com.ecommerce.repository.CartRepository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartService {
    private Logger logger= Logger.getLogger(CartService.class.getName());
    private final ProductService productService;
    private final CartRepository cartRepository;

    public CartService(ProductService productService, CartRepository cartRepository) {
        this.productService = productService;
        this.cartRepository = cartRepository;
    }

    public void addProductToCart(Customer customer, int productId, int quantity) {
    logger.log(Level.INFO,"Attempting to find product id={0}",productId);
              Product product = productService.getProductById(productId);
        if (product == null) {
            logger.log(Level.WARNING, "No product with id={0}", productId);
            throw new InvalidProductException("Invalid product.");}
         if (quantity <= 0){
             logger.log(Level.WARNING, "Invalid quantity of:{0}", quantity);
             throw new InvalidQuantityException("Quantity cannot be negative");
        }

        Cart cart =cartRepository.findByCustomerId(customer.getId());
        if (cart == null) {
            cart = cartRepository.createCartForCustomer(customer.getId());
        }
        List<CartItem> items = getLoadedItems(cart);
        logger.log(Level.INFO,"Items loaded for customer id={0}",customer.getId());

        CartItem item = findItem(items, productId);
        if (item != null) {
            logger.log(Level.INFO,"Updated item id={0} - Quantity={1} for CustomerEmail={2}",new Object[]{productId,quantity,customer.getEmail()});
            increaseItemQuantity(item, quantity);
        } else {
            logger.log(Level.INFO,"Added item id={0} - Quantity={1} for CustomerEmail={2}",new Object[]{productId,quantity,customer.getEmail()});
            CartItem newItem = new CartItem(
                    cart.getId(),
                    product.getId(),
                    product.getName(),
                    quantity,
                    product.getPrice()
            );
            items.add(newItem);
        }

        cartRepository.saveCartItems(cart, items);
    }

    public void removeItemFromCart(Customer customer, int productId) {
        Cart cart =cartRepository.findByCustomerId(customer.getId());
        logger.log(Level.INFO,"Attempting to load items for customer id={0}",customer.getId());
        List<CartItem> items = getLoadedItems(cart);
        CartItem item = findItem(items, productId);
        if (item == null) {
            logger.log(Level.WARNING,"No product with id={0}",productId);
            throw new CartItemNotFoundException("Product not found in cart.");
        }
        items.remove(item);
        logger.log(Level.INFO,"Product id={0} removed",productId);
        saveCartItems(cart, items);
    }

    public void updateCartItemQuantity(Customer customer, int productId, int newQuantity) {
        Cart cart = cartRepository.findByCustomerId(customer.getId());
        logger.log(Level.INFO,"Attempting to load items for customer id={0}",customer.getId());
        List<CartItem> items = getLoadedItems(cart);

            CartItem item = findItem(items, productId);
        if (item == null) {
            logger.log(Level.WARNING,"No product with id={0}",productId);
            throw new CartItemNotFoundException("Product ID " + productId + " not found in cart.");
        }
            if (newQuantity > 0) {
                item.setQuantity(newQuantity);
                logger.log(Level.INFO,"Updated Quantity={0} - product id={1} - customer id={2}",new Object[]{newQuantity,productId,customer.getId()});
                saveCartItems(cart, items);
            } else {
                throw new InvalidQuantityException("Quantity must be greater than 0.");
            }
        }


public void saveCartItems(Cart cart, List<CartItem> items) {
    cartRepository.saveCartItems(cart, items);

}
    public void clearCartFileContents(Cart cart) {
        cartRepository.clearCartFileContents(cart);

    }

    public double calculateTotalPrice(Customer customer) {

        Cart cart= cartRepository.findByCustomerId(customer.getId());
        logger.log(Level.INFO,"Attempting to find cart id={0}",cart.getId());
        List<CartItem> items = cartRepository.loadCartItemsFromFile(cart);

        if(items == null || items.isEmpty()){
            logger.log(Level.WARNING,"No items in cart id={0}",cart.getId());
            throw new CartItemNotFoundException("No items - cart empty");
        }

        double total = 0.0;
        total = calculateTotal(items);
        logger.log(Level.INFO,"Total calculated:{0}",total);
        return total;
    }
    public double calculateTotal(List<CartItem> items){
        double total = 0.0;
        for (CartItem item : items) {
            Product product = productService.getProductById(item.getProductId());
            if (product != null) {
                total += product.getPrice() * item.getQuantity();
            }else{
                throw new InvalidProductException("Invalid product in cart");
            }
        }
        return total;
    }

    public CartItem findItem(List<CartItem> items, int productId) {
        for (CartItem item :items) {
            if (item.getProductId() == productId) {
                return item;
            }
        }
        return null;
    }

    public void increaseItemQuantity(CartItem item, int amount) {

        if (amount > 0) {
            item.setQuantity(item.getQuantity() + amount);

        }
    }
public List<CartItem> getLoadedItems(Cart cart) {
    return cartRepository.loadCartItemsFromFile(cart);
}
    public Cart getCartForCustomer(Customer customer) {

        Cart  cart = cartRepository.findByCustomerId(customer.getId());
        if (cart == null) {
            cart = new Cart(customer.getId());
            cartRepository.saveCart(cart);
        }
        return cart;
    }

}


