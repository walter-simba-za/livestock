package co.za.zwibvafhi.livestock.api.model;

import lombok.Getter;

/**
 * Enum representing the status of livestock.
 */
@Getter
public enum LivestockStatus {
  ALIVE("Alive"),
  SOLD("Sold"),
  DECEASED("Deceased"),
  SLAUGHTERED("Slaughtered"),
  LOST("Lost");

  /**
   * -- GETTER --
   *  Gets the capitalized title of the status.
   *
   * @return The title
   */
  private final String title;

  LivestockStatus(String title) {
    this.title = title;
  }

}