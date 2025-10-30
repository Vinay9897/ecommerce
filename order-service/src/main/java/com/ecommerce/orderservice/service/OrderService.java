package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

    OrderDto getOrderById(Long id);

    OrderDto getOrderByOrderNumber(String orderNumber);

    List<OrderDto> getAllOrders();

    List<OrderDto> getOrdersByUserId(Long userId);

    List<OrderDto> getOrdersByStatus(Order.OrderStatus status);

    List<OrderDto> getOrdersByUserIdAndStatus(Long userId, Order.OrderStatus status);

    List<OrderDto> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<OrderDto> getOrdersByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    OrderDto updateOrderStatus(Long id, Order.OrderStatus status);

    OrderDto updateOrder(Long id, OrderDto orderDto);

    void deleteOrder(Long id);

    OrderDto createOrderFromCart(Long userId, String shippingAddress, String billingAddress, String notes);
}
