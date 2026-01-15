package com.ecommerce.service;

import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.model.entities.Product;
import com.ecommerce.repository.ProductRepository;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

public class ProductServiceTest extends TestCase {


    @Test
    public void testAddProduct() {

        ProductService productService = new ProductService(new ProductRepository("products.txt"));

        productService.addProduct("product_testing", 1200.0,"women");
        List<Product> products=productService.getAllProducts();

        Product added=products.get(products.size() -1);

        assertEquals("product_testing",added.getName());
        assertEquals(1200.0,added.getPrice());
        assertEquals("women",added.getCategory());
        assertTrue(added.getId() >1);


    }
    @Test
    public void testNegativePriceThrowsInvalidProductException() {

        ProductService productService = new ProductService(new ProductRepository("products.txt"));

        assertThrows(InvalidProductException.class, () -> {
                    productService.addProduct("Product_woman", -2.0, "women");
                });
    }
    @Test
    public void testEmptyNameThrowsInvalidProductException() {

        ProductService productService = new ProductService(new ProductRepository("products.txt"));

        assertThrows(InvalidProductException.class, () -> {
            productService.addProduct("", 2222, "women");
        });
    }

    @Test

    public void testEmptyCategoryThrowsInvalidProductException() {

        ProductService productService = new ProductService(new ProductRepository("products.txt"));

        assertThrows(InvalidProductException.class, () -> {
            productService.addProduct("product", 2222, "");
        });
    }

    @Test
    public void testIsNameValid() {
        ProductService service = new ProductService(new ProductRepository("products.txt"));
        assertTrue(service.isNameValid("Women"));
        assertFalse(service.isNameValid(""));
        assertFalse(service.isNameValid(null));
    }

    @Test
    public void testIsPriceValid() {

        ProductService service = new ProductService(new ProductRepository("products.txt"));
        String invalidInput="ab";

            assertThrows(NumberFormatException.class, () -> {
                double price = Double.parseDouble(invalidInput);
                service.isPriceValid(price);
            });
        assertTrue(service.isPriceValid(0.0));

        assertFalse(service.isPriceValid(-5.0));

    }
    @Test
    public void testIsCategoryValid() {
        ProductService service = new ProductService(new ProductRepository("products.txt"));
        assertTrue(service.isCategoryValid("Women"));
        assertFalse(service.isCategoryValid(""));
        assertFalse(service.isCategoryValid(null));
    }
    @Test
    public void testUpdateProduct(){

        ProductService service = new ProductService(new ProductRepository("products.txt"));

       service.updateProduct(19,"product_Updat_19",1000.0,"men");

        Product updated = service.getProductById(19);

       assertEquals("product_Updat_19",updated.getName());
       assertEquals(1000.0,updated.getPrice());
       assertEquals("men",updated.getCategory());

    }
    @Test
    public void testReplaceProduct(){
        ProductService service = new ProductService(new ProductRepository("products.txt"));
        List<Product> products = new ArrayList<>();
        products.add(new Product(1,"Product_1",300.0,"men"));
        products.add(new Product(2,"Product_2",300.0,"woman"));

        Product updateProduct = new Product(2,"Product_3",100.0,"men");

        boolean result= service.replaceProduct(products,2,updateProduct);

        assertTrue(result);
        assertEquals("Product_1",products.get(0).getName());
        assertEquals("Product_3",products.get(1).getName());
        assertEquals(100.0,products.get(1).getPrice());
        assertEquals("men",products.get(1).getCategory());

    }

}