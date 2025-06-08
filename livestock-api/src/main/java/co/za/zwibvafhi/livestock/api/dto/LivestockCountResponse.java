package co.za.zwibvafhi.livestock.api.dto;

import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import lombok.Builder;
import lombok.Getter;

/** Response DTO for livestock count. */
@Getter
@Builder(toBuilder = true)
public class LivestockCountResponse {

  private final Long userId;

  private final LivestockCategory category;

  private final int maleCount;

  private final int femaleCount;
}
