package co.za.zwibvafhi.livestock.core.service.helper;

import co.za.zwibvafhi.livestock.api.dto.RecordExpenseRequest;
import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.common.LivestockConstants;
import co.za.zwibvafhi.livestock.common.LivestockErrorCodes;
import co.za.zwibvafhi.livestock.common.LivestockException;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockCount;
import co.za.zwibvafhi.livestock.persistence.entity.User;
import co.za.zwibvafhi.livestock.persistence.repository.LivestockCountRepository;
import co.za.zwibvafhi.livestock.persistence.repository.UserRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Provides common validation logic for user, count, and expense operations
 * to ensure data integrity.
 */
@Component
@RequiredArgsConstructor
public class ValidationHelper {

  private final UserRepository userRepository;
  private final LivestockCountRepository countRepository;

  /**
   * Validates user existence by ID.
   *
   * @param userId ID of the user
   * @return User entity
   * @throws LivestockException if user not found
   */
  public User validateUser(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(
            () ->
                new LivestockException(
                    LivestockErrorCodes.USER_NOT_FOUND,
                    String.format(LivestockConstants.MSG_USER_NOT_FOUND, userId)));
  }

  /**
   * Validates that a livestock count does not exist for user and category.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @throws LivestockException if count exists
   */
  public void validateCountNotExists(Long userId, LivestockCategory category) {
    countRepository
        .findByUserUserIdAndCategory(userId, category)
        .ifPresent(
            count -> {
              throw new LivestockException(
                  LivestockErrorCodes.COUNT_EXISTS,
                  String.format(LivestockConstants.MSG_COUNT_EXISTS, userId, category));
            });
  }

  /**
   * Validates and retrieves livestock count for user and category.
   *
   * @param userId ID of the user
   * @param category Livestock category
   * @return Livestock count
   * @throws LivestockException if count not found
   */
  public LivestockCount validateCount(Long userId, LivestockCategory category) {
    return countRepository
        .findByUserUserIdAndCategory(userId, category)
        .orElseThrow(
            () ->
                new LivestockException(
                    LivestockErrorCodes.COUNT_NOT_FOUND,
                    String.format(LivestockConstants.MSG_COUNT_NOT_FOUND, userId, category)));
  }

  /**
   * Validates expense amount is positive.
   *
   * @param request Expense request
   * @throws LivestockException if amount is invalid
   */
  public void validateExpenseAmount(RecordExpenseRequest request) {
    if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new LivestockException(
          LivestockErrorCodes.INVALID_EXPENSE_AMOUNT,
          String.format(LivestockConstants.MSG_INVALID_EXPENSE_AMOUNT, request.getAmount()));
    }
  }

  /**
   * Validates pagination parameters.
   *
   * @param page Page number (0-based)
   * @param size Page size (max 100)
   * @throws LivestockException if parameters are invalid
   */
  public void validatePagination(int page, int size) {
    if (page < 0 || size <= 0 || size > 100) {
      throw new LivestockException(
          LivestockErrorCodes.INVALID_PAGINATION,
          String.format(LivestockConstants.MSG_INVALID_PAGINATION, page, size));
    }
  }
}