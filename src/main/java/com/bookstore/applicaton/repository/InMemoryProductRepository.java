package com.bookstore.applicaton.repository;

import com.bookstore.applicaton.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<Integer, Product> productMap = new ConcurrentHashMap<>();

    @Override
    public Product addProduct(Product product) {
        productMap.put(product.getProductId(), product);
        return product;
    }

    @Override
    public Product getProduct(Integer productId) {
        return productMap.get(productId);
    }

    @Override
    public boolean updateProduct(Integer productId, Product updatedProduct) {
        if (productMap.containsKey(productId)) {
            productMap.put(productId, updatedProduct);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteProduct(Integer productId) {
        return productMap.remove(productId) != null;
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(productMap.values());
    }
}