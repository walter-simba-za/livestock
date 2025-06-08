package co.za.zwibvafhi.livestock.client;

import co.za.zwibvafhi.livestock.common.LivestockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.io.InputStream;
import lombok.Getter;
import lombok.Setter;

/** Error decoder for Feign client to handle API errors. */
public class LivestockFeignErrorDecoder implements ErrorDecoder {

  private final ErrorDecoder defaultDecoder = new Default();

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Exception decode(String methodKey, Response response) {
    try (InputStream body = response.body().asInputStream()) {
      if (body != null) {
        ProblemDetailResponse problemDetail =
            objectMapper.readValue(body, ProblemDetailResponse.class);
        String errorCode =
            problemDetail.getTitle() != null ? problemDetail.getTitle() : "UNKNOWN_ERROR";
        String message =
            problemDetail.getDetail() != null ? problemDetail.getDetail() : response.reason();
        return new LivestockException(errorCode, message);
      }
    } catch (IOException e) {
      // Fallback to default decoder
    }
    return defaultDecoder.decode(methodKey, response);
  }

  /** DTO for parsing Problem Detail JSON. */
  @Getter
  @Setter
  private static class ProblemDetailResponse {
    private String title;

    private String detail;
  }
}
