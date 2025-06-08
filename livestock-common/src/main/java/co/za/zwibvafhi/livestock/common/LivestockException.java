package co.za.zwibvafhi.livestock.common;

import lombok.Getter;

/** Custom exception for livestock API errors. */
@Getter
public class LivestockException extends RuntimeException {

  private final String errorCode;

  public LivestockException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public LivestockException(String errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }
}
