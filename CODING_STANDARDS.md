# Livestock Management System Coding Standards

## 1. Java Code Style

- **Guideline**: Follow
  the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html), enforced by
  Checkstyle (`checkstyle.xml`).
- **Formatting**:
    - Use 2-space indentation, no tabs (enforced by `.editorconfig` and `formatter.xml`).
    - Maximum line length is 100 characters.
    - Use Unix line endings (`\n`).
- **Naming Conventions**:
    - Classes/interfaces: PascalCase (e.g., `LivestockServiceImpl`).
    - Methods: camelCase (e.g., `recordEvent`).
    - Constants: UPPER_SNAKE_CASE (e.g., `LivestockConstants.MSG_USER_NOT_FOUND`).
    - Variables/parameters: camelCase (e.g., `userId`).
- **Imports**:
    - Avoid wildcard imports (e.g., `import java.util.*`).
    - Remove unused imports.
- **Javadoc**:
    - Document public and protected methods with Javadoc (e.g., in `LivestockService`).
    - Include `@param`, `@return`, and `@throws` where applicable.

## 2. Spring Boot Best Practices

- **Dependency Injection**:
    - Use constructor injection (e.g., `LivestockServiceImpl` uses `@RequiredArgsConstructor`).
    - Avoid `@Autowired` on fields; prefer constructor or setter injection.
- **Transactions**:
    - Use `@Transactional` on service methods accessing repositories (e.g., `recordEvent`
      in `LivestockServiceImpl`).
    - Specify `readOnly = true` for read-only operations.
- **Controllers**:
    - Use `@RestController` for REST APIs in `livestock-app`.
    - Return appropriate HTTP status codes (e.g., 404 for `LivestockException`).
    - Validate inputs with `@Valid` and `jakarta.validation` constraints.
- **Repositories**:
    - Use Spring Data JPA naming conventions for query methods (e.g., `findByUserUserIdAndCategory`
      in `LivestockEventRepository`).
    - Prefer derived queries over `@Query` unless necessary.
- **Logging**:
    - Use SLF4J with Logback for logging.
    - Log errors with context (e.g., `log.error("User not found: {}", userId)`).
    - Avoid `System.out.println`.

## 3. Error Handling

- Throw custom exceptions (e.g., `LivestockException`) for business logic errors.
- Include error codes and messages (e.g., `LivestockErrorCodes.USER_NOT_FOUND`).
- Handle exceptions globally in `livestock-app` with `@ControllerAdvice`.
- Map exceptions to appropriate HTTP status codes.

## 4. Testing

- Write unit tests with JUnit 5 for service and repository layers (
  e.g., `LivestockServiceImplTest`).
- Use Mockito for mocking dependencies.
- Aim for 75% branch coverage (enforced by JaCoCo in `livestock/pom.xml`).
- Use `@SpringBootTest` sparingly for integration tests; prefer `@DataJpaTest` or `@WebMvcTest`.
- Test edge cases and error conditions (e.g., `LivestockException` scenarios).

## 5. Code Organization

- Follow package structure: `co.za.zwibvafhi.livestock.<module>.<layer>` (
  e.g., `co.za.zwibvafhi.livestock.persistence.service`).
- Keep classes focused (e.g., `LivestockServiceImpl` handles business logic, not persistence).
- Use `@Service`, `@Repository`, and `@RestController` annotations appropriately.
- Avoid large methods; refactor if exceeding 150 lines (enforced by Checkstyle).

## 6. Commit Messages

- Follow [Conventional Commits](https://www.conventionalcommits.org/): `type(scope): description`.
    - Example: `fix(livestock-core): resolve repository method issue`.
    - Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`.
- Reference issue IDs if applicable (e.g., `fix(livestock-app): resolve #123`).

## 7. Tooling

- **Checkstyle**: Enforces Google Java Style Guide (`mvn validate`).
- **SpotBugs**: Detects bugs (`mvn verify`).
- **PMD**: Analyzes best practices and performance (`mvn verify`).
- **Formatter**: Enforces formatting (`mvn formatter:validate` or `mvn formatter:format`).
- **IDE Setup**:
    - Install Checkstyle, SpotBugs, and PMD plugins for IntelliJ/Eclipse.
    - Use `.editorconfig` for consistent formatting.
- **CI/CD**:
    - Run `mvn verify` in Bamboo to enforce standards.
    - Fail builds on violations.

## 8. Security

- Use OWASP Dependency-Check to scan for vulnerabilities (`mvn dependency-check:check`).
- Sanitize inputs in REST APIs to prevent injection attacks.
- Avoid hardcoding sensitive data (e.g., use `application.yml` for secrets).

## 9. Performance

- Use Spring Data JPA efficiently (e.g., avoid N+1 queries in `LivestockEventRepository`).
- Cache frequently accessed data with Caffeine (e.g., in `LivestockServiceImpl`).
- Profile long-running methods with tools like VisualVM.
