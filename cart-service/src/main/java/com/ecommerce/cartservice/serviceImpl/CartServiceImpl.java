package com.ecommerce.cartservice.serviceImpl;

import com.ecommerce.cartservice.dto.AddToCartRequestDto;
import com.ecommerce.cartservice.dto.CartDto;
import com.ecommerce.cartservice.dto.CartItemDto;
import com.ecommerce.cartservice.dto.UpdateCartItemRequestDto;
import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.entity.CartItem;
import com.ecommerce.cartservice.exception.CartException;
import com.ecommerce.cartservice.exception.CartItemNotFoundException;
import com.ecommerce.cartservice.interfaces.CartService;
import com.ecommerce.cartservice.repository.CartItemRepository;
import com.ecommerce.cartservice.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of CartService interface.
 * This class provides the actual implementation of cart business logic.
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {
    
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public CartDto getCartByUserId(Long userId) {
        Optional<Cart> cartOptional = cartRepository.findByUserIdAndIsActiveTrue(userId);
        
        if (cartOptional.isEmpty()) {
            // Return empty cart if no cart exists
            return createEmptyCartDto(userId);
        }
        
        return convertToCartDto(cartOptional.get());
    }
    
    @Override
    public CartDto addToCart(AddToCartRequestDto addToCartRequest) {
        // Find or create cart for user
        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(addToCartRequest.getUserId())
                .orElseGet(() -> createCartForUser(addToCartRequest.getUserId()));
        
        // Check if item already exists in cart
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(
                cart.getId(), addToCartRequest.getProductId());
        
        if (existingItem.isPresent()) {
            // Update quantity if item already exists
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + addToCartRequest.getQuantity());
            cartItemRepository.save(item);
        } else {
            // Create new cart item
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductId(addToCartRequest.getProductId());
            newItem.setProductName(addToCartRequest.getProductName());
            newItem.setProductDescription(addToCartRequest.getProductDescription());
            newItem.setPrice(addToCartRequest.getPrice());
            newItem.setQuantity(addToCartRequest.getQuantity());
            newItem.setImageUrl(addToCartRequest.getImageUrl());
            
            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }
        
        // Recalculate totals and save cart
        cart.calculateTotals();
        cart = cartRepository.save(cart);
        
        return convertToCartDto(cart);
    }
    
    @Override
    public CartDto updateCartItem(UpdateCartItemRequestDto updateRequest) {
        CartItem cartItem = cartItemRepository.findById(updateRequest.getCartItemId())
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with ID: " + updateRequest.getCartItemId()));
        
        cartItem.setQuantity(updateRequest.getQuantity());
        cartItemRepository.save(cartItem);
        
        // Recalculate cart totals
        Cart cart = cartItem.getCart();
        cart.calculateTotals();
        cart = cartRepository.save(cart);
        
        return convertToCartDto(cart);
    }
    
    @Override
    public CartDto removeFromCart(Long cartItemId, Long userId) {
        CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found or doesn't belong to user"));
        
        Cart cart = cartItem.getCart();
        cart.removeItem(cartItem);
        cartItemRepository.delete(cartItem);
        
        // Recalculate totals and save
        cart.calculateTotals();
        cart = cartRepository.save(cart);
        
        return convertToCartDto(cart);
    }
    
    @Override
    public CartDto clearCart(Long userId) {
        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new CartException("No active cart found for user"));
        
        // Remove all items
        cartItemRepository.deleteByCartId(cart.getId());
        cart.getCartItems().clear();
        cart.calculateTotals();
        cart = cartRepository.save(cart);
        
        return convertToCartDto(cart);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Integer getCartItemCount(Long userId) {
        return cartRepository.getTotalItemsCountByUserId(userId);
    }
    
    @Override
    public Cart createCartForUser(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setIsActive(true);
        return cartRepository.save(cart);
    }
    
    /**
     * Convert Cart entity to CartDto
     */
    private CartDto convertToCartDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setUserId(cart.getUserId());
        cartDto.setSessionId(cart.getSessionId());
        cartDto.setTotalAmount(cart.getTotalAmount());
        cartDto.setTotalItems(cart.getTotalItems());
        cartDto.setIsActive(cart.getIsActive());
        cartDto.setCreatedAt(cart.getCreatedAt());
        cartDto.setUpdatedAt(cart.getUpdatedAt());
        
        // Convert cart items
        List<CartItemDto> cartItemDtos = cart.getCartItems().stream()
                .map(this::convertToCartItemDto)
                .collect(Collectors.toList());
        cartDto.setCartItems(cartItemDtos);
        
        return cartDto;
    }
    
    /**
     * Convert CartItem entity to CartItemDto
     */
    private CartItemDto convertToCartItemDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        dto.setId(cartItem.getId());
        dto.setProductId(cartItem.getProductId());
        dto.setProductName(cartItem.getProductName());
        dto.setProductDescription(cartItem.getProductDescription());
        dto.setPrice(cartItem.getPrice());
        dto.setQuantity(cartItem.getQuantity());
        dto.setImageUrl(cartItem.getImageUrl());
        dto.setAddedAt(cartItem.getAddedAt());
        dto.setUpdatedAt(cartItem.getUpdatedAt());
        return dto;
    }
    
    /**
     * Create empty cart DTO for user
     */
    private CartDto createEmptyCartDto(Long userId) {
        CartDto cartDto = new CartDto();
        cartDto.setUserId(userId);
        cartDto.setTotalAmount(java.math.BigDecimal.ZERO);
        cartDto.setTotalItems(0);
        cartDto.setIsActive(true);
        cartDto.setCartItems(List.of());
        return cartDto;
    }
}
