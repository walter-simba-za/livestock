package co.za.zwibvafhi.livestock.e2e;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {
  private String testName;
  private String endpoint;
  private String method;
  private Object requestBody;
  private Map<String, String> queryParams;
  private Map<String, Object> validationRules;
  private boolean enabled;
}