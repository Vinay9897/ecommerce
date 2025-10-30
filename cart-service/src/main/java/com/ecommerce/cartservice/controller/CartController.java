package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.dto.CartDto;
import com.ecommerce.cartservice.dto.ProductDto;
import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.repository.CartRepository;
import com.ecommerce.cartservice.service.ProductClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {

    private final CartRepository cartRepository;
    private final ProductClient productClient;

    
    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestParam Long userId,
                                          @RequestParam Long productId,
                                          @RequestParam(defaultValue = "1") Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Cart existing = cartRepository.findByUserIdAndProductId(userId, productId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            Cart updated = cartRepository.save(existing);
            return ResponseEntity.ok(updated);
        }

        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setProductId(productId);
        newCart.setQuantity(quantity);
        Cart created = cartRepository.save(newCart);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/findCartByCartId/{cartId}")
    public ResponseEntity<Cart> findCartByCartId(@PathVariable Long cartId) {
        return cartRepository.findById(cartId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/findAllCartOfSpecificUser/{userId}")
    public ResponseEntity<List<CartDto>> findAllCartOfSpecificUser(@PathVariable Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        List<CartDto> result = carts.stream().map(cart -> {
            ProductDto product = productClient.getProductById(cart.getProductId());
            CartDto dto = new CartDto();
            dto.setId(cart.getId());
            dto.setUserId(cart.getUserId());
            dto.setProductId(cart.getProductId());
            dto.setQuantity(cart.getQuantity());
            if (product != null) {
                dto.setProductName(product.getProductName());
                dto.setShortDescription(product.getShortDescription());
                dto.setBrand(product.getBrand());
                dto.setMaterial(product.getMaterial());
                dto.setBasePrice(product.getBasePrice());
                dto.setIsReturnable(product.getIsReturnable());
                dto.setReturnPolicy(product.getReturnPolicy());
                dto.setLongDescription(product.getLongDescription());
                dto.setImageUrl(product.getImageUrl());
            }
            return dto;
        }).toList();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/updateCart/{cartId}")
    public ResponseEntity<Cart> updateCart(@PathVariable Long cartId, @Valid @RequestBody Cart cart) {
        return cartRepository.findById(cartId)
                .map(existingCart -> {
                    existingCart.setUserId(cart.getUserId());
                    existingCart.setProductId(cart.getProductId());
                    existingCart.setQuantity(cart.getQuantity());
                    Cart updated = cartRepository.save(existingCart);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteCartByCartId/{cartId}")
    public ResponseEntity<Void> deleteCartByCartId(@PathVariable Long cartId) {
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
