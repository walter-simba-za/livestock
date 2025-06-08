package co.za.zwibvafhi.livestock.common;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

/** Utility for creating RFC 7807 Problem Details. */
public final class ProblemDetailUtils {

  private static final String BASE_URI = "urn:livestock:error:";

  private ProblemDetailUtils() {
    // Prevent instantiation
  }

  public static ProblemDetail createProblemDetail(
      HttpStatus status, String errorCode, String detail) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
    problemDetail.setType(URI.create(BASE_URI + errorCode));
    problemDetail.setTitle(errorCode);
    return problemDetail;
  }
}
