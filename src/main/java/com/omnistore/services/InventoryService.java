package com.omnistore.services;

import com.omnistore.entity.Product;
import com.omnistore.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ---------------- UPDATE STOCK ----------------
    @Transactional
    public Product updateStock(Long productId, Integer newStock) {

        if (newStock == null || newStock < 0) {
            throw new RuntimeException("Stock cannot be null or negative");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStock(newStock);

        return productRepository.save(product);
    }

    // ---------------- GET PRODUCT BY ID ----------------
    public Product getProductById(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
