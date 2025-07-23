package com.example.ecommerceapi.service;

import com.example.ecommerceapi.entity.*;
import com.example.ecommerceapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    public Order createOrderFromCart(Long userId, String shippingAddress) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartService.getCartByUserId(userId);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Validate stock availability
        for (CartItem cartItem : cart.getItems()) {
            if (!productService.isProductInStock(cartItem.getProduct().getId(), cartItem.getQuantity())) {
                throw new RuntimeException("Product " + cartItem.getProduct().getName() + " is out of stock");
            }
        }

        // Create order
        Order order = new Order(user, cart.getTotalAmount(), shippingAddress);
        order = orderRepository.save(order);

        // Create order items and update stock
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem(order, cartItem.getProduct(),
                                               cartItem.getQuantity(), cartItem.getPrice());
            orderItemRepository.save(orderItem);

            // Update product stock
            productService.updateStock(cartItem.getProduct().getId(), cartItem.getQuantity());
        }

        // Clear cart after successful order
        cartService.clearCart(userId);

        return order;
    }

    public List<Order> getUserOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to cancel this order");
        }

        if (order.getStatus() == Order.OrderStatus.SHIPPED ||
            order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel shipped or delivered order");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);

        // Restore stock
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
        }
    }
}
