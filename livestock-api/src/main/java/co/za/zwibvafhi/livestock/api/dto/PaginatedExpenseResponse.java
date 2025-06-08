package co.za.zwibvafhi.livestock.api.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/** Response DTO for paginated livestock expenses. */
@Data
@Builder
public class PaginatedExpenseResponse {

  private List<ExpenseResponse> content;

  private int page;

  private int size;

  private long totalElements;

  private int totalPages;
}
