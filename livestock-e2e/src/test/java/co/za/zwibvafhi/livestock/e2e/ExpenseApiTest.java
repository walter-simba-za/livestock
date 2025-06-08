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
@DisplayName("Livestock Expense API Tests")
class ExpenseApiTest {

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
  @DisplayName("Record medication expense")
  void shouldRecordMedicationExpense() throws Exception {
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
        .statusCode(200)
        .body("expenseCategory", equalTo("MEDICATION"))
        .body("amount", equalTo(200.0f));
  }

  @Test
  @DisplayName("Record feed expense")
  void shouldRecordFeedExpense() throws Exception {
    RecordExpenseRequest request = RecordExpenseRequest.builder()
        .category(LivestockCategory.CATTLE)
        .expenseCategory(ExpenseCategory.FEED)
        .amount(new BigDecimal("150.00"))
        .description("Grain feed")
        .expenseDate(LocalDate.now())
        .build();
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(request))
        .when()
        .post("/api/v1/livestock/1/expenses")
        .then()
        .statusCode(200)
        .body("expenseCategory", equalTo("FEED"))
        .body("amount", equalTo(150.0f));
  }

  @Test
  @DisplayName("Fail to record expense with negative amount")
  void shouldFailWithNegativeAmount() throws Exception {
    RecordExpenseRequest request = RecordExpenseRequest.builder()
        .category(LivestockCategory.CATTLE)
        .expenseCategory(ExpenseCategory.MEDICATION)
        .amount(new BigDecimal("-100.00"))
        .description("Negative amount")
        .expenseDate(LocalDate.now())
        .build();
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(request))
        .when()
        .post("/api/v1/livestock/1/expenses")
        .then()
        .statusCode(400);
  }

  @TestFactory
  @DisplayName("Dynamic expense tests from JSON")
  Collection<DynamicTest> dynamicExpenseTests() throws Exception {
    TestCaseFactory testCaseFactory = new TestCaseFactory(objectMapper);
    TestCaseRunner testCaseRunner = new TestCaseRunner(objectMapper, null);
    DynamicTestFactory dynamicTestFactory = new DynamicTestFactory(testCaseRunner);
    String testDir = Path.of("src/test/resources", TestConstants.TESTS_BASE_DIR, TestConstants.EXPENSES_DIR).toString();
    return testCaseFactory.loadTestCases(testDir).stream()
        .map(dynamicTestFactory::createDynamicTest)
        .toList();
  }
}