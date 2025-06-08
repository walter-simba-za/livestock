package co.za.zwibvafhi.livestock.api.dto;

import co.za.zwibvafhi.livestock.api.model.ExpenseCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

/** Response DTO for livestock expense details. */
@Data
@Builder
public class ExpenseResponse {

  private Long id;

  private Long userId;

  private LivestockCategory category;

  private ExpenseCategory expenseCategory;

  private BigDecimal amount;

  private String description;

  private LocalDate date;
}
