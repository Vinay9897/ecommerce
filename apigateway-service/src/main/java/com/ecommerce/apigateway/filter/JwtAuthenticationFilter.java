package com.ecommerce.apigateway.filter;

import com.ecommerce.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
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
        System.out.println("vinay1");

        // Public endpoints (no token required)
        String pathValue = request.getPath().value();
       
        if (pathValue.startsWith("/api/auth/")) {
            return chain.filter(exchange); // Allow without token for auth service
        }

        System.out.println("vinay2");

        System.out.println("R1");

        String authorization = request.getHeaders().getFirst("Authorization");
        System.out.println("R2");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("R3");

            return unauthorized(exchange.getResponse());
        }
        System.out.println("R4");

        String token = authorization.substring(7);
        System.out.println("R5");

        try {
            Claims claims = jwtUtil.validateToken(token);
            String username = claims.getSubject();
            List<String> roles = jwtUtil.getRolesFromToken(token);

            String path = request.getPath().value();    // http://localhost:8080/api/products/addProduct
            System.out.println("R7 path " + path);
            System.out.println("R7 roles " + roles);
            if (path.equals("/api/products/addProduct") || path.startsWith("/api/products/addProduct/") || path.equals("/api/products/admin")) {
              
                System.out.println("R6");
                boolean isAdmin = roles.stream().anyMatch(r -> r.equalsIgnoreCase("ADMIN") || r.equalsIgnoreCase("ROLE_ADMIN"));
                        System.out.println("R7");
                        if (!isAdmin) {
                            System.out.println("R8");
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