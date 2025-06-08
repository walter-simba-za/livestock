package co.za.zwibvafhi.livestock.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
  private final ObjectMapper mapper;

  public LoggingFilter() {
    mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  @Override
  public Response filter(
      FilterableRequestSpecification requestSpec,
      FilterableResponseSpecification responseSpec,
      FilterContext ctx) {
    logRequest(requestSpec);
    Response response = ctx.next(requestSpec, responseSpec);
    logResponse(response);
    return response;
  }

  private void logRequest(FilterableRequestSpecification requestSpec) {
    String uri = requestSpec.getURI();
    Map<String, String> queryParams = requestSpec.getQueryParams();
    Object body = requestSpec.getBody();

    logger.debug("Request URI: {}", uri);
    logger.debug("Query Params: {}", queryParams);
    logger.debug("Request Body (Raw): {}", body);
    try {
      if (body != null) {
        logger.debug("Request Body (Pretty): {}", mapper.writeValueAsString(body));
      }
    } catch (Exception e) {
      logger.warn("Failed to pretty-print request body", e);
    }
  }

  private void logResponse(Response response) {
    String rawBody = response.getBody().asString();
    logger.debug("Response Status: {}", response.getStatusCode());
    logger.debug("Response Body (Raw): {}", rawBody);
    try {
      if (!rawBody.isEmpty()) {
        Object parsedBody = mapper.readValue(rawBody, Object.class);
        logger.debug("Response Body (Pretty): {}", mapper.writeValueAsString(parsedBody));
      }
    } catch (Exception e) {
      logger.warn("Failed to pretty-print response body", e);
    }
  }
}