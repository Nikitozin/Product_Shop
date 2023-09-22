package com.example.product_shop.service;

import com.example.product_shop.model.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);

    List<Product> getAllProducts();

    Product getProductById(Long productId);
}
