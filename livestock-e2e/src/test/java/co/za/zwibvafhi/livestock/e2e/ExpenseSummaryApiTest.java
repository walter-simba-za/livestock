package co.za.zwibvafhi.livestock.e2e;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import co.za.zwibvafhi.livestock.api.dto.RecordExpenseRequest;
import co.za.zwibvafhi.livestock.api.model.ExpenseCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.app.LivestockApplication;
import co.za.zwibvafhi.livestock.e2e.helper.DynamicTestFactory;
import co.za.zwibvafhi.livestock.e2e.helper.TestCaseFactory;
import co.za.zwibvafhi.livestock.e2e.helper.TestCaseRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(classes = LivestockApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Livestock Expense Summary API Tests")
class ExpenseSummaryApiTest {

  @LocalServerPort
  private int port;

  @Autowired
  private Flyway flyway;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeAll
  static void configureRestAssured() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.filters(new LoggingFilter());
  }

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
    flyway.clean();
    flyway.migrate();
  }

  @Test
  @DisplayName("Get expense summaries for valid category")
  void shouldGetExpenseSummaries() throws Exception {
    setupData();
    given()
        .queryParams("category", "CATTLE")
        .when()
        .get("/api/v1/livestock/1/expense-summaries")
        .then()
        .statusCode(200)
        .body("[0].expenseCategory", equalTo("MEDICATION"))
        .body("[0].totalAmount", equalTo(200.0f));
  }

  @Test
  @DisplayName("Get expense summaries with invalid user ID")
  void shouldFailWithInvalidUserId() {
    given()
        .queryParams("category", "CATTLE")
        .when()
        .get("/api/v1/livestock/999/expense-summaries")
        .then()
        .statusCode(404);
  }

  @TestFactory
  @DisplayName("Dynamic expense summary tests from JSON")
  Collection<DynamicTest> dynamicExpenseSummaryTests() throws Exception {
    TestCaseFactory testCaseFactory = new TestCaseFactory(objectMapper);
    TestCaseRunner testCaseRunner = new TestCaseRunner(objectMapper, testCase -> {
      if (!testCase.getTestName().contains("missing-category")) {
        try {
          setupData();
        } catch (Exception e) {
          throw new RuntimeException("Failed to setup data for test: " + testCase.getTestName(), e);
        }
      }
    });
    DynamicTestFactory dynamicTestFactory = new DynamicTestFactory(testCaseRunner);
    String testDir = Path.of("src/test/resources", TestConstants.TESTS_BASE_DIR, TestConstants.EXPENSE_SUMMARIES_DIR).toString();
    return testCaseFactory.loadTestCases(testDir).stream()
        .map(dynamicTestFactory::createDynamicTest)
        .toList();
  }

  private void setupData() throws Exception {
    RecordExpenseRequest request = RecordExpenseRequest.builder()
        .category(LivestockCategory.CATTLE)
        .expenseCategory(ExpenseCategory.MEDICATION)
        .amount(new BigDecimal("200.00"))
        .description("Antibiotics")
        .expenseDate(LocalDate.now())
        .build();
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(request))
        .when()
        .post("/api/v1/livestock/1/expenses")
        .then()
        .statusCode(200);
  }
}