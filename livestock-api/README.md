# Livestock API Module

The `livestock-api` module defines a framework-agnostic API contract for the Livestock Management
System using OpenAPI annotations. It includes DTOs, enums, and interfaces shared across modules,
ensuring consistency in API interactions.

## Purpose

- Provides `LivestockApi`, a pure Java interface with OpenAPI annotations defining REST endpoints
  for livestock counts, events, expenses, and profit reports.
- Defines data structures (DTOs and enums) for requests and responses, including expense tracking
  for various farm-related costs and financial reporting.
- Generates OpenAPI spec (`/v3/api-docs.yaml`) when used in `livestock-app` for Swagger UI and
  client generation.

## Usage

- Consumed by `livestock-api-client` for Feign client implementation.
- Implemented by `livestock-app` using Spring Web annotations.
- Supports external implementations (e.g., Quarkus, Micronaut) via OpenAPI contract.

## Benefits

- Framework-agnostic design ensures portability across implementations.
- OpenAPI spec facilitates client/server code generation and interactive documentation.
- Shared DTOs ensure consistency across modules.

## Future Enhancements

- Add audit logging for expense operations to track changes.
- Support filtering expenses by additional criteria (e.g., description keywords).
- Implement API versioning to support backward compatibility.
- Add support for user-defined custom expense categories.

For setup and deployment, see [parent README](../../README.md).
