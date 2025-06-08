# Livestock Core Module

The `livestock-core` module contains the business logic, mappings, and configurations for the
Livestock Management System.

## Purpose

- Implements service layer for livestock operations.
- Provides MapStruct mappings between DTOs and entities.
- Configures caching and mappers.

## Dependencies

- `livestock-api`
- `livestock-common`
- MapStruct
- Caffeine
- Lombok

## Usage

- Used by `livestock-app` to handle API requests.
- Reusable in alternative implementations (e.g., Quarkus).

## Benefits

- Separates business logic from persistence/controller.
- Caching improves performance.
- MapStruct ensures efficient DTO-entity mapping.

For setup and deployment, see [parent README](../../README.md).
