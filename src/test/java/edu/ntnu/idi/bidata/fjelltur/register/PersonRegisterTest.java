package edu.ntnu.idi.bidata.fjelltur.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.bidata.fjelltur.model.Person;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link PersonRegister} class.
 *
 * <p>Test plan (written before the tests were implemented):</p>
 * <ul>
 *   <li><b>Positive:</b> adding persons grows the register; lookup by name
 *       finds the person, ignoring case; persons are returned sorted by
 *       name.</li>
 *   <li><b>Negative:</b> adding {@code null} throws; adding the same person
 *       twice returns {@code false} and does not duplicate; lookup with
 *       {@code null}/blank name throws; lookup of unknown name returns
 *       empty.</li>
 * </ul>
 */
class PersonRegisterTest {

  /** The register under test. */
  private PersonRegister register;

  @BeforeEach
  void setUp() {
    register = new PersonRegister();
  }

  @Test
  @DisplayName("Adding a person increases the number of persons")
  void addingPersonIncreasesCount() {
    register.addPerson(new Person("Alex"));

    assertEquals(1, register.getNumberOfPersons());
  }

  @Test
  @DisplayName("Finding a person by name finds the person, ignoring case")
  void findingPersonByNameIgnoresCase() {
    register.addPerson(new Person("Alex"));

    assertTrue(register.findPersonByName("alex").isPresent());
    assertEquals("Alex", register.findPersonByName("alex").get().getName());
  }

  @Test
  @DisplayName("All persons are returned sorted alphabetically by name")
  void allPersonsAreSortedByName() {
    register.addPerson(new Person("Nora"));
    register.addPerson(new Person("Alex"));
    register.addPerson(new Person("Jonas"));

    List<Person> sorted = register.getAllPersonsSortedByName();

    assertEquals("Alex", sorted.get(0).getName());
    assertEquals("Jonas", sorted.get(1).getName());
    assertEquals("Nora", sorted.get(2).getName());
  }

  @Test
  @DisplayName("Adding a null person throws NullPointerException")
  void addingNullPersonThrows() {
    assertThrows(NullPointerException.class, () -> register.addPerson(null));
  }

  @Test
  @DisplayName("Adding the same person twice returns false and does not duplicate")
  void addingSamePersonTwiceDoesNotDuplicate() {
    register.addPerson(new Person("Alex"));

    assertFalse(register.addPerson(new Person("alex")));
    assertEquals(1, register.getNumberOfPersons());
  }

  @Test
  @DisplayName("Finding a person with null name throws NullPointerException")
  void findingPersonWithNullNameThrows() {
    assertThrows(NullPointerException.class, () -> register.findPersonByName(null));
  }

  @Test
  @DisplayName("Finding a person with blank name throws IllegalArgumentException")
  void findingPersonWithBlankNameThrows() {
    assertThrows(IllegalArgumentException.class, () -> register.findPersonByName("  "));
  }

  @Test
  @DisplayName("Finding an unknown person returns empty")
  void findingUnknownPersonReturnsEmpty() {
    assertTrue(register.findPersonByName("Unknown").isEmpty());
  }
}
