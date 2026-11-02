package edu.ntnu.idi.bidata.fjelltur.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Person} class.
 *
 * <p>Test plan (written before the tests were implemented):</p>
 * <ul>
 *   <li><b>Positive:</b> a valid person is created with a trimmed name;
 *       equality is based on the name, ignoring case.</li>
 *   <li><b>Negative:</b> {@code null} name throws {@code NullPointerException};
 *       blank name throws {@code IllegalArgumentException}.</li>
 * </ul>
 */
class PersonTest {

  @Test
  @DisplayName("Creating a person with a valid name stores the trimmed name")
  void creatingPersonWithValidNameStoresTrimmedName() {
    Person person = new Person("  Alex  ");

    assertEquals("Alex", person.getName());
  }

  @Test
  @DisplayName("Creating a person with null name throws NullPointerException")
  void creatingPersonWithNullNameThrows() {
    assertThrows(NullPointerException.class, () -> new Person(null));
  }

  @Test
  @DisplayName("Creating a person with blank name throws IllegalArgumentException")
  void creatingPersonWithBlankNameThrows() {
    assertThrows(IllegalArgumentException.class, () -> new Person("   "));
  }

  @Test
  @DisplayName("Equality is based on name, ignoring case")
  void equalityIsBasedOnNameIgnoringCase() {
    assertEquals(new Person("Alex"), new Person("alex"));
    assertEquals(new Person("Alex").hashCode(), new Person("alex").hashCode());
  }
}
