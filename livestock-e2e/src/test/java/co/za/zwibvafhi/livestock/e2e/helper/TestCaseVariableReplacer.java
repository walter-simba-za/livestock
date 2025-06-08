package co.za.zwibvafhi.livestock.e2e.helper;

import co.za.zwibvafhi.livestock.e2e.TestCase;

public class TestCaseVariableReplacer {

  public String replaceVariables(TestCase testCase) {
    String endpoint = testCase.getEndpoint();
    String userId = testCase.getTestName().contains("invalid-user") ? "999" : "1";
    return endpoint.replace("{userId}", userId);
  }
}