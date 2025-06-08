package co.za.zwibvafhi.livestock.persistence.entity;

import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockEventType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Entity representing a livestock event. */
@Entity
@Table(name = "livestock_event")
@NoArgsConstructor
@Getter
@Setter
public class LivestockEvent {

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
  @Column(name = "event_type", nullable = false)
  private LivestockEventType eventType;

  @Column(name = "male_count", nullable = false)
  private int maleCount;

  @Column(name = "female_count", nullable = false)
  private int femaleCount;

  @Column(name = "event_date", nullable = false)
  private LocalDate eventDate;

  @Column(name = "sale_price")
  private BigDecimal salePrice;

  @Column(name = "cost")
  private BigDecimal cost;
}
