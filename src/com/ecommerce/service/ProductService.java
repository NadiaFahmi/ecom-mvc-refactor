package com.ecommerce.service;
import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.model.entities.Product;
import com.ecommerce.repository.ProductRepository;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class ProductService {


    private Logger logger = Logger.getLogger(ProductService.class.getName());
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }


    public boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public boolean isPriceValid(double price) {
        return price >= 0;
    }

    public boolean isCategoryValid(String category) {
        return category != null && !category.trim().isEmpty();
    }

    public Product addProduct(String name, double price, String category) {

        if (!isNameValid(name)) {
            throw new InvalidProductException("name cannot be empty");
        }
        if (!isPriceValid(price)) {
            throw new InvalidProductException("Price must be greater than zero.");
        }
        if (!isCategoryValid(category)) {
            throw new InvalidProductException("category cannot be empty");
        }

        int id = calculateInitialCounter(loadProductList());
        logger.log(Level.INFO, "productIdCounter={0}", id);


        Product product = new Product(id, name, price, category);

        List<Product> products = loadProductList();
        products.add(product);
        repository.save(products);
        logger.log(Level.INFO, "Added new product: name={0}, price={1}, category={2}", new Object[]{name, price, category});
        return product;
    }

    public List<Product> getAllProducts() {
        Collection<Product> loaded = loadProductList();
        return new ArrayList<>(loaded);
    }

    public Product getProductById(int id) {
        for (Product product : loadProductList()) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public List<Product> getProductsByCategory(String category) {

        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category must not be empty");
        }

        List<Product> products = loadProductList();
        List<Product> filtered = filterProductsByCategory(products,category);

        List<Integer> matchingIds = new ArrayList<>();

        for (Product p : filtered) {
                matchingIds.add(p.getId());
            }

        logger.log(Level.INFO, "Matched category={0}", matchingIds);
        return filtered;
    }

   public  List<Product> filterProductsByCategory(List<Product> products, String category){

        List<Product> filtered = new ArrayList<>();
     for (Product product : products) {
           if (product.getCategory().equalsIgnoreCase(category)) {
               filtered.add(product);
           }
       }
    return filtered;
    }
    public boolean removeProduct(int id) {
        List<Product> products = loadProductList();
        boolean removed = products.removeIf(p -> p.getId() == id);

        if (removed) {
            repository.save(products);
        } else {
            logger.severe("Failed. ProductId not found");
            throw new InvalidProductException("productId not found");
        }
        return removed;
    }

    public void updateProduct(int id, String newName, double newPrice, String newCategory) {
        List<Product> products = loadProductList();

        Product updatedProduct = new Product(id, newName, newPrice, newCategory);

        if(replaceProduct(products,id,updatedProduct)){
            logger.log(Level.INFO, "Updated product with id={0}", id);
            repository.save(products);
        }else{
            logger.severe("Failed. ProductId not found");
            throw new InvalidProductException("❌ Product ID " + id + " not found.");

        }

    }
        public boolean replaceProduct(List<Product> products, int id, Product updateProduct){

        for(int i=0; i< products.size(); i++ ){
            if(products.get(i).getId() == id){
                products.set(i,updateProduct);
                return true;
            }
        }
        return false;

        }
    private List<Product> loadProductList() {
        return new ArrayList<>(repository.load());
    }


    public int calculateInitialCounter(Collection<Product> products) {

        int maxId = 0;
        for (Product product : products) {
            if (product.getId() > maxId) {
                maxId = product.getId();
            }

        }

        return maxId + 1;
    }
}