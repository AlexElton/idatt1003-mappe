package edu.ntnu.iir.bidata.fjelltur.register;

import edu.ntnu.iir.bidata.fjelltur.model.Person;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A register that holds all loggers (persons) known to the application.
 *
 * <p>Responsibility: own the collection of {@link Person} objects and support
 * looking up loggers by name, so that the user interface can offer the user a
 * list of previous loggers when a new activity is created.</p>
 */
public class PersonRegister {

  /** All registered loggers, in insertion order. */
  private final List<Person> persons;

  /** Creates an empty person register. */
  public PersonRegister() {
    this.persons = new ArrayList<>();
  }

  /**
   * Adds a person to the register.
   *
   * @param person the person to add; must not be {@code null}
   * @return {@code true} if the person was added, {@code false} if a person
   *         with the same name already exists
   * @throws NullPointerException if the person is {@code null}
   */
  public boolean addPerson(Person person) {
    java.util.Objects.requireNonNull(person, "Person must not be null");
    if (hasPerson(person)) {
      return false;
    }
    return persons.add(person);
  }

  /**
   * Returns whether a person with the same name exists in the register.
   *
   * @param person the person to look for; must not be {@code null}
   * @return {@code true} if the person is already registered
   * @throws NullPointerException if the person is {@code null}
   */
  public boolean hasPerson(Person person) {
    java.util.Objects.requireNonNull(person, "Person must not be null");
    return persons.contains(person);
  }

  /**
   * Finds a person by name, ignoring case.
   *
   * @param name the name to search for; must not be {@code null} or blank
   * @return an {@link Optional} containing the person, or empty if not found
   * @throws NullPointerException     if the name is {@code null}
   * @throws IllegalArgumentException if the name is blank
   */
  public Optional<Person> findPersonByName(String name) {
    java.util.Objects.requireNonNull(name, "Name must not be null");
    if (name.isBlank()) {
      throw new IllegalArgumentException("Name must not be blank");
    }
    return persons.stream()
        .filter(person -> person.getName().equalsIgnoreCase(name.trim()))
        .findFirst();
  }

  /**
   * Returns all loggers sorted alphabetically by name.
   *
   * @return an unmodifiable list of all loggers, sorted by name
   */
  public List<Person> getAllPersonsSortedByName() {
    return persons.stream()
        .sorted(Comparator.comparing(Person::getName, String.CASE_INSENSITIVE_ORDER))
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Returns the number of loggers in the register.
   *
   * @return the number of loggers in the register
   */
  public int getNumberOfPersons() {
    return persons.size();
  }
}
