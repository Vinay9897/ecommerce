package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Order Management", description = "APIs for managing orders in the e-commerce system")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "Create a new order", description = "Create a new order with order items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        log.info("POST request to create new order for user: {}", orderDto.getUserId());
        OrderDto createdOrder = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PostMapping("/create-from-cart")
    @Operation(summary = "Create order from cart", description = "Create a new order from user's cart items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<OrderDto> createOrderFromCart(
            @Parameter(description = "User ID", required = true) @RequestParam Long userId,
            @Parameter(description = "Shipping address", required = true) @RequestParam String shippingAddress,
            @Parameter(description = "Billing address", required = true) @RequestParam String billingAddress,
            @Parameter(description = "Order notes") @RequestParam(required = false) String notes) {
        log.info("POST request to create order from cart for user: {}", userId);
        OrderDto createdOrder = orderService.createOrderFromCart(userId, shippingAddress, billingAddress, notes);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }


    @GetMapping("/getOrderById/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order using its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDto> getOrderById(@Parameter(description = "Order ID", required = true) @PathVariable Long id) {
        log.info("GET request to fetch order with id: {}", id);
        OrderDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orderNumber/{orderNumber}")
    @Operation(summary = "Get order by order number", description = "Retrieve a specific order using its order number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDto> getOrderByOrderNumber(@Parameter(description = "Order number", required = true) @PathVariable String orderNumber) {
        log.info("GET request to fetch order with order number: {}", orderNumber);
        OrderDto order = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/getAllOrders")
    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all orders",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class)))
    })
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        log.info("GET request to fetch all orders");
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get orders by user ID", description = "Retrieve all orders for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class)))
    })
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        log.info("GET request to fetch orders for user: {}", userId);
        List<OrderDto> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Retrieve all orders with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class)))
    })
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@Parameter(description = "Order status", required = true) @PathVariable Order.OrderStatus status) {
        log.info("GET request to fetch orders with status: {}", status);
        List<OrderDto> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}/status/{status}")
    @Operation(summary = "Get orders by user ID and status", description = "Retrieve orders for a specific user with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class)))
    })
    public ResponseEntity<List<OrderDto>> getOrdersByUserIdAndStatus(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId,
            @Parameter(description = "Order status", required = true) @PathVariable Order.OrderStatus status) {
        log.info("GET request to fetch orders for user: {} with status: {}", userId, status);
        List<OrderDto> orders = orderService.getOrdersByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get orders by date range", description = "Retrieve orders within a specified date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class)))
    })
    public ResponseEntity<List<OrderDto>> getOrdersByDateRange(
            @Parameter(description = "Start date", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("GET request to fetch orders between {} and {}", startDate, endDate);
        List<OrderDto> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}/date-range")
    @Operation(summary = "Get orders by user ID and date range", description = "Retrieve orders for a specific user within a specified date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class)))
    })
    public ResponseEntity<List<OrderDto>> getOrdersByUserIdAndDateRange(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId,
            @Parameter(description = "Start date", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("GET request to fetch orders for user: {} between {} and {}", userId, startDate, endDate);
        List<OrderDto> orders = orderService.getOrdersByUserIdAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/updateOrderStatus/{id}/status")
    @Operation(summary = "Update order status", description = "Update the status of an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDto> updateOrderStatus(
            @Parameter(description = "Order ID", required = true) @PathVariable Long id,
            @Parameter(description = "New order status", required = true) @RequestParam Order.OrderStatus status) {
        log.info("PUT request to update order status for id: {} to {}", id, status);
        OrderDto updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/updateOrder/{id}")
    @Operation(summary = "Update an existing order", description = "Update order information by providing the order ID and new data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDto> updateOrder(
            @Parameter(description = "Order ID to update", required = true) @PathVariable Long id,
            @Valid @RequestBody OrderDto orderDto) {
        log.info("PUT request to update order with id: {}", id);
        OrderDto updatedOrder = orderService.updateOrder(id, orderDto);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/deleteOrder/{id}")
    @Operation(summary = "Delete an order", description = "Remove an order from the system using its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<Void> deleteOrder(@Parameter(description = "Order ID to delete", required = true) @PathVariable Long id) {
        log.info("DELETE request to delete order with id: {}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
