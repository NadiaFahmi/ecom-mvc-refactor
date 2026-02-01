package test.java.service;

import main.java.exception.InvalidProductException;
import main.java.model.entities.Product;
import main.java.repository.ProductRepository;
import junit.framework.TestCase;
import main.java.service.ProductService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;


public class ProductServiceTest extends TestCase {

    private ProductRepository productRepository;
    private ProductService productService;


    @Before
    public void setUp() {
        productRepository = new ProductRepository("products.txt");
        productService = new ProductService(productRepository);
    }

    @Test
    public void testAddProduct() {
        // Arrange
        String name = "product_testing_newOne";
        double price = 1200.0;
        String category = "women";

        //Act
        productService.addProduct(name, price, category);
        List<Product> products = productService.getAllProducts();
        Product added = products.get(products.size() - 1);

        //Assert
        assertEquals("product_testing_newOne", added.getName());
        assertEquals(1200.0, added.getPrice());
        assertEquals("women", added.getCategory());
        assertTrue(added.getId() > 1);
    }

    @Test
    public void testNegativePriceThrowsInvalidProductException() {

        // Arrange
        String name = "Product_woman";
        double invalidPrice = -2.0;
        String category = "women";

        //Act + Assert
        assertThrows(InvalidProductException.class, () -> {
            productService.addProduct(name, invalidPrice, category);
        });
    }

    @Test
    public void testEmptyNameThrowsInvalidProductException() {

        // Arrange
        String name = "";
        double price = 2222.0;
        String category = "women";

        //Act + Assert
        assertThrows(InvalidProductException.class, () -> {
            productService.addProduct(name, price, category);
        });
    }

    @Test
    public void testEmptyCategoryThrowsInvalidProductException() {

        // Arrange
        String name = "product";
        double price = 2222.0;
        String category = "";

        //Act + Assert
        assertThrows(InvalidProductException.class, () -> {
            productService.addProduct(name, price, category);
        });
    }
    @Test
    public void testValidName() {
        assertTrue( productService.isNameValid("Women"));
    }

    @Test
    public void testEmptyName() {
        assertFalse( productService.isNameValid(""));
    }
    @Test
    public void testNullName() {
        assertFalse( productService.isNameValid(null));

    }

    @Test
    public void testIsPriceValid() {
        // Arrange
        String invalidInput = "ab";

        //Act + Assert
        assertThrows(NumberFormatException.class, () -> {
            double price = Double.parseDouble(invalidInput);
            productService.isPriceValid(price);
        });
        assertTrue(productService.isPriceValid(0.0));

        assertFalse(productService.isPriceValid(-5.0));

    }

    @Test
    public void testValidCategory() {
        assertTrue( productService.isCategoryValid("Women"));
    }

    @Test
    public void testEmptyCategory() {
        assertFalse(productService.isCategoryValid(""));
    }
    @Test
    public void testNullCategory() {
        assertFalse( productService.isCategoryValid(null));

    }

    @Test
    public void testUpdateProduct() {
        // Arrange
        int productId = 19;
        String newName= "product_Updated_19";
        double newPrice= 1000.0;
        String newCategory = "men";

        //Act
        productService.updateProduct(productId,newName,newPrice,newCategory);
        Product updated = productService.getProductById(19);

        //Assert
        assertEquals("product_Updated_19", updated.getName());
        assertEquals(1000.0, updated.getPrice());
        assertEquals("men", updated.getCategory());

    }

    @Test
    public void testReplaceProduct() {

        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product_1", 300.0, "men"));
        products.add(new Product(2, "Product_2", 300.0, "woman"));
        Product updateProduct = new Product(2, "Product_333", 100.0, "men");

        //Act
        boolean result = productService.replaceProduct(products, 2, updateProduct);

        //Assert
        assertTrue(result);
        assertEquals("Product_1", products.get(0).getName());
        assertEquals("Product_333", products.get(1).getName());
        assertEquals(100.0, products.get(1).getPrice());
        assertEquals("men", products.get(1).getCategory());

    }

}