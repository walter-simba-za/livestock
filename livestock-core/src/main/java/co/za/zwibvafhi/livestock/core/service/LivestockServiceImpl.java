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
import co.za.zwibvafhi.livestock.api.model.LivestockEventType;
import co.za.zwibvafhi.livestock.common.LivestockConstants;
import co.za.zwibvafhi.livestock.common.LivestockErrorCodes;
import co.za.zwibvafhi.livestock.common.LivestockException;
import co.za.zwibvafhi.livestock.core.mapper.LivestockMapper;
import co.za.zwibvafhi.livestock.core.service.helper.CountUpdater;
import co.za.zwibvafhi.livestock.core.service.helper.LivestockIdManager;
import co.za.zwibvafhi.livestock.core.service.helper.ValidationHelper;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockCount;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockEvent;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockExpense;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockId;
import co.za.zwibvafhi.livestock.persistence.entity.User;
import co.za.zwibvafhi.livestock.persistence.repository.LivestockCountRepository;
import co.za.zwibvafhi.livestock.persistence.repository.LivestockEventRepository;
import co.za.zwibvafhi.livestock.persistence.repository.LivestockExpenseRepository;
import co.za.zwibvafhi.livestock.persistence.repository.LivestockIdRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements livestock management operations, handling counts, events, expenses,
 * and reports with caching and transaction support.
 */
@Service
@RequiredArgsConstructor
public class LivestockServiceImpl implements LivestockService {

  private final LivestockMapper livestockMapper;
  private final LivestockCountRepository countRepository;
  private final LivestockEventRepository eventRepository;
  private final LivestockExpenseRepository expenseRepository;
  private final LivestockIdRepository livestockIdRepository;
  private final LivestockIdManager idManager;
  private final CountUpdater countUpdater;
  private final ValidationHelper validationHelper;

  /**
   * Initializes livestock count for a user and category.
   *
   * @param userId ID of the user
   * @param request Count initialization details
   * @return Initialized count response
   * @throws LivestockException if user not found or count exists
   */
  @Override
  @CacheEvict(value = LivestockConstants.CACHE_COUNTS, key = "#userId + ':' + #request.category")
  public Optional<LivestockCountResponse> initializeCount(
      Long userId, InitializeCountRequest request) {
    User user = validationHelper.validateUser(userId);
    validationHelper.validateCountNotExists(userId, request.getCategory());
    LivestockCount count = livestockMapper.toCount(user, request);
    count = countRepository.save(count);
    return Optional.of(livestockMapper.toCountResponse(count));
  }

  /**
   * Records a livestock event (e.g., birth, sale) with count and ID updates.
   *
   * @param userId ID of the user
   * @param request Event details
   * @return Recorded event response
   * @throws LivestockException if user, count, sale price, or IDs are invalid
   */
  @Override
  @Transactional
  @CacheEvict(
      value = {
          LivestockConstants.CACHE_COUNTS,
          LivestockConstants.CACHE_EVENTS,
          LivestockConstants.CACHE_PROFITS
      },
      key = "#userId + ':' + #request.category")
  public Optional<LivestockEventResponse> recordEvent(Long userId, RecordEventRequest request) {
    User user = validationHelper.validateUser(userId);
    LivestockCount count = validationHelper.validateCount(userId, request.getCategory());
    validateEventSalePrice(request);
    List<LivestockId> livestockIds = idManager.validateLivestockIds(userId, request, count);
    countUpdater.validateCounts(count, request);
    countUpdater.updateCountBasedOnEvent(count, request);
    LivestockEvent event = saveEventAndIds(user, count, request, livestockIds);
    countRepository.save(count);
    return Optional.of(livestockMapper.toEventResponse(event));
  }

