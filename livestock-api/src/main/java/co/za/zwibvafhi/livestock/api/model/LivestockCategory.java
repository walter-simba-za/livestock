package co.za.zwibvafhi.livestock.api.model;

import lombok.Getter;

/**
 * Enum for livestock categories.
 */
@Getter
public enum LivestockCategory {
  GOAT("Goat"),
  SHEEP("Sheep"),
  CATTLE("Cattle");

  /**
   * -- GETTER --
   *  Gets the capitalized title of the category.
   *
   * @return The title
   */
  private final String title;

  LivestockCategory(String title) {
    this.title = title;
  }

}