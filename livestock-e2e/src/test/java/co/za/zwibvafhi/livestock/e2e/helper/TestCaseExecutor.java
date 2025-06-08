package co.za.zwibvafhi.livestock.e2e.helper;

import static org.hamcrest.Matchers.equalTo;

import co.za.zwibvafhi.livestock.e2e.TestCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class TestCaseExecutor {

  private final ObjectMapper objectMapper;

  public TestCaseExecutor(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public void execute(TestCase testCase, String endpoint) throws Exception {
    RequestSpecification requestSpec = RestAssured.given().contentType(ContentType.JSON);
    if (testCase.getQueryParams() != null) {
      requestSpec.queryParams(testCase.getQueryParams());
    }
    if (testCase.getRequestBody() != null && (testCase.getMethod().equalsIgnoreCase("POST") || testCase.getMethod().equalsIgnoreCase("PUT"))) {
      requestSpec.body(objectMapper.writeValueAsString(testCase.getRequestBody()));
    }

    Response response = requestSpec.when().request(testCase.getMethod(), endpoint);

    Map<String, Object> validationRules = testCase.getValidationRules();
    if (validationRules == null) {
      throw new IllegalStateException("Validation rules missing for test: " + testCase.getTestName());
    }
    Integer expectedStatus = (Integer) validationRules.get("expectedStatus");
    if (expectedStatus == null) {
      throw new IllegalStateException("Expected status missing for test: " + testCase.getTestName());
    }
    response.then().statusCode(expectedStatus);
    @SuppressWarnings("unchecked")
    Map<String, Object> responseFields = (Map<String, Object>) validationRules.get("responseFields");
    if (responseFields != null) {
      responseFields.forEach((field, value) -> response.then().body(field, equalTo(value)));
    }
  }
}