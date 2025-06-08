package co.za.zwibvafhi.livestock.persistence.entity;

import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import co.za.zwibvafhi.livestock.api.model.LivestockGender;
import co.za.zwibvafhi.livestock.api.model.LivestockStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a unique livestock identifier with gender and status.
 */
@Entity
@Table(name = "livestock_id")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LivestockId {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "tag_number", nullable = false)
  private String tagNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LivestockCategory category;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LivestockGender gender;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LivestockStatus status;

  @ManyToOne
  @JoinColumn(name = "event_id", nullable = false)
  private LivestockEvent event;

  @Column(name = "purchase_price")
  private BigDecimal purchasePrice;
}