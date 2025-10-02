package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.dto.AddToCartRequestDto;
import com.ecommerce.cartservice.dto.CartDto;
import com.ecommerce.cartservice.dto.UpdateCartItemRequestDto;
import com.ecommerce.cartservice.exception.CartException;
import com.ecommerce.cartservice.exception.CartItemNotFoundException;
import com.ecommerce.cartservice.interfaces.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for cart operations.
 * This controller uses CartService interface reference for dependency injection.
 * Controllers should not implement interfaces - only service layer implements interfaces.
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    
    private final CartService cartService; // Interface reference for dependency injection
    
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        try {
            CartDto cart = cartService.getCartByUserId(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("cart", cart);
            response.put("message", "Cart retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve cart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@Valid @RequestBody AddToCartRequestDto addToCartRequest) {
        try {
            CartDto cart = cartService.addToCart(addToCartRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("cart", cart);
            response.put("message", "Item added to cart successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CartException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to add item to cart");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@Valid @RequestBody UpdateCartItemRequestDto updateRequest) {
        try {
            CartDto cart = cartService.updateCartItem(updateRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("cart", cart);
            response.put("message", "Cart item updated successfully");
            return ResponseEntity.ok(response);
        } catch (CartItemNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update cart item");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/remove/{cartItemId}/user/{userId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long cartItemId, @PathVariable Long userId) {
        try {
            CartDto cart = cartService.removeFromCart(cartItemId, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("cart", cart);
            response.put("message", "Item removed from cart successfully");
            return ResponseEntity.ok(response);
        } catch (CartItemNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to remove item from cart");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        try {
            CartDto cart = cartService.clearCart(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("cart", cart);
            response.put("message", "Cart cleared successfully");
            return ResponseEntity.ok(response);
        } catch (CartException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to clear cart");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/count/{userId}")
    public ResponseEntity<?> getCartItemCount(@PathVariable Long userId) {
        try {
            Integer count = cartService.getCartItemCount(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("count", count);
            response.put("message", "Cart item count retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get cart item count");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
