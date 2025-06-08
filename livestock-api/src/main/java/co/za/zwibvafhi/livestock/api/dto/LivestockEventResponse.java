package co.za.zwibvafhi.livestock.api.dto;

import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockEventType;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

/** Response DTO for livestock event details. */
@Data
@Builder
public class LivestockEventResponse {

  private Long id;

  private Long userId;

  private LivestockCategory category;

  private LivestockEventType eventType;

  private int maleCount;

  private int femaleCount;

  private LocalDate eventDate;

  private BigDecimal salePrice;

  private BigDecimal cost;
}
