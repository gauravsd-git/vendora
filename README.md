# Vendora Backend

Backend REST API for Vendora Multi-Store Retail Management System.

Built using Spring Boot, Spring Security, JWT Authentication, Spring Data JPA, Hibernate, and MySQL.

Provides secure APIs for authentication, store management, products, orders, payments, analytics, and user management.

---

# Features

## Authentication
- Signup
- Login
- JWT Token Generation
- Role Based Authorization

## User Roles

### ADMIN
- Manage Stores
- Moderate Store Status
- View Platform Data

### STORE_ADMIN
- Manage Own Store
- Products Management
- Cashier Management
- Orders Management

### CASHIER
- Order Creation
- Billing
- Payments

---

# Modules

## Auth APIs
- POST /auth/signup
- POST /auth/login

## User APIs
- GET /api/users/profile
- POST /api/users/cashier
- GET /api/users/cashiers

## Store APIs
- GET /api/stores
- POST /api/stores
- PUT /api/stores/{id}
- DELETE /api/stores/{id}

## Product APIs
- GET /api/products
- POST /api/products
- PUT /api/products/{id}
- DELETE /api/products/{id}

## Order APIs
- GET /api/orders
- POST /api/orders

## Payment APIs
- POST /api/payments

## Analytics APIs
- GET /api/analytics/total-sales
- GET /api/analytics/top-products

---

# Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- MySQL
- Maven

---

# Project Structure

```txt
src/main/java/
 ├── controller/
 ├── service/
 ├── service/impl/
 ├── repository/
 ├── model/
 ├── mapper/
 └── configuration/
