package edu.ntnu.iir.bidata.fjelltur.model;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a group of loggers, e.g. "Fjellgjengen", that activities can be
 * shared with.
 *
 * <p>Responsibility: hold a group name and its members, and manage membership.
 * Members are kept in a {@link LinkedHashSet} so that each person appears at
 * most once and iteration order is stable. The group does not know anything
 * about activities — sharing is modelled on the {@link Activity} side.</p>
 */
public class Group {

  /** The name of the group. Fixed after creation. */
  private final String name;

  /** The members of the group, in insertion order. */
  private final Set<Person> members;

  /**
   * Creates a new, empty group.
   *
   * @param name the name of the group; must not be {@code null} or blank
   * @throws NullPointerException     if the name is {@code null}
   * @throws IllegalArgumentException if the name is blank
   */
  public Group(String name) {
    this.name = Objects.requireNonNull(name, "Group name must not be null");
    if (name.isBlank()) {
      throw new IllegalArgumentException("Group name must not be blank");
    }
    this.members = new LinkedHashSet<>();
  }

  /**
   * Returns the name of the group.
   *
   * @return the name of the group
   */
  public String getName() {
    return name;
  }

  /**
   * Returns an unmodifiable view of the members of the group.
   *
   * @return an unmodifiable set of the group members
   */
  public Set<Person> getMembers() {
    return java.util.Collections.unmodifiableSet(members);
  }

  /**
   * Adds a person to the group.
   *
   * @param person the person to add; must not be {@code null}
   * @return {@code true} if the person was added, {@code false} if they were
   *         already a member
   * @throws NullPointerException if the person is {@code null}
   */
  public boolean addMember(Person person) {
    return members.add(Objects.requireNonNull(person, "Member must not be null"));
  }

  /**
   * Removes a person from the group.
   *
   * @param person the person to remove; must not be {@code null}
   * @return {@code true} if the person was removed, {@code false} if they were
   *         not a member
   * @throws NullPointerException if the person is {@code null}
   */
  public boolean removeMember(Person person) {
    return members.remove(Objects.requireNonNull(person, "Member must not be null"));
  }

  /**
   * Returns whether a person is a member of the group.
   *
   * @param person the person to look for; must not be {@code null}
   * @return {@code true} if the person is a member of the group
   * @throws NullPointerException if the person is {@code null}
   */
  public boolean hasMember(Person person) {
    return members.contains(Objects.requireNonNull(person, "Member must not be null"));
  }

  /**
   * Compares this group to another object. Two groups are equal if their names
   * are equal, ignoring case.
   *
   * @param obj the object to compare with
   * @return {@code true} if the objects are groups with equal names
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Group other)) {
      return false;
    }
    return name.equalsIgnoreCase(other.name);
  }

  /**
   * Returns a hash code consistent with {@link #equals(Object)}.
   *
   * @return a hash code based on the lower-cased group name
   */
  @Override
  public int hashCode() {
    return name.toLowerCase().hashCode();
  }
}
