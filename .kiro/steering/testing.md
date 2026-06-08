---
inclusion: fileMatch
fileMatchPattern: "**/*Test.java,**/*Tests.java"
---

# Testing — microTracking

## Framework
- JUnit 5 + Spring Boot Test

## Key Test Classes
- `EmailServiceLocalTest` — Unit tests for email functionality

## Run
- `./mvnw test`

## Conventions
- Test classes in `src/test/java/`
- Unit tests for services (`*Test.java`)
- Mock external dependencies (email server) in tests
