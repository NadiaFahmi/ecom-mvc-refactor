package com.ecommerce;

import com.ecommerce.repository.CartRepository;
import com.ecommerce.service.CartService;
import com.ecommerce.service.ProductService;

public class FakeCartService extends CartService {
    public FakeCartService(ProductService productService, CartRepository cartRepository) {
        super(productService, cartRepository);
    }
}
