package com.ecommerce.cartservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long id;

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull(message = "quantity is required")
    private Integer quantity;

    private String productName;

    private String shortDescription;

    private String brand;

    private String material;

    @NotNull(message = "basePrice is required")
    private BigDecimal basePrice;

    private Boolean isReturnable = Boolean.TRUE;

    private String returnPolicy;

    private String longDescription;

    private String imageUrl;



    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

   

}
