package com.ecommerce.service;

import com.ecommerce.model.entities.Product;

import java.util.List;

public interface ProductFilter {
    List<Product> filter(List<Product> products, String criteria);
}
