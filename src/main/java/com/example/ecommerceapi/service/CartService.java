package com.example.ecommerceapi.service;

import com.example.ecommerceapi.entity.*;
import com.example.ecommerceapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> createCartForUser(userId));
    }

    private Cart createCartForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = new Cart(user);
        return cartRepository.save(cart);
    }

    public Cart addItemToCart(Long userId, Long productId, Integer quantity) {
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock available");
        }

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;

            if (product.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock available");
            }

            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(cart, product, quantity, product.getPrice());
            cartItemRepository.save(newItem);
        }

        // Return fresh cart with all items
        return getCartByUserId(userId);
    }

    public Cart updateCartItem(Long userId, Long productId, Integer quantity) {
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        if (quantity <= 0) {
            cartItemRepository.delete(item);
            cart.getItems().remove(item);
        } else {
            if (product.getStockQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock available");
            }
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        cartItemRepository.delete(item);
        cart.getItems().remove(item);

        return cartRepository.save(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public BigDecimal getCartTotal(Long userId) {
        Cart cart = getCartByUserId(userId);
        return cart.getTotalAmount();
    }
}
