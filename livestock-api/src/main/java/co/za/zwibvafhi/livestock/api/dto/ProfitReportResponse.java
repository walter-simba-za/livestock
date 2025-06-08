package co.za.zwibvafhi.livestock.api.dto;

import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/** Response DTO for profit report. */
@Data
@Builder
public class ProfitReportResponse {

  private LivestockCategory category;

  private BigDecimal totalRevenue;

  private BigDecimal totalExpenses;

  private BigDecimal netProfit;
}
