# Livestock Application Module

The `livestock-app` module is the main application for the Livestock Management System using Spring
Boot. It implements the `LivestockApi` contract defined in `livestock-api` and integrates
with `livestock-core` for business logic and `livestock-persistence` for data storage.

## Purpose

- Implements REST endpoints for livestock management using Spring Web, supporting expense tracking
  for various farm-related costs.
- Provides Swagger UI for API interaction and documentation.
- Integrates with PostgreSQL via Spring Data JPA and Flyway for migrations.

## Dependencies

- `livestock-api`
- `livestock-core`
- `livestock-persistence`

## Usage

- Run locally: `mvn spring-boot:run -Dspring-boot.run.profiles=local`.
- Access Swagger UI: `http://localhost:8080/swagger-ui.html`.
- Test endpoints: `http://localhost:8080/api/v1/livestock`.

## Benefits

- Modular design with Spring Boot for rapid development.
- Interactive API documentation via Swagger UI.
- Robust persistence with PostgreSQL and Flyway migrations.

## Future Enhancements

- Implement audit logging for expense operations to track changes.
- Add support for exporting expense summaries to CSV or PDF.
- Enhance caching strategies for large datasets in paginated responses.
- Allow batch import of expenses via CSV uploads.

For setup and deployment, see [parent README](../../README.md).
