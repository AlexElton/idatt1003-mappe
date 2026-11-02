package edu.ntnu.idi.bidata.fjelltur.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single logged mountain hike (activity) in the trip register.
 *
 * <p>Responsibility: hold all information about one hike — who logged it, the type
 * of the hike, a title, a description, when it was registered, optional
 * measurements (duration, distance, rating) and its sharing state. The class is
 * responsible for validating its own state: an {@code Activity} can never exist
 * in an invalid state, since all fields are validated in the constructor and in
 * every setter.</p>
 *
 * <p>The logger ({@link Person}) is stored as an object reference, not as a text
 * field, so that sharing and statistics can be based on identity rather than on
 * possibly ambiguous name strings.</p>
 *
 * <p>Fields that describe the hike itself (title, description) may be changed
 * after creation, while the logger and the registration time are fixed at
 * creation time and have no public setters.</p>
 */
public class Activity {

  /** Activity type constant for a regular mountain hike. */
  public static final String TYPE_MOUNTAIN_HIKE = "Mountain hike";

  /** Activity type constant for a hike on a glacier. */
  public static final String TYPE_GLACIER_HIKE = "Glacier hike";

  /** Activity type constant for a trip with a summit as the goal. */
  public static final String TYPE_SUMMIT_TRIP = "Summit trip";

  /** All activity types allowed as the value of the type field. */
  private static final String[] ALLOWED_TYPES =
      {TYPE_MOUNTAIN_HIKE, TYPE_GLACIER_HIKE, TYPE_SUMMIT_TRIP};

  /**
   * Returns all allowed activity types.
   *
   * @return a list of the allowed activity type names
   */
  public static List<String> allowedTypes() {
    return List.of(ALLOWED_TYPES);
  }

  /**
   * Returns whether the given type is one of the allowed activity types,
   * ignoring case.
   *
   * @param type the type to check
   * @return {@code true} if the type is allowed
   */
  public static boolean isAllowedType(String type) {
    return allowedTypes().stream().anyMatch(allowed -> allowed.equalsIgnoreCase(type.trim()));
  }

  /** The person who logged this activity. Fixed after creation. */
  private Person logger;

  /** The type of the hike, one of the TYPE_ constants of this class. */
  private String type;

  /** Duration of the hike in minutes; 0 means not registered. */
  private int durationMinutes;

  /** Distance of the hike in kilometres; 0 means not registered. */
  private double distanceKm;

  /** Rating of the hike from 1 (worst) to 5 (best); 0 means not rated. */
  private int rating;

  /** Location or starting point of the hike, e.g. "Gjendesheim". */
  private String location = "";

  /** Short title of the hike, e.g. "Tur til Preikestolen". */
  private String title;

  /** Longer free-text description of the hike. */
  private String description;

  /** Date and time the activity was registered. Fixed after creation. */
  private LocalDateTime registeredAt;

  /**
   * Whether this activity is private. A private activity is only visible to
   * the logger; a shared activity is visible to everyone it is shared with.
   */
  private boolean isPrivate;

  /** Loggers this activity has been shared with directly. */
  private final List<Person> sharedWith;

  /** Groups this activity has been shared with. */
  private final List<Group> sharedWithGroups;

  /**
   * Creates a new, private activity.
   *
   * @param logger      the person who logged the activity; must not be {@code null}
   * @param type        the type of the hike; must be one of the TYPE_ constants
   * @param title       a short title of the activity; must not be blank
   * @param description a longer description of the activity; must not be {@code null}
   * @param registeredAt the date and time the activity was registered; must not be {@code null}
   * @throws NullPointerException     if any argument is {@code null}
   * @throws IllegalArgumentException if the title is blank or the type is not allowed
   */
  public Activity(Person logger, String type, String title, String description,
      LocalDateTime registeredAt) {
    setLogger(logger);
    setType(type);
    setTitle(title);
    setDescription(description);
    setRegisteredAt(registeredAt);
    this.isPrivate = true;
    this.sharedWith = new ArrayList<>();
    this.sharedWithGroups = new ArrayList<>();
  }

  /**
   * Returns the type of this hike.
   *
   * @return the type of the hike, one of the TYPE_ constants of this class
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of this hike.
   *
   * @param type the new type; must be one of the TYPE_ constants of this class
   * @throws NullPointerException     if the type is {@code null}
   * @throws IllegalArgumentException if the type is not an allowed activity type
   */
  public void setType(String type) {
    Objects.requireNonNull(type, "Type must not be null");
    String trimmed = type.trim();
    for (String candidate : ALLOWED_TYPES) {
      if (candidate.equalsIgnoreCase(trimmed)) {
        this.type = candidate;
        return;
      }
    }
    throw new IllegalArgumentException(
        "Type must be one of: " + String.join(", ", ALLOWED_TYPES));
  }

  /**
   * Returns the duration of this hike in minutes.
   *
   * @return the duration in minutes, or 0 if no duration is registered
   */
  public int getDurationMinutes() {
    return durationMinutes;
  }

  /**
   * Sets the duration of this hike in minutes.
   *
   * @param durationMinutes the duration in minutes; must be positive
   * @throws IllegalArgumentException if the duration is not positive
   */
  public void setDurationMinutes(int durationMinutes) {
    if (durationMinutes <= 0) {
      throw new IllegalArgumentException("Duration must be positive");
    }
    this.durationMinutes = durationMinutes;
  }

