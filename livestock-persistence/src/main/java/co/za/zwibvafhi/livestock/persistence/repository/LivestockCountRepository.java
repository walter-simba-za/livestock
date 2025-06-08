package co.za.zwibvafhi.livestock.persistence.repository;

import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockCount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing livestock count entities.
 *
 * <p>Provides CRUD operations for {@link LivestockCount} and custom queries to retrieve counts
 * by user ID and livestock category.
 */
@Repository
public interface LivestockCountRepository extends JpaRepository<LivestockCount, Long> {

  /**
   * Finds a livestock count by user ID and category.
   *
   * @param userId the ID of the user
   * @param category the livestock category (e.g., CATTLE, GOAT)
   * @return an {@link Optional} containing the count if found, or empty if not
   */
  Optional<LivestockCount> findByUserUserIdAndCategory(Long userId, LivestockCategory category);
}