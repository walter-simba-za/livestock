package co.za.zwibvafhi.livestock.core.service.helper;

import co.za.zwibvafhi.livestock.api.dto.RecordEventRequest;
import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockEventType;
import co.za.zwibvafhi.livestock.api.model.LivestockGender;
import co.za.zwibvafhi.livestock.api.model.LivestockStatus;
import co.za.zwibvafhi.livestock.common.LivestockConstants;
import co.za.zwibvafhi.livestock.common.LivestockErrorCodes;
import co.za.zwibvafhi.livestock.common.LivestockException;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockCount;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockEvent;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockId;
import co.za.zwibvafhi.livestock.persistence.entity.User;
import co.za.zwibvafhi.livestock.persistence.repository.LivestockIdRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Manages creation, validation, and updates of livestock tag numbers with
 * gender-specific prefixes (M for male, F for female).
 */
@Component
@RequiredArgsConstructor
public class LivestockIdManager {

  private final LivestockIdRepository livestockIdRepository;

  /**
   * Validates livestock tag numbers for an event, ensuring correct count and status.
   *
   * @param userId ID of the user
   * @param request Event request with tag numbers
   * @param count Livestock count for reference
   * @return List of validated livestock IDs
   * @throws LivestockException if tags are missing, mismatched, or invalid
   */
  public List<LivestockId> validateLivestockIds(
      Long userId, RecordEventRequest request, LivestockCount count) {
    int totalCount = request.getMaleCount() + request.getFemaleCount();
    if (!requiresIdValidation(request.getEventType())) {
      return validatePurchaseIds(request, totalCount);
    }
    return validateSaleOrSlaughterIds(userId, request, totalCount, count.getCategory());
  }

  /**
   * Handles creation or update of livestock tag numbers based on event type.
   *
   * @param user User entity
   * @param count Livestock count
   * @param event Livestock event
   * @param request Event request
   * @param livestockIds List of livestock IDs
   * @throws LivestockException if tag creation or update fails
   */
  public void handleLivestockIds(
      User user,
      LivestockCount count,
      LivestockEvent event,
      RecordEventRequest request,
      List<LivestockId> livestockIds) {
    if (isBirthOrPurchase(request.getEventType())) {
      createLivestockIds(user, count, event, request);
    } else if (isSaleOrSlaughterOrLost(request.getEventType())) {
      updateLivestockIds(livestockIds, event, request.getEventType());
    }
  }

  /**
   * Checks if event type requires tag validation (SALE, SLAUGHTER, or LOST).
   *
   * @param eventType Event type
   * @return True if validation is required
   */
  private boolean requiresIdValidation(LivestockEventType eventType) {
    return eventType == LivestockEventType.SALE
        || eventType == LivestockEventType.SLAUGHTER
        || eventType == LivestockEventType.LOST;
  }

  /**
   * Validates tags for PURCHASE events, ensuring count matches.
   *
   * @param request Event request
   * @param totalCount Total male and female count
   * @return Empty list if valid
   * @throws LivestockException if tag count mismatches
   */
  private List<LivestockId> validatePurchaseIds(RecordEventRequest request, int totalCount) {
    if (request.getLivestockIds() != null && request.getLivestockIds().size() != totalCount) {
      throw new LivestockException(
          LivestockErrorCodes.LIVESTOCK_ID_COUNT_MISMATCH,
          String.format(
              LivestockConstants.MSG_LIVESTOCK_ID_COUNT_MISMATCH,
              request.getLivestockIds().size(),
              totalCount));
    }
    return new ArrayList<>();
  }

  /**
   * Validates tags for SALE, SLAUGHTER, or LOST events.
   *
   * @param userId ID of the user
   * @param request Event request
   * @param totalCount Total male and female count
   * @param category Livestock category
   * @return List of validated IDs
   * @throws LivestockException if tags are invalid
   */
  private List<LivestockId> validateSaleOrSlaughterIds(
      Long userId, RecordEventRequest request, int totalCount, LivestockCategory category) {
    checkIdsProvided(request.getLivestockIds());
    List<LivestockId> livestockIds = fetchAndValidateIds(userId, request.getLivestockIds(), category);
    validateIdCount(livestockIds, totalCount);
    validateIdStatus(livestockIds);
    return livestockIds;
  }

  /**
   * Checks if tag numbers are provided.
   *
   * @param tagNumbers List of tag numbers
   * @throws LivestockException if tags are null or empty
   */
  private void checkIdsProvided(List<String> tagNumbers) {
    if (tagNumbers == null || tagNumbers.isEmpty()) {
      throw new LivestockException(
          LivestockErrorCodes.INVALID_LIVESTOCK_IDS,
          String.format(LivestockConstants.MSG_INVALID_LIVESTOCK_IDS, "No IDs provided"));
    }
  }

  /**
   * Fetches and validates livestock tag numbers from repository.
   *
   * @param userId ID of the user
   * @param tagNumbers List of tag numbers
   * @param category Livestock category
   * @return List of fetched IDs
   * @throws LivestockException if some tags are not found
   */
  private List<LivestockId> fetchAndValidateIds(
      Long userId, List<String> tagNumbers, LivestockCategory category) {
    List<LivestockId> livestockIds =
        livestockIdRepository.findByUserUserIdAndCategoryAndTagNumberIn(
            userId, category, tagNumbers);
    if (livestockIds.size() != tagNumbers.size()) {
      throw new LivestockException(
          LivestockErrorCodes.INVALID_LIVESTOCK_IDS,
          String.format(LivestockConstants.MSG_INVALID_LIVESTOCK_IDS, "Some IDs not found"));
    }
    return livestockIds;
  }

