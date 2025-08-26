package com.ecommerce.service;

import com.ecommerce.model.entities.Product;

import java.util.ArrayList;
import java.util.List;



public class CategoryFilter implements ProductFilter {
    public List<Product> filter(List<Product> products, String category) {
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                result.add(p);
                           }
        }
        return result;
    }


}
