package co.za.zwibvafhi.livestock.e2e.helper;

import co.za.zwibvafhi.livestock.e2e.TestCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Consumer;

public class TestCaseRunner {

  private final TestCaseExecutor executor;
  private final TestCaseVariableReplacer replacer;
  private final Consumer<TestCase> setupAction;

  public TestCaseRunner(ObjectMapper objectMapper, Consumer<TestCase> setupAction) {
    this.executor = new TestCaseExecutor(objectMapper);
    this.replacer = new TestCaseVariableReplacer();
    this.setupAction = setupAction;
  }

  public void run(TestCase testCase) throws Exception {
    if (setupAction != null && !testCase.getTestName().contains("invalid-user")) {
      setupAction.accept(testCase);
    }
    String endpoint = replacer.replaceVariables(testCase);
    executor.execute(testCase, endpoint);
  }
}