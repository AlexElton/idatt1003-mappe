package edu.ntnu.iir.bidata.fjelltur.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.iir.bidata.fjelltur.model.Activity;
import edu.ntnu.iir.bidata.fjelltur.model.Group;
import edu.ntnu.iir.bidata.fjelltur.model.Person;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ActivityRegister} class.
 *
 * <p>Test plan (written before the tests were implemented):</p>
 * <ul>
 *   <li><b>Positive:</b> adding activities grows the register; activities are
 *       returned sorted newest first; search by date, date range, logger and
 *       word return the correct activities; shared-activities search respects
 *       direct sharing and group membership; statistics count activities per
 *       logger; removal works.</li>
 *   <li><b>Negative:</b> adding {@code null} throws; searching with
 *       {@code null} dates/logger/word throws; blank search word throws;
 *       from-date after to-date throws; searching an empty register returns an
 *       empty list; removing an activity not in the register returns
 *       {@code false}; returned lists cannot be modified from outside.</li>
 * </ul>
 */
class ActivityRegisterTest {

  /** The register under test. */
  private ActivityRegister register;

  /** Loggers used in the test fixtures. */
  private Person alex;

  private Person nora;

  private Person jonas;

  @BeforeEach
  void setUp() {
    register = new ActivityRegister();
    alex = new Person("Alex");
    nora = new Person("Nora");
    jonas = new Person("Jonas");
  }

  /**
   * Creates a valid activity with the given logger and registration time.
   *
   * @param logger       the logger of the activity
   * @param registeredAt the registration time
   * @return a new valid activity
   */
  private Activity createActivity(Person logger, LocalDateTime registeredAt) {
    return new Activity(logger, "Tur til Besseggen", "Klassisk ryggtrase.", registeredAt);
  }

  @Nested
  @DisplayName("Positive tests")
  class PositiveTests {

    @Test
    @DisplayName("Adding activities increases the number of activities")
    void addingActivitiesIncreasesCount() {
      register.addActivity(createActivity(alex, LocalDateTime.of(2026, 10, 1, 12, 0)));

      assertEquals(1, register.getNumberOfActivities());
    }

    @Test
    @DisplayName("All activities are returned sorted newest first")
    void allActivitiesAreSortedNewestFirst() {
      register.addActivity(createActivity(alex, LocalDateTime.of(2026, 10, 1, 12, 0)));
      register.addActivity(createActivity(nora, LocalDateTime.of(2026, 10, 5, 8, 0)));
      register.addActivity(createActivity(jonas, LocalDateTime.of(2026, 9, 20, 9, 0)));

      List<Activity> sorted = register.getAllActivitiesSortedByDate();

      assertEquals(LocalDateTime.of(2026, 10, 5, 8, 0), sorted.get(0).getRegisteredAt());
      assertEquals(LocalDateTime.of(2026, 10, 1, 12, 0), sorted.get(1).getRegisteredAt());
      assertEquals(LocalDateTime.of(2026, 9, 20, 9, 0), sorted.get(2).getRegisteredAt());
    }

