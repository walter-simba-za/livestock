package co.za.zwibvafhi.livestock.api.contract;

import co.za.zwibvafhi.livestock.api.dto.ExpenseResponse;
import co.za.zwibvafhi.livestock.api.dto.ExpenseSummaryResponse;
import co.za.zwibvafhi.livestock.api.dto.InitializeCountRequest;
import co.za.zwibvafhi.livestock.api.dto.LivestockCountResponse;
import co.za.zwibvafhi.livestock.api.dto.LivestockEventResponse;
import co.za.zwibvafhi.livestock.api.dto.PaginatedExpenseResponse;
import co.za.zwibvafhi.livestock.api.dto.ProfitReportResponse;
import co.za.zwibvafhi.livestock.api.dto.RecordEventRequest;
import co.za.zwibvafhi.livestock.api.dto.RecordExpenseRequest;
import co.za.zwibvafhi.livestock.api.model.ExpenseCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Framework-agnostic API contract for livestock management operations. */
@Tag(
    name = "Livestock Management",
    description = "API for managing livestock counts, events, expenses, " + "and profit reports")
public interface LivestockApi {

  /**
   * Initializes the livestock count for a user and category.
   *
   * @param userId the ID of the user
   * @param request the count initialization details
   * @return the initialized count response, or empty if unsuccessful
   */
  @Operation(
      summary = "Initialize livestock count",
      description =
          "Initializes the livestock count for a user and category. "
              + "Returns the initialized count if successful.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Count initialized successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LivestockCountResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters or body"),
        @ApiResponse(
            responseCode = "409",
            description = "Count already exists for user and category")
      })
  Optional<LivestockCountResponse> initializeCount(
      @Parameter(description = "User ID", required = true) Long userId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Count initialization details",
              required = true)
          @Valid
      InitializeCountRequest request);

  /**
   * Records a livestock event such as birth, death, slaughter, sale, or purchase.
   *
   * @param userId the ID of the user
   * @param request the event details
   * @return the recorded event response, or empty if unsuccessful
   */
  @Operation(
      summary = "Record a livestock event",
      description =
          "Records an event (e.g., birth, death, slaughter, sale, purchase) for a user "
              + "and category. For SALE events, a positive sale price is required. "
              + "For PURCHASE and BIRTH, cost is optional. SLAUGHTER and SALE require livestock IDs.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Event recorded successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LivestockEventResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters, body, sale price, or livestock IDs"),
        @ApiResponse(responseCode = "404", description = "Count not found for user and category")
      })
  Optional<LivestockEventResponse> recordEvent(
      @Parameter(description = "User ID", required = true) Long userId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description =
                  "Event details, including sale price for SALE, cost for PURCHASE/BIRTH, "
                      + "and livestock IDs for SLAUGHTER/SALE",
              required = true)
          @Valid
      RecordEventRequest request);

  /**
   * Records an expense for a livestock item.
   *
   * @param userId the ID of the user
   * @param request the expense details
   * @return the recorded expense response, or empty if unsuccessful
   */
  @Operation(
      summary = "Record a livestock expense",
      description =
          "Records an expense (e.g., medication, vaccination) for a user and category. "
              + "Returns the recorded expense details.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Expense recorded successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExpenseResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters or body"),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  Optional<ExpenseResponse> recordExpense(
      @Parameter(description = "User ID", required = true) Long userId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Expense details",
              required = true)
          @Valid
      RecordExpenseRequest request);

  /**
   * Retrieves the current livestock count for a user and category.
   *
   * @param userId the ID of the user
   * @param category the livestock category
   * @return the current count response, or empty if not found
   */
  @Operation(
      summary = "Retrieve current livestock count",
      description = "Fetches the current livestock count for a user and specified category.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Count retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LivestockCountResponse.class))),
        @ApiResponse(responseCode = "404", description = "Count not found for user and category")
      })
  Optional<LivestockCountResponse> getCurrentCount(
      @Parameter(description = "User ID", required = true) Long userId,
      @Parameter(description = "Livestock category", required = true) LivestockCategory category);

  /**
   * Retrieves the event history for a user and category.
   *
   * @param userId the ID of the user
   * @param category the livestock category
   * @param eventType the event type (optional)
   * @return the list of event responses
   */
  @Operation(
      summary = "Retrieve livestock event history",
      description =
          "Fetches the event history for a user and category, optionally filtered by "
              + "event type (e.g., BIRTH, DEATH, SLAUGHTER, SALE, PURCHASE).",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Event history retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LivestockEventResponse.class)))
      })
  List<LivestockEventResponse> getEventHistory(
      @Parameter(description = "User ID", required = true) Long userId,
      @Parameter(description = "Livestock category", required = true) LivestockCategory category,
      @Parameter(
              description = "Event type (e.g., BIRTH, DEATH, SLAUGHTER, SALE, PURCHASE), optional")
          String eventType);

  /**
   * Retrieves a profit report for a user and category.
   *
   * @param userId the ID of the user
   * @param category the livestock category
   * @param startDate the start date (optional)
   * @param endDate the end date (optional)
   * @return the list of profit report responses
   */
  @Operation(
      summary = "Retrieve profit report",
      description =
          "Fetches a profit report for a user and category, optionally filtered by date "
              + "range. Includes total revenue, expenses, and net profit.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Profit report retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProfitReportResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  List<ProfitReportResponse> getProfitReport(
      @Parameter(description = "User ID", required = true) Long userId,
      @Parameter(description = "Livestock category", required = true) LivestockCategory category,
      @Parameter(description = "Start date (inclusive), optional") LocalDate startDate,
      @Parameter(description = "End date (inclusive), optional") LocalDate endDate);

  /**
   * Retrieves paginated expenses for a user and category.
   *
   * @param userId the ID of the user
   * @param category the livestock category
   * @param expenseCategory the expense category (optional)
   * @param startDate the start date (optional)
   * @param endDate the end date (optional)
   * @param page the page number (0-based)
   * @param size the page size (max 100)
   * @return the paginated expense response
   */
  @Operation(
      summary = "Retrieve paginated livestock expenses",
      description =
          "Fetches expenses for a user and livestock category, optionally filtered by "
              + "expense category (e.g., PURCHASE, MEDICATION) and date range, with pagination support.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Expenses retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PaginatedExpenseResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid expense category or pagination parameters"),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  PaginatedExpenseResponse getExpenses(
      @Parameter(description = "User ID", required = true) Long userId,
      @Parameter(description = "Livestock category", required = true) LivestockCategory category,
      @Parameter(description = "Expense category (e.g., PURCHASE, MEDICATION), optional")
      ExpenseCategory expenseCategory,
      @Parameter(description = "Start date (inclusive), optional") LocalDate startDate,
      @Parameter(description = "End date (inclusive), optional") LocalDate endDate,
      @Parameter(description = "Page number (0-based), default 0") int page,
      @Parameter(description = "Page size, default 20, max 100") int size);

  /**
   * Retrieves expense summaries by category for a user and livestock category.
   *
   * @param userId the ID of the user
   * @param category the livestock category
   * @param startDate the start date (optional)
   * @param endDate the end date (optional)
   * @return the list of expense summary responses
   */
  @Operation(
      summary = "Retrieve expense summaries",
      description =
          "Fetches expense summaries (total amount and count) by expense category for a "
              + "user and livestock category, optionally filtered by date range.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Expense summaries retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExpenseSummaryResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
      })
  List<ExpenseSummaryResponse> getExpenseSummaries(
      @Parameter(description = "User ID", required = true) Long userId,
      @Parameter(description = "Livestock category", required = true) LivestockCategory category,
      @Parameter(description = "Start date (inclusive), optional") LocalDate startDate,
      @Parameter(description = "End date (inclusive), optional") LocalDate endDate);
}
