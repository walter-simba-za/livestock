# Livestock Common Module

The `livestock-common` module provides shared utilities and error handling for the Livestock
Management System, ensuring consistency across modules.

## Purpose

- Defines error codes and custom exceptions.
- Provides utilities for RFC 7807 Problem Details responses.
- Contains error message resources.

## Dependencies

- None (minimal, framework-agnostic)

## Usage

- Used by `livestock-core`, `livestock-api-client`, and `livestock-app` for error handling.
- Static imports of `LivestockErrorCodes` simplify error code usage.

## Benefits

- Centralizes error handling for consistency.
- Framework-agnostic, reusable in other frameworks.
- Message templates support internationalization.

For setup and deployment, see [parent README](../../README.md).
