package com.ecommerce.productservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8082").description("Local Development Server"),
                        new Server().url("https://api.ecommerce.com").description("Production Server")
                ))
                .info(new Info()
                        .title("Product Service API")
                        .version("1.0.0")
                        .description("REST API for managing products in the e-commerce application. " +
                                "This service provides comprehensive CRUD operations, search functionality, " +
                                "and filtering capabilities for product management.")
                        .contact(new Contact()
                                .name("E-commerce Team")
                                .email("support@ecommerce.com")
                                .url("https://github.com/your-org/ecommerce"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}
