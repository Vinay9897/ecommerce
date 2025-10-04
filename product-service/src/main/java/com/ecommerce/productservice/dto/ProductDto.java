package com.ecommerce.productservice.dto;

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
public class ProductDto {

    private Long id;

    @NotNull(message = "catalogueId is required")
    private Long catalogueId;

    @NotBlank(message = "catalogueName is required")
    private String catalogueName;

    @NotNull(message = "subCatalogueId is required")
    private Long subCatalogueId;

    @NotBlank(message = "subCatalogueName is required")
    private String subCatalogueName;

    @NotBlank(message = "productName is required")
    private String productName;

    private String shortDescription;

    private String brand;

    private String material;

    @NotNull(message = "basePrice is required")
    private BigDecimal basePrice;

    private Boolean isReturnable;

    private String returnPolicy;

    private String longDescription;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
