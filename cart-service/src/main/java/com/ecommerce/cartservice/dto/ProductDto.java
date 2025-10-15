package com.ecommerce.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    private String productName;

    private String shortDescription;

    private String brand;

    private String material;

    private BigDecimal basePrice;

    private Boolean isReturnable;

    private String returnPolicy;

    private String longDescription;

    private String imageUrl;
}


