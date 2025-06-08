package co.za.zwibvafhi.livestock.common;

/** Constants for the Livestock Management System. */
public final class LivestockConstants {

  // Cache names
  public static final String CACHE_COUNTS = "counts";

  public static final String CACHE_EVENTS = "events";

  public static final String CACHE_PROFITS = "profits";

  public static final String CACHE_EXPENSES = "expenses";

  public static final String CACHE_EXPENSE_SUMMARIES = "expense-summaries";

  // Error message templates
  public static final String MSG_COUNT_EXISTS = "Count already exists for user %d and category %s";

  public static final String MSG_COUNT_NOT_FOUND = "Count not found for user %d and category %s";

  public static final String MSG_USER_NOT_FOUND = "User not found: %d";

  public static final String MSG_INVALID_EVENT_TYPE = "Invalid event type: %s";

  public static final String MSG_INVALID_SALE_PRICE =
      "Sale price must be positive for SALE event: %s";

  public static final String MSG_NEGATIVE_COUNT =
      "Event would result in negative count for user %d and category %s";

  public static final String MSG_INVALID_LIVESTOCK_IDS =
      "Invalid or inactive livestock IDs provided: %s";

  public static final String MSG_LIVESTOCK_ID_COUNT_MISMATCH =
      "Number of livestock IDs (%d) does not match total count (%d)";

  public static final String MSG_INVALID_EXPENSE_AMOUNT = "Expense amount must be positive: %s";

  public static final String MSG_INVALID_EXPENSE_CATEGORY = "Invalid expense category: %s";

  public static final String MSG_INVALID_PAGINATION =
      "Invalid pagination parameters: page=%d, size=%d";

  private LivestockConstants() {
    // Prevent instantiation
  }
}
