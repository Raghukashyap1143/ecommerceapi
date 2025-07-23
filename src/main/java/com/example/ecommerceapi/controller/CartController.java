package com.example.ecommerceapi.controller;

import com.example.ecommerceapi.entity.Cart;
import com.example.ecommerceapi.service.CartService;
import com.example.ecommerceapi.service.UserService;
import com.example.ecommerceapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private Long getUserIdFromToken(String token) {
        String username = jwtUtil.extractUsername(token.substring(7)); // Remove "Bearer "
        return userService.getUserIdByUsername(username);
    }

    @GetMapping
    public ResponseEntity<?> getCart(@RequestHeader("Authorization") String token) {
        try {
            Long userId = getUserIdFromToken(token);
            Cart cart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestHeader("Authorization") String token,
                                         @RequestBody AddToCartRequest request) {
        try {
            Long userId = getUserIdFromToken(token);
            Cart cart = cartService.addItemToCart(userId,
                                                request.getProductId(),
                                                request.getQuantity());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Item added to cart successfully");
            response.put("cart", cart);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestHeader("Authorization") String token,
                                          @RequestBody UpdateCartRequest request) {
        try {
            Long userId = getUserIdFromToken(token);
            Cart cart = cartService.updateCartItem(userId,
                                                 request.getProductId(),
                                                 request.getQuantity());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cart updated successfully");
            response.put("cart", cart);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeItemFromCart(@RequestHeader("Authorization") String token,
                                              @RequestParam Long productId) {
        try {
            Long userId = getUserIdFromToken(token);
            Cart cart = cartService.removeItemFromCart(userId, productId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Item removed from cart successfully");
            response.put("cart", cart);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestHeader("Authorization") String token) {
        try {
            Long userId = getUserIdFromToken(token);
            cartService.clearCart(userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cart cleared successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // DTOs
    public static class AddToCartRequest {
        private Long productId;
        private Integer quantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public static class UpdateCartRequest {
        private Long productId;
        private Integer quantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}