    @Test
    @DisplayName("Search by date finds only activities on that date")
    void searchByDateFindsOnlyActivitiesOnThatDate() {
      register.addActivity(createActivity(alex, LocalDateTime.of(2026, 10, 1, 8, 0)));
      register.addActivity(createActivity(nora, LocalDateTime.of(2026, 10, 1, 20, 0)));
      register.addActivity(createActivity(jonas, LocalDateTime.of(2026, 10, 2, 9, 0)));

      List<Activity> result = register.findActivitiesOnDate(LocalDate.of(2026, 10, 1));

      assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Search by date range includes both endpoints")
    void searchByDateRangeIncludesEndpoints() {
      register.addActivity(createActivity(alex, LocalDateTime.of(2026, 10, 1, 8, 0)));
      register.addActivity(createActivity(nora, LocalDateTime.of(2026, 10, 3, 9, 0)));
      register.addActivity(createActivity(jonas, LocalDateTime.of(2026, 10, 5, 10, 0)));
      register.addActivity(createActivity(alex, LocalDateTime.of(2026, 10, 7, 11, 0)));

      List<Activity> result =
          register.findActivitiesInDateRange(LocalDate.of(2026, 10, 3), LocalDate.of(2026, 10, 5));

      assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Search by logger finds only activities logged by that person")
    void searchByLoggerFindsOnlyThatLoggersActivities() {
      register.addActivity(createActivity(alex, LocalDateTime.of(2026, 10, 1, 8, 0)));
      register.addActivity(createActivity(nora, LocalDateTime.of(2026, 10, 2, 9, 0)));
      register.addActivity(createActivity(alex, LocalDateTime.of(2026, 10, 3, 10, 0)));

      List<Activity> result = register.findActivitiesByLogger(alex);

      assertEquals(2, result.size());
      assertTrue(result.stream().allMatch(activity -> activity.getLogger().equals(alex)));
    }

    @Test
    @DisplayName("Search by word matches title or description, ignoring case")
    void searchByWordMatchesTitleOrDescription() {
      Activity besseggen = new Activity(alex, "Tur til Besseggen",
          "Klassisk ryggtrase.", LocalDateTime.of(2026, 10, 1, 8, 0));
      Activity gaustatoppen = new Activity(nora, "Gaustatoppen",
          "Tung tur med utsikt.", LocalDateTime.of(2026, 10, 2, 9, 0));
      register.addActivity(besseggen);
      register.addActivity(gaustatoppen);

      List<Activity> byTitle = register.findActivitiesByWord("besseggen");
      List<Activity> byDescription = register.findActivitiesByWord("UTSIKT");

      assertEquals(1, byTitle.size());
      assertEquals(besseggen, byTitle.get(0));
      assertEquals(1, byDescription.size());
      assertEquals(gaustatoppen, byDescription.get(0));
    }

    @Test
    @DisplayName("Shared-activities search finds direct sharing and group sharing")
    void sharedActivitiesIncludeDirectAndGroupSharing() {
      Group fjellgjengen = new Group("Fjellgjengen");
      fjellgjengen.addMember(nora);

      Activity sharedDirectly = createActivity(alex, LocalDateTime.of(2026, 10, 1, 8, 0));
      sharedDirectly.shareWith(jonas);
      Activity sharedWithGroup = createActivity(alex, LocalDateTime.of(2026, 10, 2, 9, 0));
      sharedWithGroup.shareWith(fjellgjengen);
      Activity privateActivity = createActivity(alex, LocalDateTime.of(2026, 10, 3, 10, 0));

      register.addActivity(sharedDirectly);
      register.addActivity(sharedWithGroup);
      register.addActivity(privateActivity);

      List<Activity> sharedWithJonas = register.findActivitiesSharedWith(jonas);
      List<Activity> sharedWithNora = register.findActivitiesSharedWith(nora);

      assertEquals(1, sharedWithJonas.size());
      assertEquals(sharedDirectly, sharedWithJonas.get(0));
      assertEquals(1, sharedWithNora.size());
      assertEquals(sharedWithGroup, sharedWithNora.get(0));
    }

    @Test
    @DisplayName("Statistics count activities per logger")
    void statisticsCountActivitiesPerLogger() {
      register.addActivity(createActivity(alex, LocalDateTime.of(2026, 10, 1, 8, 0)));
      register.addActivity(createActivity(alex, LocalDateTime.of(2026, 10, 2, 9, 0)));
      register.addActivity(createActivity(nora, LocalDateTime.of(2026, 10, 3, 10, 0)));

      var statistics = register.getActivityCountPerLogger();

      assertEquals(2, statistics.get(alex));
      assertEquals(1, statistics.get(nora));
      assertFalse(statistics.containsKey(jonas));
    }

    @Test
    @DisplayName("Removing an activity in the register returns true and removes it")
    void removingActivityInRegisterRemovesIt() {
      Activity activity = createActivity(alex, LocalDateTime.of(2026, 10, 1, 8, 0));
      register.addActivity(activity);

      assertTrue(register.removeActivity(activity));
      assertEquals(0, register.getNumberOfActivities());
    }
  }

  @Nested
  @DisplayName("Negative tests")
  class NegativeTests {

    @Test
    @DisplayName("Adding a null activity throws NullPointerException")
    void addingNullActivityThrows() {
      assertThrows(NullPointerException.class, () -> register.addActivity(null));
    }

    @Test
    @DisplayName("Removing a null activity throws NullPointerException")
    void removingNullActivityThrows() {
      assertThrows(NullPointerException.class, () -> register.removeActivity(null));
    }

    @Test
    @DisplayName("Removing an activity not in the register returns false")
    void removingActivityNotInRegisterReturnsFalse() {
      Activity activity = createActivity(alex, LocalDateTime.of(2026, 10, 1, 8, 0));

      assertFalse(register.removeActivity(activity));
    }

    @Test
    @DisplayName("Searching with null date throws NullPointerException")
    void searchingWithNullDateThrows() {
      assertThrows(NullPointerException.class, () -> register.findActivitiesOnDate(null));
    }

    @Test
    @DisplayName("Searching with null from-date throws NullPointerException")
    void searchingWithNullFromDateThrows() {
      assertThrows(NullPointerException.class,
          () -> register.findActivitiesInDateRange(null, LocalDate.of(2026, 10, 1)));
    }

    @Test
    @DisplayName("Searching with from-date after to-date throws IllegalArgumentException")
    void searchingWithFromDateAfterToDateThrows() {
      assertThrows(IllegalArgumentException.class,
          () -> register.findActivitiesInDateRange(
              LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 1)));
    }

    @Test
    @DisplayName("Searching with null logger throws NullPointerException")
    void searchingWithNullLoggerThrows() {
      assertThrows(NullPointerException.class, () -> register.findActivitiesByLogger(null));
    }

    @Test
    @DisplayName("Searching with null word throws NullPointerException")
    void searchingWithNullWordThrows() {
      assertThrows(NullPointerException.class, () -> register.findActivitiesByWord(null));
    }

    @Test
    @DisplayName("Searching with blank word throws IllegalArgumentException")
    void searchingWithBlankWordThrows() {
      assertThrows(IllegalArgumentException.class, () -> register.findActivitiesByWord("   "));
    }

    @Test
    @DisplayName("Searching with null person for shared activities throws")
    void searchingSharedWithNullPersonThrows() {
      assertThrows(NullPointerException.class, () -> register.findActivitiesSharedWith(null));
    }

    @Test
    @DisplayName("Searching an empty register returns an empty list")
    void searchingEmptyRegisterReturnsEmptyList() {
      assertTrue(register.findActivitiesOnDate(LocalDate.of(2026, 10, 1)).isEmpty());
      assertTrue(register.getAllActivitiesSortedByDate().isEmpty());
    }

    @Test
    @DisplayName("The returned activity list cannot be modified from outside")
    void returnedListIsUnmodifiable() {
      register.addActivity(createActivity(alex, LocalDateTime.of(2026, 10, 1, 8, 0)));

      List<Activity> activities = register.getAllActivitiesSortedByDate();

      assertThrows(UnsupportedOperationException.class, () -> activities.clear());
    }
  }
}
