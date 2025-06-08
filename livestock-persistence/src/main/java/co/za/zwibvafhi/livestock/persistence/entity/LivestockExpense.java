package co.za.zwibvafhi.livestock.persistence.entity;

import co.za.zwibvafhi.livestock.api.model.ExpenseCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Entity representing a livestock expense. */
@Entity
@Table(name = "livestock_expense")
@NoArgsConstructor
@Getter
@Setter
public class LivestockExpense {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LivestockCategory category;

  @Enumerated(EnumType.STRING)
  @Column(name = "expense_category", nullable = false)
  private ExpenseCategory expenseCategory;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column private String description;

  @Column(name = "expense_date", nullable = false)
  private LocalDate expenseDate;
}