  /**
   * Validates livestock ID count matches total count.
   *
   * @param livestockIds List of livestock IDs
   * @param totalCount Expected count
   * @throws LivestockException if count mismatches
   */
  private void validateIdCount(List<LivestockId> livestockIds, int totalCount) {
    if (livestockIds.size() != totalCount) {
      throw new LivestockException(
          LivestockErrorCodes.LIVESTOCK_ID_COUNT_MISMATCH,
          String.format(
              LivestockConstants.MSG_LIVESTOCK_ID_COUNT_MISMATCH, livestockIds.size(), totalCount));
    }
  }

  /**
   * Validates all livestock IDs are in ALIVE status.
   *
   * @param livestockIds List of livestock IDs
   * @throws LivestockException if any ID is not ALIVE
   */
  private void validateIdStatus(List<LivestockId> livestockIds) {
    boolean allAlive = livestockIds.stream().allMatch(id -> id.getStatus() == LivestockStatus.ALIVE);
    if (!allAlive) {
      throw new LivestockException(
          LivestockErrorCodes.INVALID_LIVESTOCK_IDS,
          String.format(LivestockConstants.MSG_INVALID_LIVESTOCK_IDS, "Some IDs are not alive"));
    }
  }

  /**
   * Creates livestock tag numbers for BIRTH or PURCHASE events.
   *
   * @param user User entity
   * @param count Livestock count
   * @param event Livestock event
   * @param request Event request
   */
  private void createLivestockIds(
      User user, LivestockCount count, LivestockEvent event, RecordEventRequest request) {
    List<LivestockId> newIds = new ArrayList<>();
    List<String> providedIds = request.getLivestockIds() != null ? request.getLivestockIds() : null;
    int idIndex = 0;
    int maleStartIndex = count.getMaxId() + 1;
    int femaleStartIndex = maleStartIndex + request.getMaleCount();

    newIds.addAll(
        generateIdsForGender(
            request.getMaleCount(),
            user,
            count,
            event,
            request.getCategory(),
            LivestockGender.MALE,
            providedIds,
            idIndex,
            request.getCost(),
            maleStartIndex));
    idIndex += request.getMaleCount();

    newIds.addAll(
        generateIdsForGender(
            request.getFemaleCount(),
            user,
            count,
            event,
            request.getCategory(),
            LivestockGender.FEMALE,
            providedIds,
            idIndex,
            request.getCost(),
            femaleStartIndex));

    livestockIdRepository.saveAll(newIds);
  }

  /**
   * Generates livestock tag numbers for a specific gender.
   *
   * @param count Number of tags to generate
   * @param user User entity
   * @param livestockCount Livestock count
   * @param event Livestock event
   * @param category Livestock category
   * @param gender Livestock gender
   * @param providedIds Provided tags (optional)
   * @param idIndex Starting index for provided tags
   * @param cost Purchase cost (optional)
   * @param startIndex Starting index for tag generation
   * @return List of generated IDs
   */
  private List<LivestockId> generateIdsForGender(
      int count,
      User user,
      LivestockCount livestockCount,
      LivestockEvent event,
      LivestockCategory category,
      LivestockGender gender,
      List<String> providedIds,
      int idIndex,
      BigDecimal cost,
      int startIndex) {
    List<LivestockId> ids = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      ids.add(
          LivestockId.builder()
              .user(user)
              .tagNumber(
                  providedIds != null
                      ? providedIds.get(idIndex + i)
                      : generateId(startIndex + i, gender))
              .category(category)
              .gender(gender)
              .status(LivestockStatus.ALIVE)
              .event(event)
              .purchasePrice(cost)
              .build());
    }
    return ids;
  }

  /**
   * Updates livestock tag numbers for SALE, SLAUGHTER, or LOST events.
   *
   * @param livestockIds List of livestock IDs
   * @param event Livestock event
   * @param eventType Event type
   */
  private void updateLivestockIds(
      List<LivestockId> livestockIds, LivestockEvent event, LivestockEventType eventType) {
    LivestockStatus newStatus;
    if (eventType == LivestockEventType.SLAUGHTER) {
      newStatus = LivestockStatus.SLAUGHTERED;
    } else if (eventType == LivestockEventType.SALE) {
      newStatus = LivestockStatus.SOLD;
    } else {
      newStatus = LivestockStatus.LOST;
    }
    for (LivestockId id : livestockIds) {
      id.setStatus(newStatus);
      id.setEvent(event);
    }
    livestockIdRepository.saveAll(livestockIds);
  }

  /**
   * Generates a unique livestock tag number with gender prefix.
   *
   * @param index Sequential index for tag
   * @param gender Livestock gender
   * @return Generated tag (e.g., M1, F21)
   */
  private String generateId(int index, LivestockGender gender) {
    String prefix = gender == LivestockGender.MALE ? "M" : "F";
    return prefix + index;
  }

  /**
   * Checks if event is BIRTH or PURCHASE.
   *
   * @param eventType Event type
   * @return True if BIRTH or PURCHASE
   */
  private boolean isBirthOrPurchase(LivestockEventType eventType) {
    return eventType == LivestockEventType.BIRTH || eventType == LivestockEventType.PURCHASE;
  }

  /**
   * Checks if event is SALE, SLAUGHTER, or LOST.
   *
   * @param eventType Event type
   * @return True if SALE, SLAUGHTER, or LOST
   */
  private boolean isSaleOrSlaughterOrLost(LivestockEventType eventType) {
    return eventType == LivestockEventType.SALE
        || eventType == LivestockEventType.SLAUGHTER
        || eventType == LivestockEventType.LOST;
  }
}