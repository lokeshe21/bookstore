package com.bookstore.applicaton.repository;

import com.bookstore.applicaton.domain.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public interface ProductRepository {

    Product addProduct(Product product);

    Product getProduct(Integer productId);

    boolean updateProduct(Integer productId, Product updatedProduct);

    boolean deleteProduct(Integer productId);

    List<Product> getAllProducts();  // New method to retrieve all products
}


