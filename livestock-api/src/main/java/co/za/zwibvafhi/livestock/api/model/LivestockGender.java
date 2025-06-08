package co.za.zwibvafhi.livestock.api.model;

import lombok.Getter;

/**
 * Enum for livestock genders.
 */
@Getter
public enum LivestockGender {
  MALE("Male"),
  FEMALE("Female");

  /**
   * -- GETTER --
   *  Gets the capitalized title of the gender.
   *
   * @return The title
   */
  private final String title;

  LivestockGender(String title) {
    this.title = title;
  }

}