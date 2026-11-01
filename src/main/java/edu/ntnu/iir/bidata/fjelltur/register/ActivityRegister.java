package edu.ntnu.iir.bidata.fjelltur.register;

import edu.ntnu.iir.bidata.fjelltur.model.Activity;
import edu.ntnu.iir.bidata.fjelltur.model.Person;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * A register that holds and manages all activities in the application.
 *
 * <p>Responsibility: own the collection of {@link Activity} objects and provide
 * the business logic for adding, removing, searching and listing activities.
 * The register never prints anything — presentation is the responsibility of
 * the user interface, which keeps the coupling between register and UI low.</p>
 *
 * <p>Activities are stored in an {@code ArrayList}: the register is small,
 * searched linearly by streams, and must preserve insertion order, so a
 * hash-based collection would add complexity without benefit.</p>
 */
public class ActivityRegister {

  /** All registered activities, in insertion order. */
  private final List<Activity> activities;

  /** Creates an empty activity register. */
  public ActivityRegister() {
    this.activities = new java.util.ArrayList<>();
  }

  /**
   * Adds an activity to the register.
   *
   * @param activity the activity to add; must not be {@code null}
   * @throws NullPointerException if the activity is {@code null}
   */
  public void addActivity(Activity activity) {
    activities.add(Objects.requireNonNull(activity, "Activity must not be null"));
  }

  /**
   * Removes an activity from the register.
   *
   * @param activity the activity to remove; must not be {@code null}
   * @return {@code true} if the activity was found and removed
   * @throws NullPointerException if the activity is {@code null}
   */
  public boolean removeActivity(Activity activity) {
    return activities.remove(Objects.requireNonNull(activity, "Activity must not be null"));
  }

  /**
   * Returns all activities sorted by registration time, newest first.
   *
   * @return an unmodifiable list of all activities, newest first
   */
  public List<Activity> getAllActivitiesSortedByDate() {
    return activities.stream()
        .sorted(Comparator.comparing(Activity::getRegisteredAt).reversed())
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Finds all activities registered on a specific date, regardless of time of
   * day.
   *
   * @param date the date to search for; must not be {@code null}
   * @return an unmodifiable list of activities registered on that date
   * @throws NullPointerException if the date is {@code null}
   */
  public List<Activity> findActivitiesOnDate(java.time.LocalDate date) {
    Objects.requireNonNull(date, "Date must not be null");
    return activities.stream()
        .filter(activity -> activity.getRegisteredAt().toLocalDate().equals(date))
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Finds all activities registered within a date range, both endpoints
   * included.
   *
   * @param fromDate the first date of the range; must not be {@code null}
   * @param toDate   the last date of the range; must not be {@code null}
   * @return an unmodifiable list of activities in the range, newest first
   * @throws NullPointerException     if any date is {@code null}
   * @throws IllegalArgumentException if {@code fromDate} is after {@code toDate}
   */
  public List<Activity> findActivitiesInDateRange(java.time.LocalDate fromDate,
                                                  java.time.LocalDate toDate) {
    Objects.requireNonNull(fromDate, "From-date must not be null");
    Objects.requireNonNull(toDate, "To-date must not be null");
    if (fromDate.isAfter(toDate)) {
      throw new IllegalArgumentException("From-date must not be after to-date");
    }
    return activities.stream()
        .filter(activity -> {
          java.time.LocalDate registered = activity.getRegisteredAt().toLocalDate();
          return !registered.isBefore(fromDate) && !registered.isAfter(toDate);
        })
        .sorted(Comparator.comparing(Activity::getRegisteredAt).reversed())
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Finds all activities logged by a specific person.
   *
   * @param logger the person to search for; must not be {@code null}
   * @return an unmodifiable list of activities logged by that person, newest
   *         first
   * @throws NullPointerException if the logger is {@code null}
   */
  public List<Activity> findActivitiesByLogger(Person logger) {
    Objects.requireNonNull(logger, "Logger must not be null");
    return activities.stream()
        .filter(activity -> activity.getLogger().equals(logger))
        .sorted(Comparator.comparing(Activity::getRegisteredAt).reversed())
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Finds all activities whose title or description contains the given word,
   * ignoring case.
   *
   * @param word the word to search for; must not be {@code null} or blank
   * @return an unmodifiable list of matching activities, newest first
   * @throws NullPointerException     if the word is {@code null}
   * @throws IllegalArgumentException if the word is blank
   */
  public List<Activity> findActivitiesByWord(String word) {
    Objects.requireNonNull(word, "Search word must not be null");
    if (word.isBlank()) {
      throw new IllegalArgumentException("Search word must not be blank");
    }
    String needle = word.trim().toLowerCase();
    return activities.stream()
        .filter(activity -> activity.getTitle().toLowerCase().contains(needle)
            || activity.getDescription().toLowerCase().contains(needle))
        .sorted(Comparator.comparing(Activity::getRegisteredAt).reversed())
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Finds all activities that are shared with a specific person, either
   * directly or through a group the person is a member of. Private activities
   * of other loggers are never included.
   *
   * @param person the person to find shared activities for; must not be
   *               {@code null}
   * @return an unmodifiable list of activities shared with that person, newest
   *         first
   * @throws NullPointerException if the person is {@code null}
   */
  public List<Activity> findActivitiesSharedWith(Person person) {
    Objects.requireNonNull(person, "Person must not be null");
    return activities.stream()
        .filter(activity -> !activity.isPrivate())
        .filter(activity -> activity.getSharedWith().contains(person)
            || activity.getSharedWithGroups().stream()
                .anyMatch(group -> group.hasMember(person)))
        .sorted(Comparator.comparing(Activity::getRegisteredAt).reversed())
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Returns the number of activities logged by each person, sorted
   * alphabetically by name.
   *
   * @return a map from person to activity count, sorted by person name
   */
  public Map<Person, Long> getActivityCountPerLogger() {
    return activities.stream()
        .collect(Collectors.groupingBy(Activity::getLogger,
            () -> new TreeMap<>(Comparator.comparing(Person::getName,
                String.CASE_INSENSITIVE_ORDER)),
            Collectors.counting()));
  }

  /**
   * Finds all activities of a specific type, ignoring case.
   *
   * @param type the activity type to search for; must not be {@code null} or
   *             blank
   * @return an unmodifiable list of activities of that type, newest first
   * @throws NullPointerException     if the type is {@code null}
   * @throws IllegalArgumentException if the type is blank
   */
  public List<Activity> findActivitiesByType(String type) {
    Objects.requireNonNull(type, "Type must not be null");
    if (type.isBlank()) {
      throw new IllegalArgumentException("Type must not be blank");
    }
    String needle = type.trim().toLowerCase();
    return activities.stream()
        .filter(activity -> activity.getType().equalsIgnoreCase(needle))
        .sorted(Comparator.comparing(Activity::getRegisteredAt).reversed())
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Returns the number of activities in the register.
   *
   * @return the number of activities in the register
   */
  public int getNumberOfActivities() {
    return activities.size();
  }

  /**
   * Returns the most recently added activity, if any. Useful for confirming a
   * deletion or addition in the user interface.
   *
   * @return an {@link Optional} containing the last added activity, or empty if
   *         the register is empty
   */
  public Optional<Activity> getLastAddedActivity() {
    if (activities.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(activities.get(activities.size() - 1));
  }

  /**
   * Returns the current time. Extracted as a method so that tests can reason
   * about registration times without mocking the clock.
   *
   * @return the current date and time
   */
  public static LocalDateTime now() {
    return LocalDateTime.now();
  }
}
