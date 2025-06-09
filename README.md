# Livestock Management System

The Livestock Management System is a modular, Spring Boot-based REST API for managing livestock
counts and events (e.g., births, deaths). Designed for scalability and flexibility, it supports
integration with external clients like `@HerdMateBot` and enables future framework migrations (e.g.,
Quarkus, Micronaut). The project is deployed on Fly.io with auto-scaling and a low memory
footprint (~400MB).

## Features

The Livestock Management System provides a robust API for managing livestock-related data. Below are
the key features, grouped by functionality, supporting livestock categories: `GOAT`, `SHEEP`, and
`CATTLE`.

### Livestock Counts
- **Initialize Counts**: Set initial livestock counts for a user and category with male and female numbers.
- **Retrieve Counts**: Fetch the current livestock count for a user and specified category.

### Events
- **Record Events**: Log events like `BIRTH`, `DEATH`, `LOST`, `SLAUGHTER`, `SALE`, or `PURCHASE`, including male/female counts, optional costs (for `PURCHASE`, `BIRTH`), sale prices (required for `SALE`), and livestock IDs (for `SLAUGHTER`, `SALE`).
- **Retrieve Event History**: Get a history of events for a user and category, optionally filtered by event type.

### Expenses
- **Record Expenses**: Log expenses (e.g., `PURCHASE`, `FEED`, `VACCINATION`, `MEDICATION`, `LABOUR`) with amount, description, and date.
- **Retrieve Expenses**: Fetch paginated expenses for a user and category, optionally filtered by expense category (e.g., `MEDICATION`) and date range, with pagination support (default 20 per page, max 100).

### Expense Summaries
- **Retrieve Summaries**: Get total amount and count of expenses by category (e.g., `FEED`, `LABOUR`) for a user and livestock category, optionally filtered by date range.

### Profit Reports
- **Retrieve Reports**: Generate a profit report for a user and category, showing total revenue, expenses, and net profit, optionally filtered by date range.

## Project Structure

The project is a multi-module Maven project with six modules, each serving a distinct purpose to
ensure modularity, maintainability, and selective dependency inclusion. Below is the structure:

- **livestock-api**: Framework-agnostic API contract and DTOs. [Details](./livestock-api/README.md)
- **livestock-common**: Shared utilities and error handling. [Details](./livestock-common/README.md)
- **livestock-api-client**: Feign client for API
  integration. [Details](./livestock-api-client/README.md)
- **livestock-core**: Business logic and mappings. [Details](./livestock-core/README.md)
- **livestock-persistence**: JPA entities and
  repositories. [Details](./livestock-persistence/README.md)
- **livestock-app**: Spring Boot application and REST
  controller. [Details](./livestock-app/README.md)

## Design Benefits

- **Modularity**: Each module has a single responsibility, allowing selective dependency inclusion (
  e.g., `@HerdMateBot` uses only `livestock-api-client`).
- **Framework-Agnostic API**: `livestock-api` uses OpenAPI annotations, enabling implementations in
  Spring, Quarkus, or Micronaut with minimal changes.
- **Scalability**: Fly.io deployment with auto-scaling (scale-to-zero) and 512MB VMs handles high
  traffic while minimizing costs (~$10/month for prod Postgres).
- **Maintainability**: Clear separation of concerns, Javadoc, and consistent error handling (Problem
  Details, RFC 7807) ease development.
- **Cost Efficiency**: Scale-to-zero and on-demand dev Postgres reduce Fly.io costs.
- **Migration Readiness**: Framework-light
  modules (`livestock-api`, `livestock-common`, `livestock-core`) simplify future migrations.

## Developer Setup

### Prerequisites

- **Java**: 17 (e.g., OpenJDK)
- **Maven**: 3.8.6+
- **Docker**: For building deployment images
- **Flyctl**: For Fly.io deployment (`curl -L https://fly.io/install.sh | sh`)
- **GitHub Account**: For workflow triggers
- **Fly.io Account**: For deployment (`flyctl auth signup`)