  /**
   * Records an expense for a livestock category.
   *
   * @param userId ID of the user
   * @param request Expense details
   * @return Recorded expense response
   * @throws LivestockException if user not found or amount invalid
   */
  @Override
  @CacheEvict(
      value = {
          LivestockConstants.CACHE_EXPENSES,
          LivestockConstants.CACHE_PROFITS,
          LivestockConstants.CACHE_EXPENSE_SUMMARIES
      },
      key = "#userId + ':' + #request.category")
  public Optional<ExpenseResponse> recordExpense(Long userId, RecordExpenseRequest request) {
    User user = validationHelper.validateUser(userId);
    validationHelper.validateExpenseAmount(request);
    LivestockExpense expense = livestockMapper.toExpense(user, request);
    expense = expenseRepository.save(expense);
    return Optional.of(livestockMapper.toExpenseResponse(expense));
  }

  /**
   * Retrieves current livestock count for a user and category.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @return Current count response, or empty if not found
   */
  @Override
  @Cacheable(value = LivestockConstants.CACHE_COUNTS, key = "#userId + ':' + #category")
  public Optional<LivestockCountResponse> getCurrentCount(Long userId, LivestockCategory category) {
    return countRepository
        .findByUserUserIdAndCategory(userId, category)
        .map(livestockMapper::toCountResponse);
  }

  /**
   * Retrieves event history for a user, category, and optional event type.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @param eventType Event type filter (optional)
   * @return List of event responses
   * @throws LivestockException if event type is invalid
   */
  @Override
  @Cacheable(
      value = LivestockConstants.CACHE_EVENTS,
      key = "#userId + ':' + #category + ':' + #eventType")
  public List<LivestockEventResponse> getEventHistory(
      Long userId, LivestockCategory category, String eventType) {
    List<LivestockEvent> events = fetchEvents(userId, category, eventType);
    return events.stream().map(livestockMapper::toEventResponse).toList();
  }

  /**
   * Generates profit report for a user, category, and date range.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @param startDate Start date (optional)
   * @param endDate End date (optional)
   * @return List of profit report responses
   * @throws LivestockException if user not found
   */
  @Override
  @Cacheable(
      value = LivestockConstants.CACHE_PROFITS,
      key = "#userId + ':' + #category + ':' + #startDate + ':' + #endDate")
  public List<ProfitReportResponse> getProfitReport(
      Long userId, LivestockCategory category, LocalDate startDate, LocalDate endDate) {
    validationHelper.validateUser(userId);
    LocalDate start = normalizeStartDate(startDate);
    LocalDate end = normalizeEndDate(endDate);
    BigDecimal totalRevenue = calculateRevenue(userId, category, start, end);
    BigDecimal totalExpenses = calculateTotalExpenses(userId, category, start, end);
    BigDecimal netProfit = totalRevenue.subtract(totalExpenses);
    return List.of(
        ProfitReportResponse.builder()
            .category(category)
            .totalRevenue(totalRevenue)
            .totalExpenses(totalExpenses)
            .netProfit(netProfit)
            .build());
  }

  /**
   * Retrieves paginated expenses for a user, category, and date range.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @param expenseCategory Expense category filter (optional)
   * @param startDate Start date (optional)
   * @param endDate End date (optional)
   * @param page Page number (0-based)
   * @param size Page size (max 100)
   * @return Paginated expense response
   * @throws LivestockException if user not found or pagination invalid
   */
  @Override
  @Cacheable(
      value = LivestockConstants.CACHE_EXPENSES,
      key =
          "#userId + ':' + #category + ':' + #expenseCategory + ':' + #startDate + ':' + #endDate + ':' + #page + ':' + #size")
  public PaginatedExpenseResponse getExpenses(
      Long userId,
      LivestockCategory category,
      ExpenseCategory expenseCategory,
      LocalDate startDate,
      LocalDate endDate,
      int page,
      int size) {
    validationHelper.validateUser(userId);
    validationHelper.validatePagination(page, size);
    LocalDate start = normalizeStartDate(startDate);
    LocalDate end = normalizeEndDate(endDate);
    Page<LivestockExpense> expensePage =
        fetchExpenses(userId, category, expenseCategory, start, end, page, size);
    return buildPaginatedResponse(expensePage);
  }

