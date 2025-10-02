package com.ecommerce.cartservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for the cart service.
 * This class configures API documentation.
 */
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI cartServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cart Service API")
                        .description("REST API for E-commerce Cart Management")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("E-commerce Team")
                                .email("team@ecommerce.com")
                                .url("https://ecommerce.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
