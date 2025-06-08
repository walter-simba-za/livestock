package co.za.zwibvafhi.livestock.core.service.helper;

import co.za.zwibvafhi.livestock.api.dto.RecordEventRequest;
import co.za.zwibvafhi.livestock.api.model.LivestockEventType;
import co.za.zwibvafhi.livestock.common.LivestockConstants;
import co.za.zwibvafhi.livestock.common.LivestockErrorCodes;
import co.za.zwibvafhi.livestock.common.LivestockException;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockCount;
import org.springframework.stereotype.Component;

/**
 * Validates and updates livestock counts based on events, ensuring counts
 * remain non-negative and accurate.
 */
@Component
public class CountUpdater {

  /**
   * Validates that event counts won't result in negative totals.
   *
   * @param count Current livestock count
   * @param request Event request with male and female counts
   * @throws LivestockException if counts would become negative
   */
  public void validateCounts(LivestockCount count, RecordEventRequest request) {
    if (isDecreasingEvent(request.getEventType())) {
      int newMaleCount = count.getMaleCount() - request.getMaleCount();
      int newFemaleCount = count.getFemaleCount() - request.getFemaleCount();
      if (newMaleCount < 0 || newFemaleCount < 0) {
        throw new LivestockException(
            LivestockErrorCodes.NEGATIVE_COUNT,
            String.format(
                LivestockConstants.MSG_NEGATIVE_COUNT,
                count.getUser().getUserId(),
                request.getCategory()));
      }
    }
  }

  /**
   * Updates livestock counts based on event type.
   *
   * @param count Current livestock count
   * @param request Event request with male and female counts
   */
  public void updateCountBasedOnEvent(LivestockCount count, RecordEventRequest request) {
    if (isIncreasingEvent(request.getEventType())) {
      count.setMaleCount(count.getMaleCount() + request.getMaleCount());
      count.setFemaleCount(count.getFemaleCount() + request.getFemaleCount());
      count.setMaxId(count.getMaxId() + request.getMaleCount() + request.getFemaleCount());
    } else if (isDecreasingEvent(request.getEventType())) {
      count.setMaleCount(Math.max(0, count.getMaleCount() - request.getMaleCount()));
      count.setFemaleCount(Math.max(0, count.getFemaleCount() - request.getFemaleCount()));
    }
  }

  /**
   * Checks if event increases counts (BIRTH or PURCHASE).
   *
   * @param eventType Event type
   * @return True if event increases counts
   */
  private boolean isIncreasingEvent(LivestockEventType eventType) {
    return eventType == LivestockEventType.BIRTH || eventType == LivestockEventType.PURCHASE;
  }

  /**
   * Checks if event decreases counts (DEATH, SLAUGHTER, SALE, LOST).
   *
   * @param eventType Event type
   * @return True if event decreases counts
   */
  private boolean isDecreasingEvent(LivestockEventType eventType) {
    return eventType == LivestockEventType.DEATH
        || eventType == LivestockEventType.SLAUGHTER
        || eventType == LivestockEventType.SALE
        || eventType == LivestockEventType.LOST;
  }
}