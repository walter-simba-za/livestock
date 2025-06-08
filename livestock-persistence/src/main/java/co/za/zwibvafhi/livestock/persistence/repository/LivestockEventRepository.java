package co.za.zwibvafhi.livestock.persistence.repository;

import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockEventType;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockEvent;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing livestock event entities.
 *
 * <p>Provides CRUD operations for {@link LivestockEvent} and custom queries to retrieve events
 * by user ID, category, event type, and date ranges, ordered by event date.
 */
@Repository
public interface LivestockEventRepository extends JpaRepository<LivestockEvent, Long> {

  /**
   * Finds events by user ID, category, and event type, ordered by event date.
   *
   * @param userId the ID of the user
   * @param category the livestock category (e.g., CATTLE, GOAT)
   * @param eventType the type of event (e.g., BIRTH, SALE)
   * @return a list of matching events, ordered by event date
   */
  List<LivestockEvent> findByUserUserIdAndCategoryAndEventTypeOrderByEventDate(
      Long userId, LivestockCategory category, LivestockEventType eventType);

  /**
   * Finds events by user ID and category, ordered by event date.
   *
   * @param userId the ID of the user
   * @param category the livestock category (e.g., CATTLE, GOAT)
   * @return a list of matching events, ordered by event date
   */
  List<LivestockEvent> findByUserUserIdAndCategoryOrderByEventDate(
      Long userId, LivestockCategory category);

  /**
   * Finds events by user ID, category, event type, and date range.
   *
   * @param userId the ID of the user
   * @param category the livestock category (e.g., CATTLE, GOAT)
   * @param eventType the type of event (e.g., BIRTH, SALE)
   * @param startDate the start of the date range (inclusive)
   * @param endDate the end of the date range (inclusive)
   * @return a list of matching events
   */
  List<LivestockEvent> findByUserUserIdAndCategoryAndEventTypeAndEventDateBetween(
      Long userId,
      LivestockCategory category,
      LivestockEventType eventType,
      LocalDate startDate,
      LocalDate endDate);

  /**
   * Finds events by user ID, category, and date range.
   *
   * @param userId the ID of the user
   * @param category the livestock category (e.g., CATTLE, GOAT)
   * @param startDate the start of the date range (inclusive)
   * @param endDate the end of the date range (inclusive)
   * @return a list of matching events
   */
  List<LivestockEvent> findByUserUserIdAndCategoryAndEventDateBetween(
      Long userId, LivestockCategory category, LocalDate startDate, LocalDate endDate);
}