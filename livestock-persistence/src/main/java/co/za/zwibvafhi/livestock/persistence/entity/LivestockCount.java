package co.za.zwibvafhi.livestock.persistence.entity;

import co.za.zwibvafhi.livestock.api.model.LivestockCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing livestock count.
 * */
@Entity
@Table(name = "livestock_count")
@NoArgsConstructor
@Getter
@Setter
public class LivestockCount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LivestockCategory category;

  @Column(name = "male_count", nullable = false)
  private int maleCount;

  @Column(name = "female_count", nullable = false)
  private int femaleCount;

  @Column(name = "max_id", nullable = false)
  private int maxId;
}
