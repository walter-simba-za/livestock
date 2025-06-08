package co.za.zwibvafhi.livestock.persistence.repository;

import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing livestock ID entities.
 *
 * <p>Provides CRUD operations for {@link LivestockId} and custom queries to retrieve
 * livestock IDs by user ID, category, and tag numbers.
 */
@Repository
public interface LivestockIdRepository extends JpaRepository<LivestockId, Long> {

  /**
   * Finds livestock IDs by user ID and category.
   *
   * @param userId the ID of the user
   * @param category the livestock category (e.g., CATTLE, GOAT)
   * @return a list of matching livestock IDs
   */
  List<LivestockId> findByUserUserIdAndCategory(Long userId, LivestockCategory category);

  /**
   * Finds livestock IDs by user ID, category, and tag numbers.
   *
   * @param userId the ID of the user
   * @param category the livestock category (e.g., CATTLE, GOAT)
   * @param tagNumbers the list of tag numbers to match
   * @return a list of matching livestock IDs
   */
  List<LivestockId> findByUserUserIdAndCategoryAndTagNumberIn(
      Long userId, LivestockCategory category, List<String> tagNumbers);
}