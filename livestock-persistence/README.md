# Livestock Persistence Module

The `livestock-persistence` module handles data persistence for the Livestock Management System
using Spring Data JPA and Flyway.

## Purpose

- Defines JPA entities and repositories.
- Manages database schema migrations.

## Dependencies

- `livestock-api` (for enums)
- Spring Data JPA
- Flyway

## Usage

- Used by `livestock-core` for data access.
- Flyway applies migrations in `livestock-app`.

## Benefits

- Isolates persistence logic for flexibility (e.g., switch to Quarkus Panache).
- Flyway ensures consistent schema evolution.
- JPA validates data consistency.

For setup and deployment, see [parent README](../../README.md).
