package com.ecommerce.orderservice.dto;

import com.ecommerce.orderservice.entity.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    @NotNull(message = "userId is required")
    private Long userId;

    private String orderNumber;

    private Order.OrderStatus status;

    @NotNull(message = "totalAmount is required")
    private BigDecimal totalAmount;

    private BigDecimal discountAmount = BigDecimal.ZERO;

    private BigDecimal taxAmount = BigDecimal.ZERO;

    private BigDecimal shippingAmount = BigDecimal.ZERO;   

    @NotBlank(message = "shippingAddress is required")
    private String shippingAddress;

    @NotBlank(message = "billingAddress is required")
    private String billingAddress;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<OrderItemDto> orderItems;
}
