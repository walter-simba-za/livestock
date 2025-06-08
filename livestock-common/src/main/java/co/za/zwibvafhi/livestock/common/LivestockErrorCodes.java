package co.za.zwibvafhi.livestock.common;

/** Error codes for the Livestock Management System. */
public final class LivestockErrorCodes {

  public static final String COUNT_EXISTS = "LIVESTOCK_COUNT_EXISTS";

  public static final String COUNT_NOT_FOUND = "LIVESTOCK_COUNT_NOT_FOUND";

  public static final String INVALID_REQUEST = "INVALID_REQUEST";

  public static final String USER_NOT_FOUND = "USER_NOT_FOUND";

  public static final String EVENT_NOT_FOUND = "EVENT_NOT_FOUND";

  public static final String INVALID_EVENT_TYPE = "INVALID_EVENT_TYPE";

  public static final String INVALID_SALE_PRICE = "INVALID_SALE_PRICE";

  public static final String NEGATIVE_COUNT = "NEGATIVE_COUNT";

  public static final String INVALID_LIVESTOCK_IDS = "INVALID_LIVESTOCK_IDS";

  public static final String LIVESTOCK_ID_COUNT_MISMATCH = "LIVESTOCK_ID_COUNT_MISMATCH";

  public static final String INVALID_EXPENSE_AMOUNT = "INVALID_EXPENSE_AMOUNT";

  public static final String INVALID_EXPENSE_CATEGORY = "INVALID_EXPENSE_CATEGORY";

  public static final String INVALID_PAGINATION = "INVALID_PAGINATION";

  private LivestockErrorCodes() {
    // Prevent instantiation
  }
}
