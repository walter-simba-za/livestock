package co.za.zwibvafhi.livestock.e2e.helper;

import co.za.zwibvafhi.livestock.e2e.TestCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class TestCaseFactory {

  private final ObjectMapper objectMapper;

  public TestCaseFactory(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public List<TestCase> loadTestCases(String directory) throws IOException {
    try (Stream<Path> paths = Files.walk(Path.of(directory))) {
      return paths
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().endsWith(".json"))
          .map(path -> {
            try {
              TestCase testCase = objectMapper.readValue(path.toFile(), TestCase.class);
              return testCase.isEnabled() ? testCase : null;
            } catch (IOException e) {
              throw new RuntimeException("Failed to read test case: " + path, e);
            }
          })
          .filter(Objects::nonNull)
          .toList();
    }
  }
}