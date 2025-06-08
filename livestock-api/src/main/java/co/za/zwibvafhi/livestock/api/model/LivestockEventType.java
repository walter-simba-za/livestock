package co.za.zwibvafhi.livestock.api.model;

import lombok.Getter;

/**
 * Types of livestock events.
 */
@Getter
public enum LivestockEventType {
  BIRTH("Birth"),
  DEATH("Death"),
  LOST("Lost"),
  SLAUGHTER("Slaughter"),
  SALE("Sale"),
  PURCHASE("Purchase");

  /**
   * -- GETTER --
   *  Gets the capitalized title of the event type.
   *
   * @return The title
   */
  private final String title;

  LivestockEventType(String title) {
    this.title = title;
  }

}