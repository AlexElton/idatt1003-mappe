package edu.ntnu.iir.bidata.fjelltur.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.iir.bidata.fjelltur.model.Group;
import edu.ntnu.iir.bidata.fjelltur.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link GroupRegister} class.
 *
 * <p>Test plan (written before the tests were implemented):</p>
 * <ul>
 *   <li><b>Positive:</b> adding groups grows the register; lookup by name
 *       finds the group, ignoring case; removing a registered group
 *       works.</li>
 *   <li><b>Negative:</b> adding {@code null} throws; adding the same group
 *       twice returns {@code false}; lookup with {@code null}/blank name
 *       throws; removing an unregistered group returns {@code false}.</li>
 * </ul>
 */
class GroupRegisterTest {

  /** The register under test. */
  private GroupRegister register;

  @BeforeEach
  void setUp() {
    register = new GroupRegister();
  }

  @Test
  @DisplayName("Adding a group increases the number of groups")
  void addingGroupIncreasesCount() {
    register.addGroup(new Group("Fjellgjengen"));

    assertEquals(1, register.getNumberOfGroups());
  }

  @Test
  @DisplayName("Finding a group by name finds the group, ignoring case")
  void findingGroupByNameIgnoresCase() {
    register.addGroup(new Group("Fjellgjengen"));

    assertTrue(register.findGroupByName("fjellgjengen").isPresent());
  }

  @Test
  @DisplayName("Removing a registered group returns true and removes it")
  void removingRegisteredGroupRemovesIt() {
    Group group = new Group("Fjellgjengen");
    register.addGroup(group);

    assertTrue(register.removeGroup(group));
    assertEquals(0, register.getNumberOfGroups());
  }

  @Test
  @DisplayName("Adding a null group throws NullPointerException")
  void addingNullGroupThrows() {
    assertThrows(NullPointerException.class, () -> register.addGroup(null));
  }

  @Test
  @DisplayName("Adding the same group twice returns false and does not duplicate")
  void addingSameGroupTwiceDoesNotDuplicate() {
    register.addGroup(new Group("Fjellgjengen"));

    assertFalse(register.addGroup(new Group("fjellgjengen")));
    assertEquals(1, register.getNumberOfGroups());
  }

  @Test
  @DisplayName("Finding a group with null name throws NullPointerException")
  void findingGroupWithNullNameThrows() {
    assertThrows(NullPointerException.class, () -> register.findGroupByName(null));
  }

  @Test
  @DisplayName("Finding a group with blank name throws IllegalArgumentException")
  void findingGroupWithBlankNameThrows() {
    assertThrows(IllegalArgumentException.class, () -> register.findGroupByName("  "));
  }

  @Test
  @DisplayName("Removing a group not in the register returns false")
  void removingUnregisteredGroupReturnsFalse() {
    assertFalse(register.removeGroup(new Group("Sykkelklubben")));
  }
}
