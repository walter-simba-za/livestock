package co.za.zwibvafhi.livestock.core.service;

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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for livestock management operations.
 */
public interface LivestockService {

  /**
   * Initializes livestock count for a user and category.
   */
  Optional<LivestockCountResponse> initializeCount(Long userId, InitializeCountRequest request);

  /**
   * Records a livestock event (e.g., birth, sale).
   */
  Optional<LivestockEventResponse> recordEvent(Long userId, RecordEventRequest request);

  /**
   * Records an expense for a livestock category.
   */
  Optional<ExpenseResponse> recordExpense(Long userId, RecordExpenseRequest request);

  /**
   * Retrieves current livestock count for a user and category.
   */
  Optional<LivestockCountResponse> getCurrentCount(Long userId, LivestockCategory category);

  /**
   * Retrieves event history for a user, category, and optional event type.
   */
  List<LivestockEventResponse> getEventHistory(
      Long userId, LivestockCategory category, String eventType);

  /**
   * Generates profit report for a user, category, and date range.
   */
  List<ProfitReportResponse> getProfitReport(
      Long userId, LivestockCategory category, LocalDate startDate, LocalDate endDate);

  /**
   * Retrieves paginated expenses for a user, category, and date range.
   */
  PaginatedExpenseResponse getExpenses(
      Long userId,
      LivestockCategory category,
      ExpenseCategory expenseCategory,
      LocalDate startDate,
      LocalDate endDate,
      int page,
      int size);

  /**
   * Retrieves expense summaries for a user, category, and date range.
   */
  List<ExpenseSummaryResponse> getExpenseSummaries(
      Long userId, LivestockCategory category, LocalDate startDate, LocalDate endDate);
}