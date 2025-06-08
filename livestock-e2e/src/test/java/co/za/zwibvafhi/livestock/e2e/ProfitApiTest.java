package co.za.zwibvafhi.livestock.e2e;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import co.za.zwibvafhi.livestock.api.dto.InitializeCountRequest;
import co.za.zwibvafhi.livestock.api.dto.RecordEventRequest;
import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockEventType;
import co.za.zwibvafhi.livestock.e2e.helper.DynamicTestFactory;
import co.za.zwibvafhi.livestock.e2e.helper.TestCaseFactory;
import co.za.zwibvafhi.livestock.e2e.helper.TestCaseRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
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

@SpringBootTest(classes = LivestockE2eApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Livestock Profit API Tests")
class ProfitApiTest {

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
  @DisplayName("Get profit summary with sale event")
  void shouldGetProfitWithSaleEvent() throws Exception {
    setupData();
    given()
        .queryParams("category", "CATTLE")
        .when()
        .get("/api/v1/livestock/1/profit")
        .then()
        .statusCode(200)
        .body("totalProfit", equalTo(3000.0f));
  }

  @Test
  @DisplayName("Get profit summary with no events")
  void shouldGetZeroProfitWithNoEvents() throws Exception {
    InitializeCountRequest countRequest = InitializeCountRequest.builder()
        .category(LivestockCategory.CATTLE)
        .maleCount(10)
        .femaleCount(5)
        .build();
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(countRequest))
        .when()
        .post("/api/v1/livestock/1/counts")
        .then()
        .statusCode(200);

    given()
        .queryParams("category", "CATTLE")
        .when()
        .get("/api/v1/livestock/1/profit")
        .then()
        .statusCode(200)
        .body("totalProfit", equalTo(0.0f));
  }

  @TestFactory
  @DisplayName("Dynamic profit tests from JSON")
  Collection<DynamicTest> dynamicProfitTests() throws Exception {
    TestCaseFactory testCaseFactory = new TestCaseFactory(objectMapper);
    TestCaseRunner testCaseRunner = new TestCaseRunner(objectMapper, testCase -> {
      try {
        setupData();
      } catch (Exception e) {
        throw new RuntimeException("Failed to setup data for test: " + testCase.getTestName(), e);
      }
    });
    DynamicTestFactory dynamicTestFactory = new DynamicTestFactory(testCaseRunner);
    String testDir = Path.of("src/test/resources", TestConstants.TESTS_BASE_DIR, TestConstants.PROFIT_DIR).toString();
    return testCaseFactory.loadTestCases(testDir).stream()
        .map(dynamicTestFactory::createDynamicTest)
        .toList();
  }

  private void setupData() throws Exception {
    InitializeCountRequest countRequest = InitializeCountRequest.builder()
        .category(LivestockCategory.CATTLE)
        .maleCount(10)
        .femaleCount(15)
        .build();
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(countRequest))
        .when()
        .post("/api/v1/livestock/1/counts")
        .then()
        .statusCode(200);

    RecordEventRequest eventRequest = RecordEventRequest.builder()
        .category(LivestockCategory.CATTLE)
        .eventType(LivestockEventType.SALE)
        .maleCount(2)
        .femaleCount(3)
        .salePrice(new BigDecimal("1000.00"))
        .cost(new BigDecimal("200.00"))
        .livestockIds(List.of("CATTLE_001", "CATTLE_002"))
        .build();
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(eventRequest))
        .when()
        .post("/api/v1/livestock/1/events")
        .then()
        .statusCode(200);
  }
}