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
    private Logger logger = Logger.getLogger(CartService.class.getName());
    private final ProductService productService;
    private final CartRepository cartRepository;

    public CartService(ProductService productService, CartRepository cartRepository) {
        this.productService = productService;
        this.cartRepository = cartRepository;
    }

    public void addProductToCart(Customer customer, int productId, int quantity) {
        Product product = validateProduct(productId);
        validateQuantity(quantity);

        Cart cart = getCart(customer);
        List<CartItem> items = getLoadedItems(cart);

        CartItem item = findItem(items, productId);
        if (item != null) {
            increment_cart_item_quantity(item, productId, quantity);
        } else {
            items.add(createNewCartItems(cart, product, quantity));
        }
        cartRepository.saveCartItems(cart, items);
    }

    public void increment_cart_item_quantity(CartItem item, int productId, int quantity) {
        logger.log(Level.INFO, "Item id={0} updated. previous quantity={1} , added={2} ",
                new Object[]{productId, item.getQuantity(), quantity});
        increaseItemQuantity(item, quantity);
    }

    public CartItem createNewCartItems(Cart cart, Product product, int quantity) {
        logger.log(Level.INFO, "Item id={0} added,  Quantity={1}, price={2}", new Object[]{product.getId(), quantity, product.getPrice()});
        return new CartItem(cart.getId(),
                product.getId(),
                product.getName(),
                quantity,
                product.getPrice());
    }

    public Product validateProduct(int productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new CartItemNotFoundException("Invalid product.");
        }
        return product;
    }

    public void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity cannot be zero or negative");
        }

    }

    public Cart getCart(Customer customer) {

        Cart cart = cartRepository.findByCustomerId(customer.getId());
        if (cart == null) {
            cart = cartRepository.createCartForCustomer(customer.getId());
        }
        return cart;
    }

    public void removeItemFromCart(Customer customer, int productId) {
        Cart cart = cartRepository.findByCustomerId(customer.getId());
        List<CartItem> items = getLoadedItems(cart);
        CartItem item = findItem(items, productId);
        if (item == null) {
            throw new CartItemNotFoundException("Product not found in cart.");
        }
        items.remove(item);
        logger.log(Level.INFO, "Product id={0} removed", productId);
        saveCartItems(cart, items);
    }

    public void updateCartItemQuantity(Customer customer, int productId, int newQuantity) {
        Cart cart = cartRepository.findByCustomerId(customer.getId());
        List<CartItem> items = getLoadedItems(cart);

        CartItem item = findItem(items, productId);
        if (item == null) {

            throw new CartItemNotFoundException("Product ID " + productId + " not found in cart.");
        }
        if (newQuantity > 0) {
            item.setQuantity(newQuantity);
            logger.log(Level.INFO, "Updated Quantity={0} - product id={1}", new Object[]{newQuantity, productId});
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

        Cart cart = cartRepository.findByCustomerId(customer.getId());
        List<CartItem> items = cartRepository.loadCartItemsFromFile(cart);

        if (items == null || items.isEmpty()) {
            throw new CartItemNotFoundException("No items - cart empty");
        }

        double total;
        total = calculateItemsTotalPrice(items);
        return total;
    }

    public double calculateItemsTotalPrice(List<CartItem> items) {

        double total = 0.0;
        for (CartItem item : items) {
            Product product = productService.getProductById(item.getProductId());
            if (product != null) {
                double itemTotal = product.getPrice() * item.getQuantity();
                total +=itemTotal;
                logger.log(Level.INFO, "Product ID={0}, price=${1}, quantity={2}, itemTotal=${3} total=${4}", new Object[]{product.getId(), product.getPrice(), item.getQuantity(),itemTotal,total});
            } else {
                throw new InvalidProductException("Invalid product in cart");
            }
        }
        return total;
    }

    public CartItem findItem(List<CartItem> items, int productId) {
        for (CartItem item : items) {
            if (item.getProductId() == productId) {
                logger.log(Level.INFO, "Found cartItem with productId={0}", productId);
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

        Cart cart = cartRepository.findByCustomerId(customer.getId());
        if (cart == null) {
            cart = new Cart(customer.getId());
            cartRepository.saveCart(cart);
        }
        return cart;
    }

}


