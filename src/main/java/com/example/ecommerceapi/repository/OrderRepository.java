package com.example.ecommerceapi.repository;

import com.example.ecommerceapi.entity.Order;
import com.example.ecommerceapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByUserId(Long userId);
    List<Order> findByUserOrderByOrderDateDesc(User user);
}
