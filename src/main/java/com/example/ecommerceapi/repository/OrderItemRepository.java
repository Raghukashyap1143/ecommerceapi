package com.example.ecommerceapi.repository;

import com.example.ecommerceapi.entity.OrderItem;
import com.example.ecommerceapi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
}
