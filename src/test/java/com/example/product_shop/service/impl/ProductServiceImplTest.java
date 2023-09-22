package com.example.product_shop.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.product_shop.model.Product;
import com.example.product_shop.repository.ProductRepository;
import com.example.product_shop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
public class ProductServiceImplTest {
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void testAddProduct_ValidProduct_ReturnsSavedProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Sample Product");
        product.setPrice(10.0);
        product.setQuantity(5);

        when(productRepository.save(any())).thenReturn(product);

        Product savedProduct = productService.addProduct(product);

        assertNotNull(savedProduct);
        assertEquals(1L, savedProduct.getId());
        assertEquals("Sample Product", savedProduct.getName());
        assertEquals(10.0, savedProduct.getPrice(), 0.001);
        assertEquals(5, savedProduct.getQuantity());
    }

    @Test
    public void testAddProduct_NullProduct_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productService.addProduct(null));
    }

    @Test
    public void testGetAllProducts_ReturnsListOfProducts() {
        List<Product> productList = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(10.0);
        product1.setQuantity(5);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(20.0);
        product2.setQuantity(3);

        productList.add(product1);
        productList.add(product2);

        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
    }

    @Test
    public void testGetProductById_ExistingProductId_ReturnsProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Sample Product");
        product.setPrice(10.0);
        product.setQuantity(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Sample Product", result.getName());
        assertEquals(10.0, result.getPrice(), 0.001);
        assertEquals(5, result.getQuantity());
    }

    @Test
    public void testGetProductById_NonExistingProductId_ThrowsNoSuchElementException() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.getProductById(2L));
    }
}