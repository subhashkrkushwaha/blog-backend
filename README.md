# blog-backend
Blog App Backend
Overview

This is a Spring Boot-based Blog Application backend that provides a secure, scalable, and role-based API for managing blogs. The backend supports user registration, authentication, blog creation, retrieval, and deletion with role-based access control. The application is designed for local MySQL database storage, leverages Redis caching, and comes with Swagger API documentation and unit tests using JUnit and Mockito.

Key Features

User Management

User registration and login.

JWT-based authentication.

Role-based authorization: USER and ADMIN.

Blog Management

Create your own blog posts.

View blogs created by others.

Delete only your own blogs (enforced by security rules).

Security

Spring Security with JWT authentication.

Role-based access control for sensitive operations.

Caching

Redis caching for frequently accessed blog data to improve performance.

API Documentation

Swagger UI for easy testing and documentation of REST APIs.

Testing

Unit tests with JUnit and Mockito for service and controller layers.

Technology Stack
Layer	Technology
Backend	Spring Boot
Security	Spring Security, JWT
Database	MySQL (local)
Caching	Redis
ORM	Spring Data JPA / Hibernate
API Docs	Swagger
Testing	JUnit, Mockito
Build Tool	Maven 
