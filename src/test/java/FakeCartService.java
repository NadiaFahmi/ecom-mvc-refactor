package test.java;

import main.java.repository.CartRepository;
import main.java.service.CartService;
import main.java.service.ProductService;

public class FakeCartService extends CartService {
    public FakeCartService(ProductService productService, CartRepository cartRepository) {
        super(productService, cartRepository);
    }
}
