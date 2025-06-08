package co.za.zwibvafhi.livestock.api.dto;

import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockEventType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/** Request DTO for recording a livestock event. */
@Data
@Builder
public class RecordEventRequest {

  @NotNull(message = "Category is required")
  private LivestockCategory category;

  @NotNull(message = "Event type is required")
  private LivestockEventType eventType;

  @Min(value = 0, message = "Male count must be non-negative")
  private int maleCount;

  @Min(value = 0, message = "Female count must be non-negative")
  private int femaleCount;

  @Min(value = 0, message = "Sale price must be non-negative")
  private BigDecimal salePrice;

  @Min(value = 0, message = "Cost must be non-negative")
  private BigDecimal cost;

  private List<String> livestockIds;
}
