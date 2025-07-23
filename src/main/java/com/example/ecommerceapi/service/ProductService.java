package com.example.ecommerceapi.service;

import com.example.ecommerceapi.entity.Product;
import com.example.ecommerceapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProducts(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.searchByNameOrCategory(searchTerm.trim());
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findByStockQuantityGreaterThan(0);
    }

    // Admin methods
    public Product createProduct(String name, String description, BigDecimal price,
                               String category, Integer stockQuantity, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setStockQuantity(stockQuantity);
        product.setImageUrl(imageUrl);

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, String name, String description, BigDecimal price,
                               String category, Integer stockQuantity, String imageUrl) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (name != null) product.setName(name);
        if (description != null) product.setDescription(description);
        if (price != null) product.setPrice(price);
        if (category != null) product.setCategory(category);
        if (stockQuantity != null) product.setStockQuantity(stockQuantity);
        if (imageUrl != null) product.setImageUrl(imageUrl);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public boolean isProductInStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return product.getStockQuantity() >= quantity;
    }

    public void updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int newStock = product.getStockQuantity() - quantity;
        if (newStock < 0) {
            throw new RuntimeException("Insufficient stock");
        }

        product.setStockQuantity(newStock);
        productRepository.save(product);
    }
}
