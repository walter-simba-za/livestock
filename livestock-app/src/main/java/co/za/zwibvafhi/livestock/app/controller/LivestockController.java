package co.za.zwibvafhi.livestock.app.controller;

import co.za.zwibvafhi.livestock.api.contract.LivestockApi;
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
import co.za.zwibvafhi.livestock.core.service.LivestockService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller implementing the LivestockApi contract.
 * */
@RestController
@RequestMapping(value = "/api/v1/livestock", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class LivestockController implements LivestockApi {

  private final LivestockService livestockService;

  @Override
  @PostMapping("/{userId}/counts")
  public Optional<LivestockCountResponse> initializeCount(
      @PathVariable("userId") Long userId, @Valid @RequestBody InitializeCountRequest request) {
    return livestockService.initializeCount(userId, request);
  }

  @Override
  @PostMapping("/{userId}/events")
  public Optional<LivestockEventResponse> recordEvent(
      @PathVariable("userId") Long userId, @Valid @RequestBody RecordEventRequest request) {
    return livestockService.recordEvent(userId, request);
  }

  @Override
  @PostMapping("/{userId}/expenses")
  public Optional<ExpenseResponse> recordExpense(
      @PathVariable("userId") Long userId, @Valid @RequestBody RecordExpenseRequest request) {
    return livestockService.recordExpense(userId, request);
  }

  @Override
  @GetMapping("/{userId}/counts")
  public Optional<LivestockCountResponse> getCurrentCount(
      @PathVariable("userId") Long userId, @RequestParam("category") LivestockCategory category) {
    return livestockService.getCurrentCount(userId, category);
  }

  @Override
  @GetMapping("/{userId}/events")
  public List<LivestockEventResponse> getEventHistory(
      @PathVariable("userId") Long userId,
      @RequestParam("category") LivestockCategory category,
      @RequestParam(value = "eventType", required = false) String eventType) {
    return livestockService.getEventHistory(userId, category, eventType);
  }

  @Override
  @GetMapping("/{userId}/profit")
  public List<ProfitReportResponse> getProfitReport(
      @PathVariable("userId") Long userId,
      @RequestParam("category") LivestockCategory category,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate) {
    return livestockService.getProfitReport(userId, category, startDate, endDate);
  }

  @Override
  @GetMapping("/{userId}/expenses")
  public PaginatedExpenseResponse getExpenses(
      @PathVariable("userId") Long userId,
      @RequestParam("category") LivestockCategory category,
      @RequestParam(value = "expenseCategory", required = false) ExpenseCategory expenseCategory,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "20") int size) {
    return livestockService.getExpenses(
        userId, category, expenseCategory, startDate, endDate, page, size);
  }

  @Override
  @GetMapping("/{userId}/expense-summaries")
  public List<ExpenseSummaryResponse> getExpenseSummaries(
      @PathVariable("userId") Long userId,
      @RequestParam("category") LivestockCategory category,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate) {
    return livestockService.getExpenseSummaries(userId, category, startDate, endDate);
  }
}