  /**
   * Retrieves expense summaries for a user, category, and date range.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @param startDate Start date (optional)
   * @param endDate End date (optional)
   * @return List of expense summary responses
   * @throws LivestockException if user not found
   */
  @Override
  @Cacheable(
      value = LivestockConstants.CACHE_EXPENSE_SUMMARIES,
      key = "#userId + ':' + #category + ':' + #startDate + ':' + #endDate")
  public List<ExpenseSummaryResponse> getExpenseSummaries(
      Long userId, LivestockCategory category, LocalDate startDate, LocalDate endDate) {
    validationHelper.validateUser(userId);
    LocalDate start = normalizeStartDate(startDate);
    LocalDate end = normalizeEndDate(endDate);
    List<Object[]> summaries =
        expenseRepository.findExpenseSummariesByUserUserIdAndCategoryAndExpenseDateBetween(
            userId, category, start, end);
    return summaries.stream().map(livestockMapper::toExpenseSummaryResponse).toList();
  }

  /**
   * Validates sale price for SALE events, clears it for others.
   *
   * @param request Event request with sale price
   * @throws LivestockException if sale price is invalid for SALE events
   */
  private void validateEventSalePrice(RecordEventRequest request) {
    if (request.getEventType() == LivestockEventType.SALE) {
      if (request.getSalePrice() == null || request.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
        throw new LivestockException(
            LivestockErrorCodes.INVALID_SALE_PRICE,
            String.format(LivestockConstants.MSG_INVALID_SALE_PRICE, request.getSalePrice()));
      }
    } else if (request.getSalePrice() != null) {
      request.setSalePrice(null);
    }
  }

  /**
   * Saves event and updates or creates livestock tag numbers.
   *
   * @param user User entity
   * @param count Livestock count
   * @param request Event request
   * @param livestockIds List of livestock IDs
   * @return Saved event
   */
  private LivestockEvent saveEventAndIds(
      User user, LivestockCount count, RecordEventRequest request, List<LivestockId> livestockIds) {
    LivestockEvent event = livestockMapper.toEvent(user, request);
    event = eventRepository.save(event);
    idManager.handleLivestockIds(user, count, event, request, livestockIds);
    return event;
  }

  /**
   * Fetches events by user, category, and optional event type.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @param eventType Event type filter (optional)
   * @return List of events
   * @throws LivestockException if event type is invalid
   */
  private List<LivestockEvent> fetchEvents(Long userId, LivestockCategory category, String eventType) {
    if (eventType != null && !eventType.isEmpty()) {
      try {
        LivestockEventType type = LivestockEventType.valueOf(eventType.toUpperCase());
        return eventRepository.findByUserUserIdAndCategoryAndEventTypeOrderByEventDate(
            userId, category, type);
      } catch (IllegalArgumentException e) {
        throw new LivestockException(
            LivestockErrorCodes.INVALID_EVENT_TYPE,
            String.format(LivestockConstants.MSG_INVALID_EVENT_TYPE, eventType));
      }
    }
    return eventRepository.findByUserUserIdAndCategoryOrderByEventDate(userId, category);
  }

  /**
   * Normalizes start date to default if null.
   *
   * @param startDate Input start date
   * @return Normalized start date
   */
  private LocalDate normalizeStartDate(LocalDate startDate) {
    return startDate != null ? startDate : LocalDate.of(2000, 1, 1);
  }

  /**
   * Normalizes end date to current date if null.
   *
   * @param endDate Input end date
   * @return Normalized end date
   */
  private LocalDate normalizeEndDate(LocalDate endDate) {
    return endDate != null ? endDate : LocalDate.now();
  }