  /**
   * Returns the distance of this hike in kilometres.
   *
   * @return the distance in kilometres, or 0 if no distance is registered
   */
  public double getDistanceKm() {
    return distanceKm;
  }

  /**
   * Sets the distance of this hike in kilometres.
   *
   * @param distanceKm the distance in kilometres; must be positive
   * @throws IllegalArgumentException if the distance is not positive
   */
  public void setDistanceKm(double distanceKm) {
    if (distanceKm <= 0 || Double.isNaN(distanceKm) || Double.isInfinite(distanceKm)) {
      throw new IllegalArgumentException("Distance must be a positive, finite number");
    }
    this.distanceKm = distanceKm;
  }

  /**
   * Returns the rating of this hike.
   *
   * @return the rating from 1 to 5, or 0 if the hike is not rated
   */
  public int getRating() {
    return rating;
  }

  /**
   * Sets the rating of this hike.
   *
   * @param rating the rating; must be between 1 and 5 inclusive
   * @throws IllegalArgumentException if the rating is outside 1 to 5
   */
  public void setRating(int rating) {
    if (rating < 1 || rating > 5) {
      throw new IllegalArgumentException("Rating must be between 1 and 5");
    }
    this.rating = rating;
  }

  /**
   * Returns the location or starting point of this hike.
   *
   * @return the location, or an empty string if no location is registered
   */
  public String getLocation() {
    return location;
  }
  /**
   * Sets the location or starting point of this hike.
   *
   * @param location the location; must not be {@code null} or blank
   * @throws NullPointerException     if the location is {@code null}
   * @throws IllegalArgumentException if the location is blank
   */
  public void setLocation(String location) {
    Objects.requireNonNull(location, "Location must not be null");
    if (location.isBlank()) {
      throw new IllegalArgumentException("Location must not be blank");
    }
    this.location = location.trim();
  }

  /**
   * Returns the person who logged this activity.
   *
   * @return the logger of this activity
   */
  public Person getLogger() {
    return logger;
  }

  /**
   * Returns the title of this activity.
   *
   * @return the title of this activity
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of this activity.
   *
   * @param title the new title; must not be blank
   * @throws IllegalArgumentException if the title is blank
   */
  public void setTitle(String title) {
    Objects.requireNonNull(title, "Title must not be null");
    if (title.isBlank()) {
      throw new IllegalArgumentException("Title must not be blank");
    }
    this.title = title.trim();
  }

  /**
   * Returns the description of this activity.
   *
   * @return the description of this activity
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of this activity.
   *
   * @param description the new description; must not be {@code null}
   * @throws NullPointerException if the description is {@code null}
   */
  public void setDescription(String description) {
    this.description = Objects.requireNonNull(description, "Description must not be null");
  }

  /**
   * Returns the date and time this activity was registered.
   *
   * @return the registration date and time
   */
  public LocalDateTime getRegisteredAt() {
    return registeredAt;
  }

  /**
   * Returns whether this activity is private.
   *
   * @return {@code true} if the activity is private, {@code false} if it is shared
   */
  public boolean isPrivate() {
    return isPrivate;
  }

  /**
   * Returns an unmodifiable view of the loggers this activity is shared with.
   *
   * @return an unmodifiable list of persons this activity is shared with
   */
  public List<Person> getSharedWith() {
    return Collections.unmodifiableList(sharedWith);
  }

  /**
   * Returns an unmodifiable view of the groups this activity is shared with.
   *
   * @return an unmodifiable list of groups this activity is shared with
   */
  public List<Group> getSharedWithGroups() {
    return Collections.unmodifiableList(sharedWithGroups);
  }

  /**
   * Shares this activity with a single person. The activity becomes shared
   * (no longer private).
   *
   * @param person the person to share with; must not be {@code null}
   * @throws NullPointerException if the person is {@code null}
   */
  public void shareWith(Person person) {
    Objects.requireNonNull(person, "Person to share with must not be null");
    if (!sharedWith.contains(person)) {
      sharedWith.add(person);
    }
    isPrivate = false;
  }

  /**
   * Shares this activity with all members of a group. The activity becomes
   * shared (no longer private).
   *
   * @param group the group to share with; must not be {@code null}
   * @throws NullPointerException if the group is {@code null}
   */
  public void shareWith(Group group) {
    Objects.requireNonNull(group, "Group to share with must not be null");
    if (!sharedWithGroups.contains(group)) {
      sharedWithGroups.add(group);
    }
    isPrivate = false;
  }

  /**
   * Removes all sharing of this activity, making it private again.
   */
  public void makePrivate() {
    sharedWith.clear();
    sharedWithGroups.clear();
    isPrivate = true;
  }

  /**
   * Sets the logger of this activity. Package-private because the logger is
   * fixed at creation time; used by the constructor to reuse validation.
   *
   * @param logger the logger; must not be {@code null}
   * @throws NullPointerException if the logger is {@code null}
   */
  private void setLogger(Person logger) {
    this.logger = Objects.requireNonNull(logger, "Logger must not be null");
  }

  /**
   * Sets the registration time of this activity. Private because the
   * registration time is fixed at creation time; used by the constructor to
   * reuse validation.
   *
   * @param registeredAt the registration date and time; must not be {@code null}
   * @throws NullPointerException if the registration time is {@code null}
   */
  private void setRegisteredAt(LocalDateTime registeredAt) {
    this.registeredAt = Objects.requireNonNull(registeredAt, "Registration time must not be null");
  }
}
