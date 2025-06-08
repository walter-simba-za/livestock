package co.za.zwibvafhi.livestock.api.dto;

import co.za.zwibvafhi.livestock.api.model.ExpenseCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

/**
 * Request DTO for recording a livestock expense.
 * */
@Data
@Builder
public class RecordExpenseRequest {

  @NotNull(message = "Category is required")
  private LivestockCategory category;

  @NotNull(message = "Expense category is required")
  private ExpenseCategory expenseCategory;

  @Min(value = 0, message = "Amount must be non-negative")
  private BigDecimal amount;

  private String description;

  @Builder.Default
  private LocalDate expenseDate = null;
}