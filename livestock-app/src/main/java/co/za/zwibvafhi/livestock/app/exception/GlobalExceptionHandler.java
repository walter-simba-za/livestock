package co.za.zwibvafhi.livestock.app.exception;

import co.za.zwibvafhi.livestock.common.LivestockErrorCodes;
import co.za.zwibvafhi.livestock.common.LivestockException;
import co.za.zwibvafhi.livestock.common.ProblemDetailUtils;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Global exception handler for REST controllers. */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(LivestockException.class)
  public ResponseEntity<ProblemDetail> handleLivestockException(LivestockException ex) {
    HttpStatus status = resolveStatus(ex.getErrorCode());
    ProblemDetail problemDetail =
        ProblemDetailUtils.createProblemDetail(status, ex.getErrorCode(), ex.getMessage());
    return new ResponseEntity<>(problemDetail, status);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ProblemDetail> handleConstraintViolationException(
      ConstraintViolationException ex) {
    String detail =
        ex.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.joining("; "));
    ProblemDetail problemDetail =
        ProblemDetailUtils.createProblemDetail(
            HttpStatus.BAD_REQUEST, LivestockErrorCodes.INVALID_REQUEST, detail);
    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleGenericException(Exception ex) {
    ProblemDetail problemDetail =
        ProblemDetailUtils.createProblemDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, "UNEXPECTED_ERROR", ex.getMessage());
    return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private HttpStatus resolveStatus(String errorCode) {
    return switch (errorCode) {
      case LivestockErrorCodes.COUNT_EXISTS -> HttpStatus.CONFLICT;
      case LivestockErrorCodes.COUNT_NOT_FOUND,
          LivestockErrorCodes.USER_NOT_FOUND,
          LivestockErrorCodes.EVENT_NOT_FOUND ->
          HttpStatus.NOT_FOUND;
      case LivestockErrorCodes.INVALID_REQUEST, LivestockErrorCodes.INVALID_EVENT_TYPE ->
          HttpStatus.BAD_REQUEST;
      default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
  }
}
