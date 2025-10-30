package com.ecommerce.apigatewayservice.config.filter;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus; 
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.ecommerce.apigatewayservice.config.util.JwtUtil;

import reactor.core.publisher.Mono;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // String path = request.getPath().value();

        // Public endpoints (no token required)
        String pathValue = request.getPath().value();
       
        if (pathValue.startsWith("/api/auth/")) {
            return chain.filter(exchange); // Allow without token for auth service
        }

        String authorization = request.getHeaders().getFirst("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
           

            return unauthorized(exchange.getResponse());
        }
      
        String token = authorization.substring(7);
      
        try {

            Claims claims = jwtUtil.validateToken(token);
            String username = claims.getSubject(); 
            List<String> roles = jwtUtil.getRolesFromToken(token);

            String path = request.getPath().value();    // http://localhost:8080/api/products/addProduct
            if (path.equals("/api/products/addProduct") || path.startsWith("/api/products/addProduct/") 
            || path.equals("/api/products/admin")
            || path.equals("/api/products/updateProduct/") || path.startsWith("/api/products/updateProduct/") 
            || path.equals("/api/products/deleteProduct/") || path.startsWith("/api/products/deleteProduct/")
            || path.equals("/api/orders/") || path.startsWith("/api/orders/")
            || path.equals("/api/orders/getOrderById/") || path.startsWith("/api/orders/getOrderById/")
            || path.equals("/api/orders/orderNumber/") || path.startsWith("/api/orders/orderNumber/")
            || path.equals("/api/orders/user/") || path.startsWith("/api/orders/user/")
            || path.equals("/api/orders/status/") || path.startsWith("/api/orders/status/")
            || path.equals("/api/orders/updateOrderStatus/") || path.startsWith("/api/orders/updateOrderStatus/")
            || path.equals("/api/orders/updateOrder/") || path.startsWith("/api/orders/updateOrder/")
            || path.equals("/api/orders/deleteOrder/") || path.startsWith("/api/orders/deleteOrder/")
            || path.equals("/api/inventory/") || path.startsWith("/api/inventory/")
            || path.equals("/api/inventory/getInventoryById/") || path.startsWith("/api/inventory/getInventoryById/")
            || path.equals("/api/inventory/product/") || path.startsWith("/api/inventory/product/")
            || path.equals("/api/inventory/updateInventory/") || path.startsWith("/api/inventory/updateInventory/")


 
            ) {
              
                boolean isAdmin = roles.stream().anyMatch(r -> r.equalsIgnoreCase("ADMIN") || r.equalsIgnoreCase("ROLE_ADMIN"));
                        if (!isAdmin) {
                    return unauthorized(exchange.getResponse());
                    // return forbidden(exchange.getResponse());
                }
            }

            // Require USER role for all cart service endpoints
            if (path.startsWith("/api/cart/")) {  
                boolean isUser = roles.stream()
                        .anyMatch(r -> r.equalsIgnoreCase("USER") || r.equalsIgnoreCase("ROLE_USER"));
                if (!isUser) {
                    return forbidden(exchange.getResponse());
                }
            } 

            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Name", username != null ? username : "")
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception ex) {
            return unauthorized(exchange.getResponse());
        }
    }
  
    private Mono<Void> unauthorized(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private Mono<Void> forbidden(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // Ensure this runs early
    }
}