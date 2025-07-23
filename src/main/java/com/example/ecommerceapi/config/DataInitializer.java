package com.example.ecommerceapi.config;

import com.example.ecommerceapi.entity.Product;
import com.example.ecommerceapi.entity.User;
import com.example.ecommerceapi.repository.ProductRepository;
import com.example.ecommerceapi.repository.UserRepository;
import com.example.ecommerceapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample products
        if (productRepository.count() == 0) {
            initializeProducts();
        }

        // Initialize admin user
        initializeUsers();
    }

    private void initializeProducts() {
        // Electronics
        productRepository.save(new Product("iPhone 14", "Latest Apple smartphone with advanced features",
            new BigDecimal("999.99"), "Electronics", 50));
        productRepository.save(new Product("Samsung Galaxy S23", "Premium Android smartphone",
            new BigDecimal("899.99"), "Electronics", 30));
        productRepository.save(new Product("MacBook Pro", "High-performance laptop for professionals",
            new BigDecimal("1999.99"), "Electronics", 20));
        productRepository.save(new Product("Dell XPS 13", "Ultrabook with excellent display",
            new BigDecimal("1299.99"), "Electronics", 25));
        productRepository.save(new Product("Sony WH-1000XM4", "Noise-canceling wireless headphones",
            new BigDecimal("349.99"), "Electronics", 40));

        // Clothing
        productRepository.save(new Product("Nike Air Max", "Comfortable running shoes",
            new BigDecimal("129.99"), "Clothing", 100));
        productRepository.save(new Product("Levi's 501 Jeans", "Classic denim jeans",
            new BigDecimal("79.99"), "Clothing", 80));
        productRepository.save(new Product("Adidas T-Shirt", "Cotton sports t-shirt",
            new BigDecimal("29.99"), "Clothing", 150));
        productRepository.save(new Product("North Face Jacket", "Waterproof outdoor jacket",
            new BigDecimal("199.99"), "Clothing", 60));

        // Books
        productRepository.save(new Product("Clean Code", "A handbook of agile software craftsmanship",
            new BigDecimal("39.99"), "Books", 200));
        productRepository.save(new Product("Spring Boot in Action", "Complete guide to Spring Boot",
            new BigDecimal("49.99"), "Books", 150));
        productRepository.save(new Product("Effective Java", "Best practices for Java programming",
            new BigDecimal("44.99"), "Books", 120));

        // Home & Garden
        productRepository.save(new Product("Coffee Maker", "Programmable drip coffee maker",
            new BigDecimal("89.99"), "Home & Garden", 75));
        productRepository.save(new Product("Plant Pot Set", "Ceramic pots for indoor plants",
            new BigDecimal("24.99"), "Home & Garden", 90));
        productRepository.save(new Product("LED Desk Lamp", "Adjustable brightness desk lamp",
            new BigDecimal("59.99"), "Home & Garden", 110));
    }

    private void initializeUsers() {
        try {
            // Create admin user if not exists
            userService.registerUser("admin", "admin@example.com", "admin123", User.Role.ADMIN);

            // Create sample customer
            userService.registerUser("customer1", "customer@example.com", "customer123", User.Role.CUSTOMER);

            System.out.println("Sample users created:");
            System.out.println("Admin: username=admin, password=admin123");
            System.out.println("Customer: username=customer1, password=customer123");
        } catch (Exception e) {
            // Users might already exist
            System.out.println("Sample users might already exist: " + e.getMessage());
        }
    }
}
