package com.example.product_shop.service.impl;

import com.example.product_shop.model.Product;
import com.example.product_shop.repository.ProductRepository;
import com.example.product_shop.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        if (product != null) {
            return productRepository.save(product);
        }
        throw new IllegalArgumentException("Product can't be null.");
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found by id" + productId));
    }
}
