package com.ecommerce.orderservice.serviceImpl;

import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.dto.OrderItemDto;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderItem;
import com.ecommerce.orderservice.exception.OrderNotFoundException;
import com.ecommerce.orderservice.repository.OrderItemRepository;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Creating new order for user: {}", orderDto.getUserId());
        
        Order order = new Order();
        BeanUtils.copyProperties(orderDto, order);
        
        // Generate unique order number if not provided
        if (order.getOrderNumber() == null) {
            order.setOrderNumber("ORD-" + System.currentTimeMillis());
        }
        
        // Calculate total amount if not provided
        if (order.getTotalAmount() == null) {
            order.setTotalAmount(BigDecimal.ZERO);
        }
        
        Order savedOrder = orderRepository.save(order);
        
        // Save order items if provided
        if (orderDto.getOrderItems() != null && !orderDto.getOrderItems().isEmpty()) {
            List<OrderItem> orderItems = orderDto.getOrderItems().stream()
                    .map(itemDto -> {
                        OrderItem item = new OrderItem();
                        BeanUtils.copyProperties(itemDto, item);
                        item.setOrder(savedOrder);
                        return item;
                    })
                    .collect(Collectors.toList());
            
            orderItemRepository.saveAll(orderItems);
            savedOrder.setOrderItems(orderItems);
        }
        
        return convertToDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        log.info("Fetching order by id: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return convertToDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderByOrderNumber(String orderNumber) {
        log.info("Fetching order by order number: {}", orderNumber);
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with order number: " + orderNumber));
        return convertToDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUserId(Long userId) {
        log.info("Fetching orders for user: {}", userId);
        return orderRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByStatus(Order.OrderStatus status) {
        log.info("Fetching orders with status: {}", status);
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUserIdAndStatus(Long userId, Order.OrderStatus status) {
        log.info("Fetching orders for user: {} with status: {}", userId, status);
        return orderRepository.findByUserIdAndStatus(userId, status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching orders between {} and {}", startDate, endDate);
        return orderRepository.findOrdersByDateRange(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching orders for user: {} between {} and {}", userId, startDate, endDate);
        return orderRepository.findOrdersByUserIdAndDateRange(userId, startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto updateOrderStatus(Long id, Order.OrderStatus status) {
        log.info("Updating order status for id: {} to {}", id, status);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return convertToDto(updatedOrder);
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        log.info("Updating order with id: {}", id);
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        
        BeanUtils.copyProperties(orderDto, existingOrder, "id", "orderNumber", "createdAt");
        existingOrder.setUpdatedAt(LocalDateTime.now());
        
        Order updatedOrder = orderRepository.save(existingOrder);
        return convertToDto(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        log.info("Deleting order with id: {}", id);
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public OrderDto createOrderFromCart(Long userId, String shippingAddress, String billingAddress, String notes) {
        log.info("Creating order from cart for user: {}", userId);
        
        // This would typically integrate with cart service to get cart items
        // For now, we'll create a basic order structure
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(userId);
        orderDto.setShippingAddress(shippingAddress);
        orderDto.setBillingAddress(billingAddress);
        orderDto.setNotes(notes);
        orderDto.setStatus(Order.OrderStatus.PENDING);
        orderDto.setTotalAmount(BigDecimal.ZERO);
        
        return createOrder(orderDto);
    }

    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order, orderDto);
        
        if (order.getOrderItems() != null) {
            List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                    .map(this::convertItemToDto)
                    .collect(Collectors.toList());
            orderDto.setOrderItems(orderItemDtos);
        }
        
        return orderDto;
    }

    private OrderItemDto convertItemToDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        BeanUtils.copyProperties(orderItem, orderItemDto);
        if (orderItem.getOrder() != null) {
            orderItemDto.setOrderId(orderItem.getOrder().getId());
        }
        return orderItemDto;
    }
}
