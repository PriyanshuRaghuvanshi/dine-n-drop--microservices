#  Dine N Drop — Microservices-Based Food Delivery & Dining Platform

Dine N Drop is a backend system inspired by real-world food delivery and dining platforms like Swiggy and Zomato.  
It demonstrates a **microservices architecture** with API Gateway, Service Discovery, Authentication, Synchronous & Asynchronous inter-service communication, and containerized deployment.

---

##  Overview

The goal of this project is to bridge the gap between **restaurants** and **consumers** by providing:
-  **Food Delivery** — browse restaurants, view menus, place delivery orders  
-  **Dining Reservations** — book tables for specific date & time slots  
-  **Payment Processing** — handle order payments with success/failure flows  
-  **Notifications** — notify users & restaurants about order status changes  
-  **Secure Access** — authentication and JWT-based authorization

All backend services are built as independent Spring Boot microservices, each responsible for a specific domain, and communicate via REST or RabbitMQ events.

---

##  Microservices Architecture

| Service                | Responsibility                                                                 |
|-------------------------|-------------------------------------------------------------------------------|
| **auth-service**        | User authentication (in-memory users), JWT token generation                   |
| **restaurant-service**  | Manage restaurants, menu items                                                |
| **order-service**       | Manage order creation, validation, async events for payments                  |
| **dining-service**      | Table booking, reservations                                                   |
| **payment-service**     | Process payments (mock), handle success/failure flows                         |
| **notification-service**| Listen to events and send notifications (logged for simulation)               |
| **service-discovery**   | Eureka-based service registry                                                 |
| **api-gateway**         | Spring Cloud Gateway — routing, authentication check, centralized entry point |

---

##  Tech Stack

- **Language / Framework**: Java 17 + Spring Boot 3.x  
- **Database**:
  - PostgreSQL — for `restaurant-service`, `order-service`, `dining-service`
  - In-memory users — for `auth-service`
- **Service Discovery**: Eureka Server  
- **API Gateway**: Spring Cloud Gateway  
- **Message Broker**: RabbitMQ (for inter-service events)  
- **Asynchronous Programming**: CompletableFuture for async REST calls  
- **Containerization**: Docker & Docker Compose (Dockerfile for each service)

---

##  Authentication & Authorization

- In-memory users are stored in `auth-service`.  
- On login (`/auth/login`), a **JWT token** is issued.  
- This token is required for accessing protected endpoints of other services.  
- **API Gateway** intercepts requests, validates the JWT, and then routes them to the appropriate microservice.  
- No authorization roles are implemented (authentication only).

---

##  Inter-Service Communication

- **Synchronous Communication**:  
  - REST API calls between services using `WebClient` (e.g., order-service fetching restaurant details).  
  - Some calls use `CompletableFuture` to improve performance and reduce blocking.

- **Asynchronous Communication** (Event-Driven):  
  - **RabbitMQ** is used between:
    - `order-service` → `payment-service` (payment initiation events)  
    - `payment-service` → `order-service` (payment success/failure events)  
    - `order-service` → `notification-service` (order status change notifications)

This combination improves responsiveness and decouples services for background processing.

---

##  Order Flow (Positive & Negative Scenarios)

###  Positive Flow
1. User logs in → receives JWT.  
2. User selects restaurants & menus.  
3. User places order.  
4. Order validated .  
5. Payment initiated via RabbitMQ.  
6. Payment successful → order confirmed.  
7. Notification sent to user & restaurant.

###  Negative Flow
- **Payment Failure**: payment-service publishes failure event → order marked as cancelled → notification sent.  
- **Restaurant Rejection**: restaurant can reject order → cancellation event published → notification sent.

---

##  Docker & Deployment

### Prerequisites
- Docker & Docker Compose installed
- Ports 8001+, 8080+ available

### Build Images
Each microservice contains its own `Dockerfile`. You can build all images with:

docker-compose build

### Run Entire Stack

docker-compose up

### This will start:

1. Eureka service discovery
2. API Gateway
3. All microservices
4. RabbitMQ broker
5. PostgreSQL containers

### Access Points
Eureka Dashboard → http://localhost:8001
API Gateway → http://localhost:8005
RabbitMQ Management → http://localhost:15672 (guest/guest)


## Local Development (Optional)
Each service can also be run locally using Maven:

cd restaurant-service
mvn spring-boot:run

- Make sure PostgreSQL and RabbitMQ are running locally if not using Docker.

---

##  Future Enhancements

-  **Global Search Functionality**: Implement a unified search service to search across restaurants and menu items efficiently (possibly using Elasticsearch or a custom index).  
-  **IAM & SSO Integration**: Replace or augment the current in-memory auth with a full-fledged IAM solution like **Keycloak**, enabling SSO, centralized user management, and role-based access.  
-  **Environment-Based Configurations**: Introduce environment-specific property files and/or a centralized config server for cleaner deployments across DEV, QA, and PROD.  
-  **Enhanced Logging**: Integrate structured, centralized logging with better trace IDs for easier debugging and observability.  
-  **Improved Exception Handling**: Introduce a global error handling strategy across services for cleaner API responses and better resilience.  
-  **Retry & Resilience**: Strengthen fault tolerance by adding retry mechanisms, bulkheads, and fallback strategies alongside circuit breakers.  
-  **Kubernetes Deployment**: Deploy and scale microservices using Kubernetes and Helm for better scalability and orchestration.

---

##  Author

**Priyanshu Raghuvanshi**  
Backend Developer | Java | Spring Boot | Microservices Architecture

---
