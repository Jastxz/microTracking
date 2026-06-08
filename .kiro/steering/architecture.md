---
inclusion: always
---

# Architecture — microTracking

## Stack
- **Framework:** Spring Boot + Spring Cloud (Eureka client)
- **Language:** Java
- **Build:** Maven
- **Domain:** Tracking/monitoring microservice

## Module Boundaries (from graphify: 56 nodes, 79 edges, 10 communities)
- God nodes: `TrackingService` (7 edges), `TrackingController` (5), `EmailService` (4)
- REST API for tracking operations with email notification capability
- `TrackingController` — HTTP endpoints for tracking data
- `TrackingService` — Core tracking business logic
- `EmailService` — Sends notifications/reports via email
- Registered with Eureka service discovery

## Dependency Rules
- Controller → Service layer (TrackingService, EmailService)
- `EmailService` is a cross-cutting service — used by TrackingService for notifications
- No direct coupling to other microservices' internals
- External email sending: via EmailService abstraction only

## Ecosystem
Part of cluster: eurekaServer, microGateway, microNeural, microPrimeNumbers, micro-adversarial-search
