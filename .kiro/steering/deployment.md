---
inclusion: fileMatch
fileMatchPattern: ".woodpecker*,Dockerfile*,docker-compose*,application.yml"
---

# Deployment — microTracking

## CI/CD
- Woodpecker CI pipeline (`.woodpecker.yaml`)

## Local
- `./mvnw spring-boot:run`
- Requires eurekaServer on port 8761

## Startup Order
1. eurekaServer
2. microGateway
3. This service

## Email Service
- EmailService requires SMTP configuration in application.yml
- Use environment variables for SMTP credentials in production