  /**
   * Calculates revenue from sale events.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @param start Start date
   * @param end End date
   * @return Total revenue
   */
  private BigDecimal calculateRevenue(Long userId, LivestockCategory category, LocalDate start, LocalDate end) {
    List<LivestockEvent> saleEvents =
        eventRepository.findByUserUserIdAndCategoryAndEventTypeAndEventDateBetween(
            userId, category, LivestockEventType.SALE, start, end);
    return saleEvents.stream()
        .map(LivestockEvent::getSalePrice)
        .filter(Objects::nonNull)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Calculates total expenses from events, livestock IDs, and expense records.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @param start Start date
   * @param end End date
   * @return Total expenses
   */
  private BigDecimal calculateTotalExpenses(
      Long userId, LivestockCategory category, LocalDate start, LocalDate end) {
    BigDecimal eventCosts = calculateEventCosts(userId, category, start, end);
    BigDecimal purchaseCosts = calculatePurchaseCosts(userId, category);
    BigDecimal expenseCosts = calculateExpenseCosts(userId, category, start, end);
    return eventCosts.add(purchaseCosts).add(expenseCosts);
  }

  /**
   * Calculates costs from event records.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @param start Start date
   * @param end End date
   * @return Total event costs
   */
  private BigDecimal calculateEventCosts(
      Long userId, LivestockCategory category, LocalDate start, LocalDate end) {
    List<LivestockEvent> costEvents =
        eventRepository.findByUserUserIdAndCategoryAndEventDateBetween(userId, category, start, end);
    return costEvents.stream()
        .map(LivestockEvent::getCost)
        .filter(Objects::nonNull)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Calculates costs from livestock ID purchase prices.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @return Total purchase costs
   */
  private BigDecimal calculatePurchaseCosts(Long userId, LivestockCategory category) {
    List<LivestockId> livestockIds =
        livestockIdRepository.findByUserUserIdAndCategory(userId, category);
    return livestockIds.stream()
        .map(LivestockId::getPurchasePrice)
        .filter(Objects::nonNull)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Calculates costs from expense records.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @param start Start date
   * @param end End date
   * @return Total expense costs
   */
  private BigDecimal calculateExpenseCosts(
      Long userId, LivestockCategory category, LocalDate start, LocalDate end) {
    Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
    List<LivestockExpense> expenses =
        expenseRepository
            .findByUserUserIdAndCategoryAndExpenseDateBetween(userId, category, start, end, pageable)
            .getContent();
    return expenses.stream()
        .map(LivestockExpense::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Fetches expenses with optional category filter.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @param expenseCategory Expense category (optional)
   * @param start Start date
   * @param end End date
   * @param page Page number (0-based)
   * @param size Page size
   * @return Page of expenses
   */
  private Page<LivestockExpense> fetchExpenses(
      Long userId,
      LivestockCategory category,
      ExpenseCategory expenseCategory,
      LocalDate start,
      LocalDate end,
      int page,
      int size) {
    Pageable pageable = PageRequest.of(page, size);
    if (expenseCategory != null) {
      return expenseRepository.findByUserUserIdAndCategoryAndExpenseCategoryAndExpenseDateBetween(
          userId, category, expenseCategory, start, end, pageable);
    }
    return expenseRepository.findByUserUserIdAndCategoryAndExpenseDateBetween(
        userId, category, start, end, pageable);
  }

  /**
   * Builds paginated expense response.
   *
   * @param expensePage Page of expense entities
   * @return Paginated expense response
   */
  private PaginatedExpenseResponse buildPaginatedResponse(Page<LivestockExpense> expensePage) {
    List<ExpenseResponse> content =
        expensePage.getContent().stream().map(livestockMapper::toExpenseResponse).toList();
    return PaginatedExpenseResponse.builder()
        .content(content)
        .page(expensePage.getNumber())
        .size(expensePage.getSize())
        .totalElements(expensePage.getTotalElements())
        .totalPages(expensePage.getTotalPages())
        .build();
  }
}