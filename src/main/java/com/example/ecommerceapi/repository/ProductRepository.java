package com.example.ecommerceapi.repository;

import com.example.ecommerceapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Product> searchByNameOrCategory(@Param("search") String search);

    List<Product> findByStockQuantityGreaterThan(Integer quantity);

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findAllCategories();
}
