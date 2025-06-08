package co.za.zwibvafhi.livestock.e2e.helper;

import co.za.zwibvafhi.livestock.e2e.TestCase;
import org.junit.jupiter.api.DynamicTest;

public class DynamicTestFactory {

  private final TestCaseRunner testCaseRunner;

  public DynamicTestFactory(TestCaseRunner testCaseRunner) {
    this.testCaseRunner = testCaseRunner;
  }

  public DynamicTest createDynamicTest(TestCase testCase) {
    return DynamicTest.dynamicTest(testCase.getTestName(), () -> testCaseRunner.run(testCase));
  }
}