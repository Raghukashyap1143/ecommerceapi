package com.example.ecommerceapi.controller;

import com.example.ecommerceapi.entity.Order;
import com.example.ecommerceapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            Order order = orderService.createOrderFromCart(request.getUserId(), request.getShippingAddress());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Order created successfully");
            response.put("order", order);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserOrders(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId)
                .map(order -> ResponseEntity.ok(order))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId, @RequestParam Long userId) {
        try {
            orderService.cancelOrder(orderId, userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Order cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Admin only endpoints
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/admin/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId,
                                             @RequestBody UpdateOrderStatusRequest request) {
        try {
            Order order = orderService.updateOrderStatus(orderId, request.getStatus());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Order status updated successfully");
            response.put("order", order);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // DTOs
    public static class CreateOrderRequest {
        private Long userId;
        private String shippingAddress;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getShippingAddress() { return shippingAddress; }
        public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    }

    public static class UpdateOrderStatusRequest {
        private Order.OrderStatus status;

        public Order.OrderStatus getStatus() { return status; }
        public void setStatus(Order.OrderStatus status) { this.status = status; }
    }
}
