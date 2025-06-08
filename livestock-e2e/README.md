# Livestock E2E Tests

## Purpose

The `livestock-e2e` module provides end-to-end (E2E) tests for the Livestock Management System's APIs. Using RestAssured for HTTP requests, Flyway for H2 migrations, and JSON test cases in `src/test/resources/tests/`, it verifies API functionality and data integrity, targeting 80% test coverage with JaCoCo reports.

## Setup

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd livestock/livestock-e2e
   ```

2. **Configure H2 Database**:
   - Tests use an H2 in-memory database, configured via `src/test/resources/application-test.yml`:
     ```yaml
     spring:
       datasource:
         url: jdbc:h2:mem:livestock_test;DB_CLOSE_DELAY=-1
         driver-class-name: org.h2.Driver
         username: sa
     ```
   - Ensure the H2 dependency is in `pom.xml`:
     ```xml
     <dependency>
       <groupId>com.h2database</groupId>
       <artifactId>h2</artifactId>
       <scope>test</scope>
     </dependency>
     ```

3. **Install Dependencies**:
   ```bash
   mvn clean install -s ../settings.xml
   ```

4. **Verify Flyway**:
   - Flyway applies migrations during tests. Check `src/test/resources/db/migration/*.sql`.
   - Run manually:
     ```bash
     mvn flyway:migrate -pl livestock-e2e -Dspring.profiles.active=test -s ../settings.xml
     ```

## Usage

### Compile
```bash
mvn clean compile -pl livestock-e2e -s ../settings.xml
```

### Run Tests
```bash
mvn test -pl livestock-e2e -Dspring.profiles.active=test -s ../settings.xml
```
- JSON test cases are loaded from `src/test/resources/tests/`, organized by:
   - Counts: `tests/counts`
   - Events: `tests/events`
   - Expenses: `tests/expenses`
   - Expense Summaries: `tests/expense-summaries`
   - Profit: `tests/profit`
- JSON format (`TestCase`):
  ```json
  {
    "testName": "example_test",
    "endpoint": "/api/v1/livestock/{userId}/counts",
    "method": "POST",
    "requestBody": { "category": "CATTLE", "maleCount": 10, "femaleCount": 5 },
    "queryParams": { "category": "CATTLE" },
    "validationRules": {
      "expectedStatus": 200,
      "responseFields": { "maleCount": 10, "femaleCount": 5 }
    },
    "enabled": true
  }
  ```
- Helper classes in `co.za.zwibvafhi.livestock.e2e.helper`:
   - `DynamicTestFactory`: Creates JUnit 5 `DynamicTest` instances.
   - `TestCaseExecutor`: Sends HTTP requests and validates responses.
   - `TestCaseFactory`: Loads `TestCase` objects from JSON files.
   - `TestCaseRunner`: Orchestrates test execution.
   - `TestCaseVariableReplacer`: Replaces placeholders like `{userId}`.

### Generate JaCoCo Reports
```bash
mvn verify -pl livestock/livestock-e2e -s ../settings.xml
```
- Reports in `target/site/jacoco/`. View `index.html` (target: 80%).

### Check Logs
```bash
cat logs/livestock-e2e.log
```