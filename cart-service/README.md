# Cart Service

A microservice for managing shopping cart operations in an e-commerce application.

## Features

- **Add to Cart**: Add products to user's shopping cart
- **Update Cart**: Modify item quantities in cart
- **Remove from Cart**: Remove specific items from cart
- **Clear Cart**: Empty entire cart for user
- **View Cart**: Get all items in user's cart
- **Cart Count**: Get total item count in cart

## Architecture

This service follows **Clean Architecture** principles:

- **Interfaces**: Define contracts without implementation (only service interfaces)
- **Service Implementation**: Contains business logic and implements interfaces
- **Controllers**: Handle HTTP requests using service interface references (no interface implementation)
- **Repositories**: Data access layer
- **Entities**: Domain models
- **DTOs**: Data transfer objects

## Tech Stack

- **Java 17**
- **Spring Boot 3.1.0**
- **Spring Data JPA**
- **H2 Database** (development)
- **MySQL** (production)
- **Swagger/OpenAPI** (documentation)
- **Lombok** (boilerplate reduction)
- **Maven** (build tool)

## Project Structure

```
cart-service/
├── src/main/java/com/ecommerce/cartservice/
│   ├── CartServiceApplication.java          # Main application class
│   ├── config/
│   │   └── SwaggerConfig.java              # API documentation config
│   ├── controller/
│   │   └── CartController.java             # REST endpoints
│   ├── dto/
│   │   ├── CartDto.java                    # Cart data transfer object
│   │   ├── CartItemDto.java                # Cart item DTO
│   │   ├── AddToCartRequestDto.java        # Add to cart request
│   │   └── UpdateCartItemRequestDto.java   # Update cart item request
│   ├── entity/
│   │   ├── Cart.java                       # Cart entity
│   │   └── CartItem.java                   # Cart item entity
│   ├── exception/
│   │   ├── CartException.java              # Cart-specific exceptions
│   │   ├── CartItemNotFoundException.java  # Item not found exception
│   │   ├── ErrorResponse.java              # Error response structure
│   │   └── GlobalExceptionHandler.java     # Global error handler
│   ├── interfaces/
│   │   └── CartService.java                # Service interface (controllers don't use interfaces)
│   ├── repository/
│   │   ├── CartRepository.java             # Cart data access
│   │   └── CartItemRepository.java         # Cart item data access
│   └── serviceImpl/
│       └── CartServiceImpl.java            # Service implementation
├── src/main/resources/
│   ├── application.properties              # Default configuration
│   └── application-dev.properties          # Development configuration
├── src/test/resources/
│   └── application-test.properties         # Test configuration
└── pom.xml                                 # Maven dependencies
```

## API Endpoints

### Cart Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cart/user/{userId}` | Get user's cart |
| POST | `/api/cart/add` | Add item to cart |
| PUT | `/api/cart/update` | Update cart item quantity |
| DELETE | `/api/cart/remove/{cartItemId}/user/{userId}` | Remove item from cart |
| DELETE | `/api/cart/clear/{userId}` | Clear entire cart |
| GET | `/api/cart/count/{userId}` | Get cart item count |

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- MySQL (for production)

### Running the Application

1. **Clone and navigate to cart-service**
```bash
cd cart-service
```

2. **Run with Maven**
```bash
mvn spring-boot:run
```

3. **Or run with specific profile**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Testing the API

1. **Access Swagger UI**
   - http://localhost:8082/swagger-ui.html

2. **View API Documentation**
   - http://localhost:8082/api-docs

3. **H2 Console (development)**
   - http://localhost:8082/h2-console

### Example API Calls

**Add to Cart:**
```json
POST /api/cart/add
{
  "userId": 1,
  "productId": 101,
  "productName": "Sample Product",
  "price": 29.99,
  "quantity": 2,
  "imageUrl": "http://example.com/image.jpg"
}
```

**Get Cart:**
```bash
GET /api/cart/user/1
```

**Update Cart Item:**
```json
PUT /api/cart/update
{
  "cartItemId": 1,
  "quantity": 5
}
```

## Database Schema

### Cart Table
- `id` (Primary Key)
- `user_id` (Foreign Key)
- `session_id`
- `total_amount`
- `total_items`
- `is_active`
- `created_at`
- `updated_at`

### Cart Items Table
- `id` (Primary Key)
- `cart_id` (Foreign Key)
- `product_id`
- `product_name`
- `product_description`
- `price`
- `quantity`
- `image_url`
- `added_at`
- `updated_at`

## Configuration

### Profiles

- **default**: H2 in-memory database
- **dev**: MySQL database
- **test**: H2 for testing

### Environment Variables

- `SPRING_PROFILES_ACTIVE`: Set active profile
- `DB_URL`: Database URL
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password

## Error Handling

The service includes comprehensive error handling:

- **Validation Errors**: 400 Bad Request
- **Not Found**: 404 Not Found  
- **Server Errors**: 500 Internal Server Error

All errors return structured JSON responses with timestamps and details.

## Monitoring

- **Health Check**: `/actuator/health`
- **Application Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`

## Development

### Building
```bash
mvn clean install
```

### Running Tests
```bash
mvn test
```

### Packaging
```bash
mvn clean package
```

## Clean Architecture Benefits

✅ **Separation of Concerns**: Each layer has single responsibility  
✅ **Testability**: Easy to mock interfaces for unit testing  
✅ **Flexibility**: Can swap implementations without changing contracts  
✅ **Maintainability**: Clear structure and dependencies  
✅ **SOLID Principles**: Follows dependency inversion principle  

This cart service is production-ready and follows industry best practices! 🚀
