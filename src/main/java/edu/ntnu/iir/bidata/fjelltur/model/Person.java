package edu.ntnu.iir.bidata.fjelltur.model;

import java.util.Objects;

/**
 * Represents a person who logs activities in the trip register (a "logger").
 *
 * <p>Responsibility: hold the identity of a logger. The name is the identity of
 * the person within this local, single-machine application, so it is fixed at
 * creation and validated to be non-blank. Two persons with the same name are
 * considered the same logger, which keeps sharing and statistics consistent.</p>
 */
public class Person {

  /** The name of the person. Fixed after creation. */
  private final String name;

  /**
   * Creates a new person (logger).
   *
   * @param name the name of the person; must not be {@code null} or blank
   * @throws NullPointerException     if the name is {@code null}
   * @throws IllegalArgumentException if the name is blank
   */
  public Person(String name) {
    this.name = validateName(name);
  }

  /**
   * Returns the name of the person.
   *
   * @return the name of the person
   */
  public String getName() {
    return name;
  }

  /**
   * Validates and normalises a name.
   *
   * @param name the name to validate
   * @return the trimmed name
   * @throws NullPointerException     if the name is {@code null}
   * @throws IllegalArgumentException if the name is blank
   */
  private static String validateName(String name) {
    Objects.requireNonNull(name, "Name must not be null");
    if (name.isBlank()) {
      throw new IllegalArgumentException("Name must not be blank");
    }
    return name.trim();
  }

  /**
   * Compares this person to another object. Two persons are equal if their
   * names are equal, ignoring case.
   *
   * @param obj the object to compare with
   * @return {@code true} if the objects are persons with equal names
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Person other)) {
      return false;
    }
    return name.equalsIgnoreCase(other.name);
  }

  /**
   * Returns a hash code consistent with {@link #equals(Object)}.
   *
   * @return a hash code based on the lower-cased name
   */
  @Override
  public int hashCode() {
    return name.toLowerCase().hashCode();
  }

  /**
   * Returns the name of the person.
   *
   * @return the name of the person
   */
  @Override
  public String toString() {
    return name;
  }
}
