package co.za.zwibvafhi.livestock.api.dto;

import co.za.zwibvafhi.livestock.api.model.ExpenseCategory;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/** Response DTO for expense summary by category. */
@Data
@Builder
public class ExpenseSummaryResponse {

  private ExpenseCategory expenseCategory;

  private BigDecimal totalAmount;

  private long expenseCount;
}