### Environment Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd livestock
   ```
2. Install dependencies:
   ```bash
   mvn clean install -s settings.xml
   ```
3. Configure Fly.io:
    - Log in: `flyctl auth login`
    - Create deploy token: `flyctl tokens create deploy -x 999999h`
    - Add token to GitHub Secrets (`FLY_API_TOKEN`) in repo settings.

### Dependency Graph Generation

To visualize the dependency relationships between project modules, you can generate a dependency
graph using the `depgraph-maven-plugin`. This is useful for understanding module interactions and
ensuring modularity.

1. **Run the Command**:
   Generate a text-based dependency graph for all `co.za.zwibvafhi.livestock` modules:
   ```bash
   mvn com.github.ferstl:depgraph-maven-plugin:4.0.3:aggregate -DgraphFormat=text -Dincludes="co.za.zwibvafhi.livestock:*" -s settings.xml
   ```
    - **Output**: The graph is saved to `target/dependency-graph.txt`.
    - **Options**:
        - Use `-DgraphFormat=dot` to generate a Graphviz-compatible `.dot` file (visualize with Graphviz tools).
        - Use `-DgraphFormat=json` for a JSON representation.
        - Omit `-Dincludes` to include external dependencies (e.g., Spring Boot).

2. **Optional: Add Plugin to POM**:
   To simplify the command, add the plugin to `livestock/pom.xml`:
   ```xml
   <build>
     <plugins>
       <plugin>
         <groupId>com.github.ferstl</groupId>
         <artifactId>depgraph-maven-plugin</artifactId>
         <version>4.0.3</version>
       </plugin>
     </plugins>
   </build>
   ```
   Then run:
   ```bash
   mvn depgraph:aggregate -DgraphFormat=text -Dincludes="co.za.zwibvafhi.livestock:*" -s settings.xml
   ```

3. **Verify the Graph**:
    - Text: Open `cat target/dependency-graph.txt` in a text editor.
    - DOT: Use Graphviz (`dot -Tpng target/dependency-graph.dot -o graph.png`) to render as an image.
    - JSON: Parse `target/dependency-graph.json` with a JSON viewer.

### Code Quality and IDE Setup

To ensure consistent coding standards (Google Java Style Guide) and high-quality code, the project
uses Checkstyle, SpotBugs, PMD, and a Maven Formatter plugin. Follow these steps to enforce
standards and set up IntelliJ IDEA.

#### Enforcing Coding Standards

- **Validate Code Style**:
  Run Checkstyle and Formatter to enforce Google Java Style Guide and formatting rules:
  ```bash
  mvn validate -s settings.xml
  ```
- **Auto-Format Code**:
  Fix formatting issues automatically:
  ```bash
  mvn formatter:format -s settings.xml
  ```
- **Check for Bugs and Best Practices**:
  Run SpotBugs and PMD to detect bugs and ensure best practices:
  ```bash
  mvn verify -s settings.xml
  ```
- **Full Quality Check**:
  Combine all checks before committing:
  ```bash
  mvn clean verify -s settings.xml
  ```
- **Reports**:
    - Checkstyle: `target/checkstyle-result.xml`
    - SpotBugs: `target/spotbugsXml.xml`
    - PMD: `target/pmd.xml`

#### IntelliJ IDEA Setup

1. Open the project in IntelliJ IDEA: `File > Open > /path/to/livestock`.
2. Install recommended plugins:
    - **Checkstyle-IDEA**:
        - Install: `File > Settings > Plugins > Marketplace > Search "Checkstyle-IDEA" > Install`.
        - Configure: `Settings > Tools > Checkstyle > Configuration File > Import checkstyle.xml` (
          select `/path/to/livestock/checkstyle.xml`).
        - Enable real-time
          scanning: `Settings > Tools > Checkstyle > Checkstyle version: 10.17.0, Scan scope: All sources`.
    - **SpotBugs**:
        - Install: `File > Settings > Plugins > Marketplace > Search "SpotBugs" > Install`.
        - Configure: `Settings > Tools > SpotBugs > Enable analysis`.
    - **PMDPlugin**:
        - Install: `File > Settings > Plugins > Marketplace > Search "PMDPlugin" > Install`.
        - Configure: `Settings > Other Settings > PMD > Add ruleset` (use default Java rules or
          import from `mvn pmd:pmd` output).
    - **EditorConfig**:
        - Install: `File > Settings > Plugins > Marketplace > Search "EditorConfig" > Install`.
        - Enable: `Settings > Editor > Code Style > Enable EditorConfig support` (
          uses `.editorconfig` automatically).
3. Auto-format on save:
    - Go to `Settings > Tools > Actions on Save`.
    - Enable `Reformat code` and `Optimize imports`.
4. Align with project standards:
    - Review `CODING_STANDARDS.md` for best practices (e.g., constructor
      injection, `@Transactional`).
    - Run `mvn formatter:format` to align existing code before editing.

### Local Development

- Run with H2 in-memory database:
  ```bash
  mvn spring-boot:run -pl livestock-app -Dspring-boot.run.profiles=local
  ```
- Access:
    - API: `http://localhost:8080/api/v1/livestock`
    - Swagger UI: `http://localhost:8080/swagger-ui.html`
    - OpenAPI spec: `http://localhost:8080/v3/api-docs.yaml`

### Deployment

- **Dev Environment**:
    - Push code to GitHub.
    - Trigger `Deploy to Fly.io Dev` workflow manually (`.github/workflows/ci.yml`).
    - Access: `https://livestock-test.fly.dev/api/v1/livestock`
    - Teardown: Trigger `Teardown Fly.io Dev` workflow to destroy app and Postgres.
- **Prod Environment**:
    - Trigger `Deploy to Fly.io Prod` workflow manually.
    - Access: `https://livestock-prod.fly.dev/api/v1/livestock`

### Client Integration

- Add dependency to `livestock-api-client`:
  ```xml
  <dependency>
      <groupId>co.za.zwibvafhi.livestock</groupId>
      <artifactId>livestock-api-client</artifactId>
      <version>1.0-SNAPSHOT</version>
  </dependency>
  ```
- Configure `LIVESTOCK_API_URL` (
  e.g., `export LIVESTOCK_API_URL=https://livestock-prod.fly.dev/api/v1/livestock`).
- Example usage:
  ```java
  @Autowired
  private LivestockClient client;
  LivestockCountResponse response = client.initializeCount(1L, InitializeCountRequest.builder()
      .category(LivestockCategory.GOAT)
      .maleCount(5)
      .femaleCount(3)
      .build());
  ```

### Notes

- **Database**: Local uses H2, dev/prod use Fly Postgres (dev is on-demand).
- **Costs**: ~$10/month for prod Postgres; dev is free if torn down.
- **Monitoring**: Prod exposes Actuator endpoints (`/actuator/health`, `/actuator/metrics`).

For detailed module information, see respective `README.md` files.
