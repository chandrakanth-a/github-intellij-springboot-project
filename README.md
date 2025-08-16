# Public API Gateway App – Backend (Spring Boot)

This backend provides JWT-secured endpoints to register/login, save public JSON API URLs, fetch and store their responses per user, and list results (dashboard). Swagger UI is enabled.

## Quick Start

1. Set up **PostgreSQL** and ensure a database named `customer` exists and is accessible.
2. Update `src/main/resources/application.properties` if needed (defaults below match your request).
3. Build & run:

```bash
mvn spring-boot:run
```

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI docs: `http://localhost:8080/v3/api-docs`

### Default Credentials / Secrets
- JWT secret is set in `application.properties` (`app.jwt.secret`). Change it for production.

## Endpoints (JWT secured except /api/auth/**)

- `POST /api/auth/register` – Register with email + password.
- `POST /api/auth/login` – Login to get JWT.
- `POST /api/urls` – Add a public JSON URL for the current user.
- `GET /api/urls` – List your saved URLs.
- `POST /api/urls/{id}/fetch` – Fetch the URL now and store result.
- `GET /api/dashboard/results` – List your stored results (most recent first).
- `GET /api/results/{id}` – Get a specific result you own.

Include the token in requests:
```
Authorization: Bearer <jwt>
```

## Database Schema

SQL is provided in `src/main/resources/db/schema.sql` if you prefer manual creation. JPA `ddl-auto=update` is enabled by default to auto-sync.

## Factory Pattern

Different external API strategies are resolved by a factory:
- `JsonPlaceholderStrategy` – for `jsonplaceholder.typicode.com`
- `GenericJsonStrategy` – fallback for other JSON APIs

These implement the `ExternalApiStrategy` interface showing inheritance/polymorphism.

## Build/Run with Maven

```
mvn clean package
java -jar target/apigw-backend-0.0.1-SNAPSHOT.jar
```
