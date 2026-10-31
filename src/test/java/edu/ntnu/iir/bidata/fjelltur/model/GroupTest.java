package edu.ntnu.iir.bidata.fjelltur.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Group} class.
 *
 * <p>Test plan (written before the tests were implemented):</p>
 * <ul>
 *   <li><b>Positive:</b> a valid group is created empty; members can be added
 *       and removed; {@code hasMember} reports membership correctly; the
 *       member set cannot be modified from outside.</li>
 *   <li><b>Negative:</b> {@code null} or blank group name throws in the
 *       constructor; adding {@code null} member throws; adding the same
 *       member twice does not duplicate; removing a non-member returns
 *       {@code false}.</li>
 * </ul>
 */
class GroupTest {

  /** A valid member used by most tests. */
  private Person member;

  @BeforeEach
  void setUp() {
    member = new Person("Nora");
  }

  @Test
  @DisplayName("Creating a group with a valid name creates an empty group")
  void creatingGroupWithValidNameCreatesEmptyGroup() {
    Group group = new Group("Fjellgjengen");

    assertEquals("Fjellgjengen", group.getName());
    assertTrue(group.getMembers().isEmpty());
  }

  @Test
  @DisplayName("Adding a member adds the person to the group")
  void addingMemberAddsPersonToGroup() {
    Group group = new Group("Fjellgjengen");

    assertTrue(group.addMember(member));

    assertTrue(group.hasMember(member));
    assertEquals(1, group.getMembers().size());
  }

  @Test
  @DisplayName("Removing a member removes the person from the group")
  void removingMemberRemovesPersonFromGroup() {
    Group group = new Group("Fjellgjengen");
    group.addMember(member);

    assertTrue(group.removeMember(member));

    assertFalse(group.hasMember(member));
  }

  @Test
  @DisplayName("Creating a group with null name throws NullPointerException")
  void creatingGroupWithNullNameThrows() {
    assertThrows(NullPointerException.class, () -> new Group(null));
  }

  @Test
  @DisplayName("Creating a group with blank name throws IllegalArgumentException")
  void creatingGroupWithBlankNameThrows() {
    assertThrows(IllegalArgumentException.class, () -> new Group("   "));
  }

  @Test
  @DisplayName("Adding a null member throws NullPointerException")
  void addingNullMemberThrows() {
    Group group = new Group("Fjellgjengen");

    assertThrows(NullPointerException.class, () -> group.addMember(null));
  }

  @Test
  @DisplayName("Adding the same member twice does not duplicate the entry")
  void addingSameMemberTwiceDoesNotDuplicate() {
    Group group = new Group("Fjellgjengen");

    group.addMember(member);
    group.addMember(new Person("nora"));

    assertEquals(1, group.getMembers().size());
  }

  @Test
  @DisplayName("Removing a person who is not a member returns false")
  void removingNonMemberReturnsFalse() {
    Group group = new Group("Fjellgjengen");

    assertFalse(group.removeMember(new Person("Jonas")));
  }

  @Test
  @DisplayName("The member set cannot be modified from outside")
  void memberSetIsUnmodifiable() {
    Group group = new Group("Fjellgjengen");

    assertThrows(UnsupportedOperationException.class,
        () -> group.getMembers().add(new Person("Jonas")));
  }
}
