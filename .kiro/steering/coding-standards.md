---
inclusion: always
---

# Coding Standards — microTracking

## Java / Spring Boot
- Spring Boot 3.x conventions
- Layered: `@RestController` → `@Service` → `@Repository`
- Constructor injection only
- DTOs for API boundaries
- Configuration in `application.yml`

## Build & Run
- `./mvnw clean package`
- `./mvnw spring-boot:run`
- Depends on eurekaServer for registration
