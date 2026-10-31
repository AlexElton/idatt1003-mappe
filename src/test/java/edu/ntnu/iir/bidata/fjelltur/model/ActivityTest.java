package edu.ntnu.iir.bidata.fjelltur.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Activity} class.
 *
 * <p>Test plan (written before the tests were implemented):</p>
 * <ul>
 *   <li><b>Positive:</b> a valid activity is created with correct field
 *       values; title and description can be changed; sharing with a person
 *       and with a group makes the activity shared; {@code makePrivate()}
 *       clears sharing; getters return the stored values.</li>
 *   <li><b>Negative:</b> {@code null} logger, {@code null} registration time
 *       and blank title throw exceptions in the constructor; blank title is
 *       rejected by the setter; {@code null} description is rejected;
 *       sharing with {@code null} person/group throws; sharing the same
 *       person twice does not duplicate the entry.</li>
 * </ul>
 */
class ActivityTest {

  /** A valid logger used by most tests. */
  private Person logger;

  /** A valid registration time used by most tests. */
  private LocalDateTime registeredAt;

  @BeforeEach
  void setUp() {
    logger = new Person("Alex");
    registeredAt = LocalDateTime.of(2026, 10, 1, 12, 0);
  }

  @Nested
  @DisplayName("Positive tests")
  class PositiveTests {

    @Test
    @DisplayName("Creating an activity with valid data stores all field values")
    void creatingActivityWithValidDataStoresAllFields() {
      Activity activity = new Activity(logger, "Tur til Besseggen",
          "Klassisk ryggtrase.", registeredAt);

      assertEquals(logger, activity.getLogger());
      assertEquals("Tur til Besseggen", activity.getTitle());
      assertEquals("Klassisk ryggtrase.", activity.getDescription());
      assertEquals(registeredAt, activity.getRegisteredAt());
      assertTrue(activity.isPrivate());
    }

    @Test
    @DisplayName("Title and description can be changed after creation")
    void titleAndDescriptionCanBeChanged() {
      Activity activity = new Activity(logger, "Old title", "Old description", registeredAt);

      activity.setTitle("New title");
      activity.setDescription("New description");

      assertEquals("New title", activity.getTitle());
      assertEquals("New description", activity.getDescription());
    }

    @Test
    @DisplayName("Sharing with a person makes the activity shared")
    void sharingWithPersonMakesActivityShared() {
      Activity activity = new Activity(logger, "Title", "Description", registeredAt);
      Person friend = new Person("Nora");

      activity.shareWith(friend);

      assertFalse(activity.isPrivate());
      assertEquals(List.of(friend), activity.getSharedWith());
    }

    @Test
    @DisplayName("Sharing with a group makes the activity shared")
    void sharingWithGroupMakesActivityShared() {
      Activity activity = new Activity(logger, "Title", "Description", registeredAt);
      Group group = new Group("Fjellgjengen");

      activity.shareWith(group);

      assertFalse(activity.isPrivate());
      assertEquals(List.of(group), activity.getSharedWithGroups());
    }

    @Test
    @DisplayName("makePrivate clears all sharing and marks the activity private")
    void makePrivateClearsSharing() {
      Activity activity = new Activity(logger, "Title", "Description", registeredAt);
      activity.shareWith(new Person("Nora"));
      activity.shareWith(new Group("Fjellgjengen"));

      activity.makePrivate();

      assertTrue(activity.isPrivate());
      assertTrue(activity.getSharedWith().isEmpty());
      assertTrue(activity.getSharedWithGroups().isEmpty());
    }

    @Test
    @DisplayName("Title is trimmed when set")
    void titleIsTrimmed() {
      Activity activity = new Activity(logger, "  Tur til Besseggen  ",
          "Description", registeredAt);

      assertEquals("Tur til Besseggen", activity.getTitle());
    }
  }

  @Nested
  @DisplayName("Negative tests")
  class NegativeTests {

    @Test
    @DisplayName("Creating an activity with null logger throws NullPointerException")
    void creatingActivityWithNullLoggerThrows() {
      assertThrows(NullPointerException.class,
          () -> new Activity(null, "Title", "Description", registeredAt));
    }

    @Test
    @DisplayName("Creating an activity with null registration time throws NullPointerException")
    void creatingActivityWithNullTimeThrows() {
      assertThrows(NullPointerException.class,
          () -> new Activity(logger, "Title", "Description", null));
    }

    @Test
    @DisplayName("Creating an activity with blank title throws IllegalArgumentException")
    void creatingActivityWithBlankTitleThrows() {
      assertThrows(IllegalArgumentException.class,
          () -> new Activity(logger, "   ", "Description", registeredAt));
    }

    @Test
    @DisplayName("Creating an activity with null title throws NullPointerException")
    void creatingActivityWithNullTitleThrows() {
      assertThrows(NullPointerException.class,
          () -> new Activity(logger, null, "Description", registeredAt));
    }

    @Test
    @DisplayName("Setting a blank title throws IllegalArgumentException")
    void settingBlankTitleThrows() {
      Activity activity = new Activity(logger, "Title", "Description", registeredAt);

      assertThrows(IllegalArgumentException.class, () -> activity.setTitle("  "));
    }

    @Test
    @DisplayName("Setting a null description throws NullPointerException")
    void settingNullDescriptionThrows() {
      Activity activity = new Activity(logger, "Title", "Description", registeredAt);

      assertThrows(NullPointerException.class, () -> activity.setDescription(null));
    }

    @Test
    @DisplayName("Sharing with null person throws NullPointerException")
    void sharingWithNullPersonThrows() {
      Activity activity = new Activity(logger, "Title", "Description", registeredAt);

      assertThrows(NullPointerException.class, () -> activity.shareWith((Person) null));
    }

    @Test
    @DisplayName("Sharing with null group throws NullPointerException")
    void sharingWithNullGroupThrows() {
      Activity activity = new Activity(logger, "Title", "Description", registeredAt);

      assertThrows(NullPointerException.class, () -> activity.shareWith((Group) null));
    }

    @Test
    @DisplayName("Sharing the same person twice does not duplicate the entry")
    void sharingSamePersonTwiceDoesNotDuplicate() {
      Activity activity = new Activity(logger, "Title", "Description", registeredAt);
      Person friend = new Person("Nora");

      activity.shareWith(friend);
      activity.shareWith(new Person("Nora"));

      assertEquals(1, activity.getSharedWith().size());
    }

    @Test
    @DisplayName("The shared-with list cannot be modified from outside")
    void sharedWithListIsUnmodifiable() {
      Activity activity = new Activity(logger, "Title", "Description", registeredAt);

      assertThrows(UnsupportedOperationException.class,
          () -> activity.getSharedWith().add(new Person("Nora")));
    }
  }

  @Test
  @DisplayName("Persons with the same name are equal")
  void personsWithSameNameAreEqual() {
    assertEquals(new Person("Nora"), new Person("nora"));
    assertNotEquals(new Person("Nora"), new Person("Jonas"));
  }
}
