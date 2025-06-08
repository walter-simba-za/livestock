package co.za.zwibvafhi.livestock.api.dto;

import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/** Request DTO for initializing livestock count. */
@Getter
@Builder(toBuilder = true)
public class InitializeCountRequest {

  @NotNull(message = "Category is required")
  private final LivestockCategory category;

  @Min(value = 0, message = "Male count must be non-negative")
  private final int maleCount;

  @Min(value = 0, message = "Female count must be non-negative")
  private final int femaleCount;
}
