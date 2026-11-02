package edu.ntnu.idi.bidata.fjelltur.register;

import edu.ntnu.idi.bidata.fjelltur.model.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A register that holds all groups of loggers in the application.
 *
 * <p>Responsibility: own the collection of {@link Group} objects and support
 * creating, finding and removing groups. Membership is managed by the
 * {@link Group} itself; this register only manages which groups exist.</p>
 */
public class GroupRegister {

  /** All registered groups, in insertion order. */
  private final List<Group> groups;

  /** Creates an empty group register. */
  public GroupRegister() {
    this.groups = new ArrayList<>();
  }

  /**
   * Adds a group to the register.
   *
   * @param group the group to add; must not be {@code null}
   * @return {@code true} if the group was added, {@code false} if a group with
   *         the same name already exists
   * @throws NullPointerException if the group is {@code null}
   */
  public boolean addGroup(Group group) {
    java.util.Objects.requireNonNull(group, "Group must not be null");
    if (hasGroup(group)) {
      return false;
    }
    return groups.add(group);
  }

  /**
   * Removes a group from the register.
   *
   * @param group the group to remove; must not be {@code null}
   * @return {@code true} if the group was found and removed
   * @throws NullPointerException if the group is {@code null}
   */
  public boolean removeGroup(Group group) {
    java.util.Objects.requireNonNull(group, "Group must not be null");
    return groups.remove(group);
  }

  /**
   * Returns whether a group with the same name exists in the register.
   *
   * @param group the group to look for; must not be {@code null}
   * @return {@code true} if the group is already registered
   * @throws NullPointerException if the group is {@code null}
   */
  public boolean hasGroup(Group group) {
    java.util.Objects.requireNonNull(group, "Group must not be null");
    return groups.contains(group);
  }

  /**
   * Finds a group by name, ignoring case.
   *
   * @param name the name to search for; must not be {@code null} or blank
   * @return an {@link Optional} containing the group, or empty if not found
   * @throws NullPointerException     if the name is {@code null}
   * @throws IllegalArgumentException if the name is blank
   */
  public Optional<Group> findGroupByName(String name) {
    java.util.Objects.requireNonNull(name, "Group name must not be null");
    if (name.isBlank()) {
      throw new IllegalArgumentException("Group name must not be blank");
    }
    return groups.stream()
        .filter(group -> group.getName().equalsIgnoreCase(name.trim()))
        .findFirst();
  }

  /**
   * Returns all groups in insertion order.
   *
   * @return an unmodifiable list of all groups
   */
  public List<Group> getAllGroups() {
    return List.copyOf(groups);
  }

  /**
   * Returns the number of groups in the register.
   *
   * @return the number of groups in the register
   */
  public int getNumberOfGroups() {
    return groups.size();
  }
}
