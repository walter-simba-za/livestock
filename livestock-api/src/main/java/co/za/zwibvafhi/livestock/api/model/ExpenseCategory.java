package co.za.zwibvafhi.livestock.api.model;

import lombok.Getter;

/**
 * Enum representing categories for livestock expenses.
 * */
@Getter
public enum ExpenseCategory {
  PURCHASE("Buying new livestock"),
  FEED("Animal feed and supplements"),
  VACCINATION("Preventive veterinary vaccinations"),
  MEDICATION("Veterinary medicines or treatments"),
  LABOUR("Wages or fees for farm workers"),
  BUILDING("Building or repairing farm structures"),
  TRANSPORT("Fuel or transport for livestock/supplies"),
  MAINTENANCE("Facility upkeep or minor repairs"),
  FENCING("Installing or repairing fences/gates"),
  EQUIPMENT("Buying or maintaining farm equipment"),
  TRAINING("Staff training or certifications"),
  MARKETING("Advertising or selling livestock/products"),
  OTHER("Miscellaneous farm expenses");

  private final String description;

  ExpenseCategory(String description) {
    this.description = description;
  }
}
