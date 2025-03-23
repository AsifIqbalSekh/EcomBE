# E-commerce Backend API :  EcomBE

## Overview
This is a backend service for an E-commerce application built using **Java, Spring Boot, MySQL, and Hibernate**. It provides a RESTful API for managing authentication, products, categories, orders, carts, and user addresses.

##  Tech Stack
- **Backend:** Java, Spring Boot, Spring Security, Spring Data JPA
- **Database:** MySQL
- **ORM:** Hibernate
- **Security:** JWT Authentication
- **Cloud Technology** AWS EBS or ECS, Docker
- **API Testing:** Postman Collection

## Features Implements:

- Comprehensive CRUD Operations for categories, products, users, carts, and orders.
- JWT-Based Authentication & Role Management to secure API endpoints.
- Product & Category Management with search and filtering capabilities.
- Shopping Cart System supporting product additions, quantity updates, and deletions.
- Order Processing Workflow for placing and tracking user orders.
- User Address Management for storing and retrieving multiple addresses.
- Custom Error Handling to gracefully manage request failures.
- File Handling to manage product images and other media assets.
- Logging & Monitoring to track API usage and requests.
- Deployment configuration to AWS EBS & ECS using Docker

##  Setup & Installation

### Prerequisites
- Java 21+
- MySQL
- Postman (for API testing)
- Maven

### Steps to Run Locally
```sh
# Clone the repository
git clone https://github.com/AsifIqbalSekh/EcomBE.git
cd EcomBE

# Configure the database in `application.properties`
# Example:
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=yourpassword

# Build and run the application
mvn clean install
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080/api`

For AWS Cloud Deployment please modify the buildspec.yml file according to your AWS infra.

For API testing, download the postman collection.

##  API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `{{base_url}}/auth/signin` | SignIn |
| `GET` | `{{base_url}}/auth/userinfo` | UserDetails |
| `POST` | `{{base_url}}/auth/signup` | SignUp |
| `GET` | `{{base_url}}/auth/admin/promote-admin/asif` | PromoteAdmin |
| `GET` | `{{base_url}}/auth/test` | Test Endpoint |

### Categories

| Method | Endpoint | Description |
|--------|---------|-------------|
| `GET` | `{{base_url}}/public/category` | Get data |
| `POST` | `{{base_url}}/admin/category` | Create category |
| `DELETE` | `{{base_url}}/admin/category/2` | Delete data |
| `PUT` | `{{base_url}}/admin/category/1` | Update data |

### Product

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `{{base_url}}/admin/categories/1/product` | Create product |
| `PUT` | `{{base_url}}/admin/product/1` | Update product |
| `DELETE` | `{{base_url}}/admin/product/2` | Delete Product |
| `PUT` | `{{base_url}}/admin/product/2/image` | Update product Image |
| `GET` | `{{base_url}}/public/product` | get All Product |
| `GET` | `{{base_url}}/public/categories/1/product` | get All Product by Catgeory |
| `GET` | `{{base_url}}/public/product/search/m` | get All Product by Keyword |

### Cart

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `{{base_url}}/carts/products/2/quantity/1` | AddProductToCart |
| `PUT` | `{{base_url}}/carts/products/2/operation/delete` | UpdateProductQuantity in Cart |
| `DELETE` | `{{base_url}}/carts/1/products/1` | DeleteProductFrom Cart |
| `GET` | `{{base_url}}/carts` | GetAllCarts |
| `GET` | `{{base_url}}/carts/users/cart` | GetUserCart |

### Address

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `{{base_url}}/addresses` | Add Address |
| `PUT` | `{{base_url}}/addresses/1` | Update address By ID |
| `GET` | `{{base_url}}/addresses` | GetAllAddresses |
| `GET` | `{{base_url}}/addresses/7` | Get Address By ID |
| `GET` | `{{base_url}}/addresses/user` | Address for Logged In User |
| `DELETE` | `{{base_url}}/addresses/8` | Delete Address By ID |

### Order

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `{{base_url}}/order/place-order` | PlaceOrder |
| `GET` | `{{base_url}}/order/order-history/user` | GetOrderByUser |



## Using Postman Collection
1. Import the **Postman Collection** into Postman.
2. Set the `base_url` variable to `http://localhost:8080/api`.
3. Authenticate using the `SignIn` API to get a **JWT Token**.
4. Set the token in the **Authorization** header as `Bearer <token>`.
5. Test various endpoints.

## Contributing
Feel free to submit pull requests to improve this project.
