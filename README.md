# Microservices Architecture

This repository implements a microservices architecture using Spring Boot 3.0.2, Spring Cloud 2022.0.4 and Java 17.

---

## Architecture

```
Gateway (8083) → Auth (8084) / Customer (8081) / Fraud (8089) / Notification (8086)
                      ↓
              Eureka (8761) | PostgreSQL (5432) | RabbitMQ (5672) | Redis (6379) | Zipkin (9411)
```

### Services

| Service       | Port | Responsibility                           |
|---------------|------|------------------------------------------|
| gateway       | 8083 | API Gateway                              |
| auth          | 8084 | JWT authentication and blacklist (Redis) |
| customer      | 8081 | Customer management                      |
| fraud         | 8089 | Anti-fraud validation                    |
| notification  | 8086 | Notifications (RabbitMQ consumer)        |
| eureka-server | 8761 | Service Discovery                        |
| clients       | -    | Shared library (DTOs, Feign clients)     |

### Infrastructure

- PostgreSQL (database per service)
- Redis (token blacklist)
- RabbitMQ (event-driven messaging)
- Zipkin (distributed tracing)

---

## Technology Stack

Java 17 | Spring Boot 3.0.2 | Spring Cloud 2022.0.4 | PostgreSQL | Redis | RabbitMQ | Docker | Maven

---

## Quick Start

1. Start infrastructure services

```bash
# From repository root
docker-compose up -d
```

2. Build the projects

```bash
mvn clean install -DskipTests
```

3. Run services (recommended order)

```bash
cd eureka-server && mvn spring-boot:run    # 8761
cd auth && mvn spring-boot:run             # 8084
cd customer && mvn spring-boot:run         # 8081
cd fraud && mvn spring-boot:run            # 8089
```

Dashboards and UIs:

- Eureka: http://localhost:8761
- RabbitMQ management: http://localhost:15672 (guest/guest)
- PgAdmin: http://localhost:5050
- Zipkin: http://localhost:9411

---

## JWT Authentication and Redis Blacklist

### Overview

Clients call protected services (for example `customer` or `fraud`), which validate tokens by delegating to the Auth
service. The Auth service maintains a Redis-backed blacklist to immediately revoke tokens on logout.

```
Client → Service (customer/fraud)
            ↓ validateToken()
         JwtTokenValidator
            ↓ FeignClient
         Auth Service
            ↓ isBlacklisted()
         Redis Blacklist ← Logout adds token
```

### Characteristics

- Access token validity: 15 minutes
- Refresh token validity: 7 days
- Logout invalidates tokens immediately by adding them to Redis (TTL applies)
- All services consult the Auth service for token validation
- Fail-safe behavior: if Redis is unavailable, the system assumes the token is not blacklisted

### Configuration

Example properties for the Auth service:

```yaml
spring.data.redis:
  host: localhost
  port: 6379
```

Other services should enable validation via the Auth service:

```yaml
jwt.validation.use-auth-service: true
```

### Endpoints

```
POST /api/v1/auth/login      # Login
POST /api/v1/auth/logout     # Logout (add token to blacklist)
POST /api/v1/auth/refresh    # Refresh token
GET  /api/v1/auth/validate   # Validate token (checks blacklist)
```

Testing: see `test_blacklist.bat` or `SOLUCAO_BLACKLIST_COMPLETA.md` for the complete test procedure.

---

## Implemented Patterns

- Microservices architecture
- Service discovery (Eureka)
- API Gateway pattern
- JWT authentication with token blacklist (Redis)
- Event-driven communication (RabbitMQ)
- Database per service
- Distributed tracing (Zipkin)
- Circuit breaker (Resilience4j)

---

## Docker

Build images:

```bash
mvn clean install && mvn jib:dockerBuild
```

Kubernetes (Minikube) deployment:

```bash
cd k8s/minikube
kubectl apply -f bootstrap/ && kubectl apply -f services/
```

---

## Documentation

- API collection: `insomnia/all-services.yaml`
- Swagger UI: available at `/swagger-ui.html` for services that expose it (for example: auth, fraud, notification)
- Static OpenAPI files are included in the repository:
    - `auth/swagger/openapi.json`
    - `clients/swagger/openapi.json`
    - `fraud/swagger/openapi.json`
    - `notification/swagger/openapi.json`
- Health check: `GET /actuator/health` (all services)

Notes:

- If services use Springdoc/OpenAPI, their interactive Swagger UI is typically accessible at `/swagger-ui.html` or
  `/swagger-ui/index.html`.
- If you prefer to view static OpenAPI JSON files locally, you can serve the repository with a simple static server and
  open a Swagger UI instance pointing to one of the JSON files.
