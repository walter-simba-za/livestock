package co.za.zwibvafhi.livestock.persistence.repository;

import co.za.zwibvafhi.livestock.api.model.ExpenseCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockExpense;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing livestock expense entities.
 *
 * <p>Provides CRUD operations for {@link LivestockExpense} and custom queries to retrieve
 * expenses by user ID, category, expense category, and date ranges, with pagination and
 * summary aggregations.
 */
@Repository
public interface LivestockExpenseRepository extends JpaRepository<LivestockExpense, Long> {

  /**
   * Finds expenses by user ID, category, and date range with pagination.
   *
   * @param userId the ID of the user
   * @param category the livestock category (e.g., CATTLE, GOAT)
   * @param startDate the start of the date range (inclusive)
   * @param endDate the end of the date range (inclusive)
   * @param pageable pagination information
   * @return a paginated list of matching expenses
   */
  Page<LivestockExpense> findByUserUserIdAndCategoryAndExpenseDateBetween(
      Long userId,
      LivestockCategory category,
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable);

  /**
   * Finds expenses by user ID, category, expense category, and date range with pagination.
   *
   * @param userId the ID of the user
   * @param category the livestock category (e.g., CATTLE, GOAT)
   * @param expenseCategory the expense category (e.g., FEED, MEDICAL)
   * @param startDate the start of the date range (inclusive)
   * @param endDate the end of the date range (inclusive)
   * @param pageable pagination information
   * @return a paginated list of matching expenses
   */
  Page<LivestockExpense> findByUserUserIdAndCategoryAndExpenseCategoryAndExpenseDateBetween(
      Long userId,
      LivestockCategory category,
      ExpenseCategory expenseCategory,
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable);

  /**
   * Aggregates expenses by category for a user, category, and date range.
   *
   * @param userId the ID of the user
   * @param category the livestock category (e.g., CATTLE, GOAT)
   * @param startDate the start of the date range (inclusive)
   * @param endDate the end of the date range (inclusive)
   * @return a list of arrays containing expense category, total amount, and count
   */
  @Query(
      "SELECT e.expenseCategory, SUM(e.amount) as totalAmount, COUNT(e) as expenseCount "
          + "FROM LivestockExpense e "
          + "WHERE e.user.userId = :userId AND e.category = :category "
          + "AND e.expenseDate BETWEEN :startDate AND :endDate "
          + "GROUP BY e.expenseCategory")
  List<Object[]> findExpenseSummariesByUserUserIdAndCategoryAndExpenseDateBetween(
      @Param("userId") Long userId,
      @Param("category") LivestockCategory category,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}