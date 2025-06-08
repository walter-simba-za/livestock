package co.za.zwibvafhi.livestock.e2e;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import co.za.zwibvafhi.livestock.api.dto.InitializeCountRequest;
import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.app.LivestockApplication;
import co.za.zwibvafhi.livestock.e2e.helper.DynamicTestFactory;
import co.za.zwibvafhi.livestock.e2e.helper.TestCaseFactory;
import co.za.zwibvafhi.livestock.e2e.helper.TestCaseRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.nio.file.Path;
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
@DisplayName("Livestock Count API Tests")
class CountApiTest {

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
  @DisplayName("Initialize livestock count successfully")
  void shouldInitializeCount() throws Exception {
    InitializeCountRequest request = InitializeCountRequest.builder()
        .category(LivestockCategory.CATTLE)
        .maleCount(10)
        .femaleCount(15)
        .build();
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(request))
        .when()
        .post("/api/v1/livestock/1/counts")
        .then()
        .statusCode(200)
        .body("maleCount", equalTo(10))
        .body("femaleCount", equalTo(15));
  }

  @Test
  @DisplayName("Get livestock count after initialization")
  void shouldGetCount() throws Exception {
    InitializeCountRequest request = InitializeCountRequest.builder()
        .category(LivestockCategory.CATTLE)
        .maleCount(5)
        .femaleCount(8)
        .build();
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(request))
        .when()
        .post("/api/v1/livestock/1/counts")
        .then()
        .statusCode(200);

    given()
        .queryParams("category", "CATTLE")
        .when()
        .get("/api/v1/livestock/1/counts")
        .then()
        .statusCode(200)
        .body("maleCount", equalTo(5))
        .body("femaleCount", equalTo(8));
  }

  @Test
  @DisplayName("Fail to initialize count with negative male count")
  void shouldFailWithNegativeMaleCount() throws Exception {
    InitializeCountRequest request = InitializeCountRequest.builder()
        .category(LivestockCategory.CATTLE)
        .maleCount(-1)
        .femaleCount(10)
        .build();
    given()
        .contentType(ContentType.JSON)
        .body(objectMapper.writeValueAsString(request))
        .when()
        .post("/api/v1/livestock/1/counts")
        .then()
        .statusCode(400);
  }

  @TestFactory
  @DisplayName("Dynamic count tests from JSON")
  Collection<DynamicTest> dynamicCountTests() throws Exception {
    TestCaseFactory testCaseFactory = new TestCaseFactory(objectMapper);
    TestCaseRunner testCaseRunner = new TestCaseRunner(objectMapper, null);
    DynamicTestFactory dynamicTestFactory = new DynamicTestFactory(testCaseRunner);
    String testDir = Path.of("src/test/resources", TestConstants.TESTS_BASE_DIR, TestConstants.COUNTS_DIR).toString();
    return testCaseFactory.loadTestCases(testDir).stream()
        .map(dynamicTestFactory::createDynamicTest)
        .toList();
  }
}