package edu.ntnu.iir.bidata.fjelltur.ui;

import edu.ntnu.iir.bidata.fjelltur.model.Activity;
import edu.ntnu.iir.bidata.fjelltur.model.Group;
import edu.ntnu.iir.bidata.fjelltur.model.Person;
import edu.ntnu.iir.bidata.fjelltur.register.ActivityRegister;
import edu.ntnu.iir.bidata.fjelltur.register.GroupRegister;
import edu.ntnu.iir.bidata.fjelltur.register.PersonRegister;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * The text-based user interface of the Fjellturregister application.
 *
 * <p>Responsibility: handle all user interaction — reading input, printing
 * output and presenting menus. All business logic lives in the register
 * classes; this class only orchestrates them, which keeps the coupling between
 * UI and business logic low (layered architecture).</p>
 *
 * <p>The menu loop reads input with a {@link Scanner} and re-prompts on
 * invalid input, so the application never crashes on bad user input.</p>
 */
public class UserInterface {

  /** Menu choice: list all activities. */
  private static final int MENU_LIST_ACTIVITIES = 1;

  /** Menu choice: register a new activity. */
  private static final int MENU_REGISTER_ACTIVITY = 2;

  /** Menu choice: open the search menu. */
  private static final int MENU_SEARCH = 3;

  /** Menu choice: delete an activity. */
  private static final int MENU_DELETE_ACTIVITY = 4;

  /** Menu choice: open the loggers menu. */
  private static final int MENU_LOGGERS = 5;

  /** Menu choice: open the groups menu. */
  private static final int MENU_GROUPS = 6;

  /** Menu choice: open the sharing menu. */
  private static final int MENU_SHARING = 7;

  /** Menu choice: show statistics. */
  private static final int MENU_STATISTICS = 8;

  /** Menu choice: exit the application. */
  private static final int MENU_EXIT = 9;

  /** Search menu choice: search by date. */
  private static final int SEARCH_BY_DATE = 1;

  /** Search menu choice: search by date range. */
  private static final int SEARCH_BY_DATE_RANGE = 2;

  /** Search menu choice: search by logger. */
  private static final int SEARCH_BY_LOGGER = 3;

  /** Search menu choice: search by word. */
  private static final int SEARCH_BY_WORD = 4;

  /** Search menu choice: search by activity type. */
  private static final int SEARCH_BY_TYPE = 5;

  /** Search menu choice: back to the main menu. */
  private static final int SEARCH_BACK = 6;

  /** Loggers menu choice: list all loggers. */
  private static final int LOGGERS_LIST = 1;

  /** Loggers menu choice: add a new logger. */
  private static final int LOGGERS_ADD = 2;

  /** Loggers menu choice: back to the main menu. */
  private static final int LOGGERS_BACK = 3;

  /** Groups menu choice: list all groups and their members. */
  private static final int GROUPS_LIST = 1;

  /** Groups menu choice: create a new group. */
  private static final int GROUPS_CREATE = 2;

  /** Groups menu choice: add a member to a group. */
  private static final int GROUPS_ADD_MEMBER = 3;

  /** Groups menu choice: remove a member from a group. */
  private static final int GROUPS_REMOVE_MEMBER = 4;

  /** Groups menu choice: back to the main menu. */
  private static final int GROUPS_BACK = 5;

  /** Sharing menu choice: share an activity with a person. */
  private static final int SHARING_SHARE_WITH_PERSON = 1;

  /** Sharing menu choice: share an activity with a group. */
  private static final int SHARING_SHARE_WITH_GROUP = 2;

  /** Sharing menu choice: make an activity private again. */
  private static final int SHARING_MAKE_PRIVATE = 3;

  /** Sharing menu choice: show activities shared with a person. */
  private static final int SHARING_SHOW_SHARED_WITH_PERSON = 4;

  /** Sharing menu choice: back to the main menu. */
  private static final int SHARING_BACK = 5;

  /** The register of all activities. */
  private ActivityRegister activityRegister;

  /** The register of all loggers. */
  private PersonRegister personRegister;

  /** The register of all groups. */
  private GroupRegister groupRegister;

  /** Reads user input from the standard input stream. */
  private Scanner scanner;

  /**
   * Initialises the application: creates the registers, the input reader and
   * fills them with demonstration data.
   */
  public void init() {
    this.activityRegister = new ActivityRegister();
    this.personRegister = new PersonRegister();
    this.groupRegister = new GroupRegister();
    this.scanner = new Scanner(System.in);
    createDemoData();
  }

  /**
   * Starts the application: runs the main menu loop until the user chooses to
   * exit. Invalid menu choices are reported and the menu is shown again, so
   * the application never crashes on bad input.
   */
  public void start() {
    System.out.println("Fjellturregister");
    System.out.println();

    boolean running = true;
    while (running) {
      printMainMenu();
      int choice = readInt("> ");
      System.out.println();
      switch (choice) {
        case MENU_LIST_ACTIVITIES -> listActivities();
        case MENU_REGISTER_ACTIVITY -> registerActivity();
        case MENU_SEARCH -> searchMenu();
        case MENU_DELETE_ACTIVITY -> deleteActivity();
        case MENU_LOGGERS -> loggersMenu();
        case MENU_GROUPS -> groupsMenu();
        case MENU_SHARING -> sharingMenu();
        case MENU_STATISTICS -> showStatistics();
        case MENU_EXIT -> running = false;
        default -> System.out.println("Invalid choice. Please enter a number from the menu.");
      }
      System.out.println();
    }
    System.out.println("Thank you for using Fjellturregister. Goodbye!");
  }

  /**
   * Prints the main menu.
   */
  private void printMainMenu() {
    System.out.println("Main menu");
    System.out.println(MENU_LIST_ACTIVITIES + ") List activities");
    System.out.println(MENU_REGISTER_ACTIVITY + ") Register activity");
    System.out.println(MENU_SEARCH + ") Search");
    System.out.println(MENU_DELETE_ACTIVITY + ") Delete activity");
    System.out.println(MENU_LOGGERS + ") Loggers");
    System.out.println(MENU_GROUPS + ") Groups");
    System.out.println(MENU_SHARING + ") Sharing");
    System.out.println(MENU_STATISTICS + ") Statistics");
    System.out.println(MENU_EXIT + ") Exit");
  }

  /**
   * Lists all activities in the register, newest first.
   */
  private void listActivities() {
    List<Activity> activities = activityRegister.getAllActivitiesSortedByDate();
    if (activities.isEmpty()) {
      System.out.println("No activities registered.");
      return;
    }
    System.out.println("All registered activities (newest first):");
    System.out.println();
    activities.forEach(activity -> {
      printActivity(activity);
      System.out.println();
    });
  }

  /**
   * Runs the search menu, offering search by date, date range, logger, word
   * and activity type. Each search prints the matching activities.
   */
  private void searchMenu() {
    boolean back = false;
    while (!back) {
      printSearchMenu();
      int choice = readInt("> ");
      System.out.println();
      switch (choice) {
        case SEARCH_BY_DATE -> searchByDate();
        case SEARCH_BY_DATE_RANGE -> searchByDateRange();
        case SEARCH_BY_LOGGER -> searchByLogger();
        case SEARCH_BY_WORD -> searchByWord();
        case SEARCH_BY_TYPE -> searchByType();
        case SEARCH_BACK -> back = true;
        default -> System.out.println("Invalid choice. Please enter a number from the menu.");
      }
      System.out.println();
    }
  }

  /**
   * Prints the search menu.
   */
  private void printSearchMenu() {
    System.out.println("Search");
    System.out.println(SEARCH_BY_DATE + ") Search by date");
    System.out.println(SEARCH_BY_DATE_RANGE + ") Search by date range");
    System.out.println(SEARCH_BY_LOGGER + ") Search by logger");
    System.out.println(SEARCH_BY_WORD + ") Search by word in title/description");
    System.out.println(SEARCH_BY_TYPE + ") Search by activity type");
    System.out.println(SEARCH_BACK + ") Back to main menu");
  }

  /**
   * Searches for activities registered on a date read from the user.
   */
  private void searchByDate() {
    LocalDate date = readDate("Date (yyyy-MM-dd): ");
    printSearchResult(activityRegister.findActivitiesOnDate(date));
  }

  /**
   * Searches for activities within a date range read from the user.
   */
  private void searchByDateRange() {
    LocalDate fromDate = readDate("From date (yyyy-MM-dd): ");
    LocalDate toDate = readDate("To date (yyyy-MM-dd): ");
    try {
      printSearchResult(activityRegister.findActivitiesInDateRange(fromDate, toDate));
    } catch (IllegalArgumentException exception) {
      System.out.println("Error: " + exception.getMessage());
    }
  }

  /**
   * Searches for activities logged by a logger chosen by the user.
   */
  private void searchByLogger() {
    Person logger = selectLogger();
    if (logger == null) {
      return;
    }
    printSearchResult(activityRegister.findActivitiesByLogger(logger));
  }

  /**
   * Searches for activities whose title or description contains a word read
   * from the user.
   */
  private void searchByWord() {
    String word = readNonBlankLine("Word to search for: ");
    printSearchResult(activityRegister.findActivitiesByWord(word));
  }

  /**
   * Searches for activities of a type read from the user.
   */
  private void searchByType() {
    System.out.println("Available types: " + String.join(", ", allowedTypes()));
    String type = readNonBlankLine("Type: ");
    try {
      printSearchResult(activityRegister.findActivitiesByType(type));
    } catch (IllegalArgumentException exception) {
      System.out.println("Error: " + exception.getMessage());
    }
  }

  /**
   * Runs the loggers menu, offering listing and adding loggers.
   */
  private void loggersMenu() {
    boolean back = false;
    while (!back) {
      printLoggersMenu();
      int choice = readInt("> ");
      System.out.println();
      switch (choice) {
        case LOGGERS_LIST -> listLoggers();
        case LOGGERS_ADD -> addLogger();
        case LOGGERS_BACK -> back = true;
        default -> System.out.println("Invalid choice. Please enter a number from the menu.");
      }
      System.out.println();
    }
  }

  /**
   * Prints the loggers menu.
   */
  private void printLoggersMenu() {
    System.out.println("Loggers");
    System.out.println(LOGGERS_LIST + ") List loggers");
    System.out.println(LOGGERS_ADD + ") Add logger");
    System.out.println(LOGGERS_BACK + ") Back to main menu");
  }

  /**
   * Lists all loggers sorted alphabetically by name.
   */
  private void listLoggers() {
    List<Person> loggers = personRegister.getAllPersonsSortedByName();
    if (loggers.isEmpty()) {
      System.out.println("No loggers registered.");
      return;
    }
    System.out.println("Registered loggers:");
    loggers.forEach(logger -> System.out.println("  - " + logger.getName()));
  }

  /**
   * Adds a new logger with a name read from the user. Duplicate names are
   * reported and the logger is not added twice.
   */
  private void addLogger() {
    String name = readNonBlankLine("Name of the new logger: ");
    boolean added = personRegister.addPerson(new Person(name));
    if (added) {
      System.out.println("Logger \"" + name.trim() + "\" was added.");
    } else {
      System.out.println("A logger with that name already exists.");
    }
  }

  /**
   * Runs the groups menu, offering listing, creating and managing group
   * membership.
   */
  private void groupsMenu() {
    boolean back = false;
    while (!back) {
      printGroupsMenu();
      int choice = readInt("> ");
      System.out.println();
      switch (choice) {
        case GROUPS_LIST -> listGroups();
        case GROUPS_CREATE -> createGroup();
        case GROUPS_ADD_MEMBER -> addMemberToGroup();
        case GROUPS_REMOVE_MEMBER -> removeMemberFromGroup();
        case GROUPS_BACK -> back = true;
        default -> System.out.println("Invalid choice. Please enter a number from the menu.");
      }
      System.out.println();
    }
  }

  /**
   * Prints the groups menu.
   */
  private void printGroupsMenu() {
    System.out.println("Groups");
    System.out.println(GROUPS_LIST + ") List groups");
    System.out.println(GROUPS_CREATE + ") Create group");
    System.out.println(GROUPS_ADD_MEMBER + ") Add member to group");
    System.out.println(GROUPS_REMOVE_MEMBER + ") Remove member from group");
    System.out.println(GROUPS_BACK + ") Back to main menu");
  }

  /**
   * Lists all groups with their members.
   */
  private void listGroups() {
    List<Group> groups = groupRegister.getAllGroups();
    if (groups.isEmpty()) {
      System.out.println("No groups registered.");
      return;
    }
    System.out.println("Registered groups:");
    groups.forEach(group -> {
      System.out.println("  " + group.getName() + ":");
      group.getMembers().forEach(member ->
          System.out.println("    - " + member.getName()));
    });
  }

  /**
   * Creates a new group with a name read from the user. Duplicate names are
   * reported and the group is not created twice.
   */
  private void createGroup() {
    String name = readNonBlankLine("Name of the new group: ");
    boolean created = groupRegister.addGroup(new Group(name));
    if (created) {
      System.out.println("Group \"" + name.trim() + "\" was created.");
    } else {
      System.out.println("A group with that name already exists.");
    }
  }

  /**
   * Adds a chosen logger as a member of a chosen group.
   */
  private void addMemberToGroup() {
    Group group = selectGroup();
    if (group == null) {
      return;
    }
    Person member = selectLogger();
    if (member == null) {
      return;
    }
    boolean added = group.addMember(member);
    if (added) {
      System.out.println(member.getName() + " was added to " + group.getName() + ".");
    } else {
      System.out.println(member.getName() + " is already a member of " + group.getName() + ".");
    }
  }

  /**
   * Removes a chosen logger from a chosen group.
   */
  private void removeMemberFromGroup() {
    Group group = selectGroup();
    if (group == null) {
      return;
    }
    Person member = selectLogger();
    if (member == null) {
      return;
    }
    boolean removed = group.removeMember(member);
    if (removed) {
      System.out.println(member.getName() + " was removed from " + group.getName() + ".");
    } else {
      System.out.println(member.getName() + " is not a member of " + group.getName() + ".");
    }
  }

  /**
   * Runs the sharing menu, offering sharing with a person or a group, making
   * an activity private again and listing activities shared with a person.
   */
  private void sharingMenu() {
    boolean back = false;
    while (!back) {
      printSharingMenu();
      int choice = readInt("> ");
      System.out.println();
      switch (choice) {
        case SHARING_SHARE_WITH_PERSON -> shareActivityWithPerson();
        case SHARING_SHARE_WITH_GROUP -> shareActivityWithGroup();
        case SHARING_MAKE_PRIVATE -> makeActivityPrivate();
        case SHARING_SHOW_SHARED_WITH_PERSON -> showActivitiesSharedWithPerson();
        case SHARING_BACK -> back = true;
        default -> System.out.println("Invalid choice. Please enter a number from the menu.");
      }
      System.out.println();
    }
  }

  /**
   * Prints the sharing menu.
   */
  private void printSharingMenu() {
    System.out.println("Sharing");
    System.out.println(SHARING_SHARE_WITH_PERSON + ") Share activity with person");
    System.out.println(SHARING_SHARE_WITH_GROUP + ") Share activity with group");
    System.out.println(SHARING_MAKE_PRIVATE + ") Make activity private");
    System.out.println(SHARING_SHOW_SHARED_WITH_PERSON + ") Show activities shared with a person");
    System.out.println(SHARING_BACK + ") Back to main menu");
  }

  /**
   * Shares a chosen activity with a chosen person.
   */
  private void shareActivityWithPerson() {
    Activity activity = selectActivity();
    if (activity == null) {
      return;
    }
    Person person = selectLogger();
    if (person == null) {
      return;
    }
    activity.shareWith(person);
    System.out.println("The activity is now shared with " + person.getName() + ".");
  }

  /**
   * Shares a chosen activity with a chosen group.
   */
  private void shareActivityWithGroup() {
    Activity activity = selectActivity();
    if (activity == null) {
      return;
    }
    Group group = selectGroup();
    if (group == null) {
      return;
    }
    activity.shareWith(group);
    System.out.println("The activity is now shared with " + group.getName() + ".");
  }

  /**
   * Makes a chosen activity private again, removing all sharing.
   */
  private void makeActivityPrivate() {
    Activity activity = selectActivity();
    if (activity == null) {
      return;
    }
    activity.makePrivate();
    System.out.println("The activity is now private.");
  }

  /**
   * Lists all activities shared with a chosen person, either directly or
   * through a group.
   */
  private void showActivitiesSharedWithPerson() {
    Person person = selectLogger();
    if (person == null) {
      return;
    }
    List<Activity> shared = activityRegister.findActivitiesSharedWith(person);
    if (shared.isEmpty()) {
      System.out.println("No activities are shared with " + person.getName() + ".");
      return;
    }
    System.out.println("Activities shared with " + person.getName() + ":");
    System.out.println();
    shared.forEach(activity -> {
      printActivity(activity);
      System.out.println();
    });
  }

  /**
   * Shows statistics: the number of activities per logger and the number of
   * shared versus private activities.
   */
  private void showStatistics() {
    System.out.println("Statistics");
    System.out.println();

    Map<Person, Long> countPerLogger = activityRegister.getActivityCountPerLogger();
    if (countPerLogger.isEmpty()) {
      System.out.println("No activities registered.");
    } else {
      System.out.println("Activities per logger:");
      System.out.println("Name                Count");
      countPerLogger.forEach((logger, count) ->
          System.out.printf("%-18s  %5d%n", logger.getName(), count));
    }
    System.out.println();

    long sharedCount = activityRegister.getAllActivitiesSortedByDate().stream()
        .filter(activity -> !activity.isPrivate())
        .count();
    long privateCount = activityRegister.getNumberOfActivities() - sharedCount;
    System.out.println("Shared activities:  " + sharedCount);
    System.out.println("Private activities: " + privateCount);
  }

  /**
   * Registers a new activity: reads all fields from the user, offers the list
   * of existing loggers or creation of a new one, and adds the activity to
   * the register.
   */
  private void registerActivity() {
    System.out.println("Register a new activity");
    Person logger = selectOrCreateLogger();
    if (logger == null) {
      return;
    }

    System.out.println("Available types: " + String.join(", ", allowedTypes()));
    String type = readNonBlankLine("Type: ");
    if (!isAllowedType(type)) {
      System.out.println("Error: \"" + type.trim() + "\" is not a valid activity type.");
      return;
    }

    String title = readNonBlankLine("Title: ");
    String description = readNonBlankLine("Description: ");
    LocalDate date = readDate("Date of the hike (yyyy-MM-dd): ");
    LocalDateTime timeOfHike = date.atTime(readTime("Time of the hike (HH:mm): "));

    Activity activity = new Activity(logger, type, title, description, timeOfHike);
    readOptionalMeasurements(activity);
    readOptionalLocation(activity);

    activityRegister.addActivity(activity);
    System.out.println();
    System.out.println("The activity was registered:");
    printActivity(activity);
  }

  /**
   * Asks for duration, distance and rating, and stores the values on the
   * activity. Empty input leaves the value unregistered.
   *
   * @param activity the activity to store the measurements on
   */
  private void readOptionalMeasurements(Activity activity) {
    String durationInput = readLine("Duration in minutes (empty to skip): ");
    if (!durationInput.isBlank()) {
      try {
        activity.setDurationMinutes(Integer.parseInt(durationInput.trim()));
      } catch (IllegalArgumentException exception) {
        System.out.println("Invalid duration, it is not registered.");
      }
    }

    String distanceInput = readLine("Distance in km (empty to skip): ");
    if (!distanceInput.isBlank()) {
      try {
        activity.setDistanceKm(Double.parseDouble(distanceInput.trim()));
      } catch (IllegalArgumentException exception) {
        System.out.println("Invalid distance, it is not registered.");
      }
    }

    String ratingInput = readLine("Rating 1-5 (empty to skip): ");
    if (!ratingInput.isBlank()) {
      try {
        activity.setRating(Integer.parseInt(ratingInput.trim()));
      } catch (IllegalArgumentException exception) {
        System.out.println("Invalid rating, it is not registered.");
      }
    }
  }

  /**
   * Asks for a location and stores it on the activity. Empty input leaves the
   * location unregistered.
   *
   * @param activity the activity to store the location on
   */
  private void readOptionalLocation(Activity activity) {
    String locationInput = readLine("Location/starting point (empty to skip): ");
    if (!locationInput.isBlank()) {
      try {
        activity.setLocation(locationInput);
      } catch (IllegalArgumentException exception) {
        System.out.println("Invalid location, it is not registered.");
      }
    }
  }

  /**
   * Deletes a chosen activity from the register after confirmation.
   */
  private void deleteActivity() {
    Activity activity = selectActivity();
    if (activity == null) {
      return;
    }
    String confirmation = readNonBlankLine(
        "Delete \"" + activity.getTitle() + "\"? (y/n): ");
    if (confirmation.trim().equalsIgnoreCase("y")) {
      activityRegister.removeActivity(activity);
      System.out.println("The activity was deleted.");
    } else {
      System.out.println("The activity was not deleted.");
    }
  }

  /**
   * Prints the details of a single activity, including logger, type,
   * measurements, sharing state and who the activity is shared with.
   *
   * @param activity the activity to print; must not be {@code null}
   */
  private void printActivity(Activity activity) {
    System.out.println("Title:        " + activity.getTitle());
    System.out.println("Type:         " + activity.getType());
    System.out.println("Logged by:    " + activity.getLogger().getName());
    System.out.println("Date/time:    " + activity.getRegisteredAt());
    System.out.println("Description:  " + activity.getDescription());
    if (activity.getDurationMinutes() > 0) {
      System.out.println("Duration:     " + activity.getDurationMinutes() + " min");
    }
    if (activity.getDistanceKm() > 0) {
      System.out.println("Distance:     " + activity.getDistanceKm() + " km");
    }
    if (activity.getRating() > 0) {
      System.out.println("Rating:       " + activity.getRating() + "/5");
    }
    if (!activity.getLocation().isEmpty()) {
      System.out.println("Location:     " + activity.getLocation());
    }
    if (activity.isPrivate()) {
      System.out.println("Sharing:      Private");
    } else {
      System.out.println("Sharing:      Shared");
      activity.getSharedWith().forEach(person ->
          System.out.println("  - Shared with person: " + person.getName()));
      activity.getSharedWithGroups().forEach(group ->
          System.out.println("  - Shared with group: " + group.getName()));
    }
  }

  /**
   * Prints the result of a search, or a message when nothing matched.
   *
   * @param result the matching activities, possibly empty
   */
  private void printSearchResult(List<Activity> result) {
    if (result.isEmpty()) {
      System.out.println("No matching activities found.");
      return;
    }
    System.out.println(result.size() + " matching activities:");
    System.out.println();
    result.forEach(activity -> {
      printActivity(activity);
      System.out.println();
    });
  }

  /**
   * Lets the user pick a logger from the list of registered loggers. Returns
   * {@code null} if there are no loggers or the user cancels.
   *
   * @return the chosen logger, or {@code null} if the selection was cancelled
   */
  private Person selectLogger() {
    List<Person> loggers = personRegister.getAllPersonsSortedByName();
    if (loggers.isEmpty()) {
      System.out.println("No loggers registered. Add a logger first.");
      return null;
    }
    System.out.println("Choose a logger (0 to cancel):");
    for (int i = 0; i < loggers.size(); i++) {
      System.out.println((i + 1) + ") " + loggers.get(i).getName());
    }
    int choice = readInt("> ");
    if (choice < 1 || choice > loggers.size()) {
      System.out.println("Cancelled.");
      return null;
    }
    return loggers.get(choice - 1);
  }

  /**
   * Lets the user pick an existing logger or create a new one.
   *
   * @return the chosen or newly created logger, or {@code null} if cancelled
   */
  private Person selectOrCreateLogger() {
    List<Person> loggers = personRegister.getAllPersonsSortedByName();
    if (loggers.isEmpty()) {
      return createNewLogger();
    }
    System.out.println("Logged by:");
    System.out.println("0) Create a new logger");
    for (int i = 0; i < loggers.size(); i++) {
      System.out.println((i + 1) + ") " + loggers.get(i).getName());
    }
    int choice = readInt("> ");
    if (choice == 0) {
      return createNewLogger();
    }
    if (choice < 1 || choice > loggers.size()) {
      System.out.println("Cancelled.");
      return null;
    }
    return loggers.get(choice - 1);
  }

  /**
   * Reads a name from the user and creates a new logger, which is added to
   * the person register.
   *
   * @return the new logger, or {@code null} if the name was invalid
   */
  private Person createNewLogger() {
    String name = readNonBlankLine("Name of the new logger: ");
    Person person = new Person(name);
    personRegister.addPerson(person);
    System.out.println("Logger \"" + person.getName() + "\" was created.");
    return person;
  }

  /**
   * Lets the user pick a group from the list of registered groups. Returns
   * {@code null} if there are no groups or the user cancels.
   *
   * @return the chosen group, or {@code null} if the selection was cancelled
   */
  private Group selectGroup() {
    List<Group> groups = groupRegister.getAllGroups();
    if (groups.isEmpty()) {
      System.out.println("No groups registered. Create a group first.");
      return null;
    }
    System.out.println("Choose a group (0 to cancel):");
    for (int i = 0; i < groups.size(); i++) {
      System.out.println((i + 1) + ") " + groups.get(i).getName());
    }
    int choice = readInt("> ");
    if (choice < 1 || choice > groups.size()) {
      System.out.println("Cancelled.");
      return null;
    }
    return groups.get(choice - 1);
  }

  /**
   * Lets the user pick an activity from the list of all activities, newest
   * first. Returns {@code null} if there are no activities or the user
   * cancels.
   *
   * @return the chosen activity, or {@code null} if the selection was
   *         cancelled
   */
  private Activity selectActivity() {
    List<Activity> activities = activityRegister.getAllActivitiesSortedByDate();
    if (activities.isEmpty()) {
      System.out.println("No activities registered.");
      return null;
    }
    System.out.println("Choose an activity (0 to cancel):");
    for (int i = 0; i < activities.size(); i++) {
      Activity activity = activities.get(i);
      System.out.println((i + 1) + ") " + activity.getTitle()
          + " (" + activity.getRegisteredAt().toLocalDate() + ", "
          + activity.getLogger().getName() + ")");
    }
    int choice = readInt("> ");
    if (choice < 1 || choice > activities.size()) {
      System.out.println("Cancelled.");
      return null;
    }
    return activities.get(choice - 1);
  }

  /**
   * Reads an integer from the user, re-prompting until a valid integer is
   * entered.
   *
   * @param prompt the prompt to print before reading
   * @return the integer read from the user
   */
  private int readInt(String prompt) {
    while (true) {
      String line = readLine(prompt);
      try {
        return Integer.parseInt(line.trim());
      } catch (NumberFormatException exception) {
        System.out.println("Please enter a whole number.");
      }
    }
  }

  /**
   * Reads a date from the user, re-prompting until a valid date in the
   * ISO format yyyy-MM-dd is entered.
   *
   * @param prompt the prompt to print before reading
   * @return the date read from the user
   */
  private LocalDate readDate(String prompt) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    while (true) {
      String line = readLine(prompt);
      try {
        return LocalDate.parse(line.trim(), formatter);
      } catch (DateTimeParseException exception) {
        System.out.println("Please enter a valid date in the format yyyy-MM-dd.");
      }
    }
  }

  /**
   * Reads a time of day from the user, re-prompting until a valid time in the
   * format HH:mm is entered.
   *
   * @param prompt the prompt to print before reading
   * @return the time read from the user
   */
  private java.time.LocalTime readTime(String prompt) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    while (true) {
      String line = readLine(prompt);
      try {
        return java.time.LocalTime.parse(line.trim(), formatter);
      } catch (DateTimeParseException exception) {
        System.out.println("Please enter a valid time in the format HH:mm.");
      }
    }
  }

  /**
   * Reads a line of input from the user.
   *
   * @param prompt the prompt to print before reading
   * @return the line read, without the trailing line separator
   */
  private String readLine(String prompt) {
    System.out.print(prompt);
    return scanner.nextLine();
  }

  /**
   * Reads a line of input that must not be blank, re-prompting until a
   * non-blank line is entered.
   *
   * @param prompt the prompt to print before reading
   * @return the non-blank line read, trimmed
   */
  private String readNonBlankLine(String prompt) {
    while (true) {
      String line = readLine(prompt).trim();
      if (!line.isEmpty()) {
        return line;
      }
      System.out.println("The input must not be empty.");
    }
  }

  /**
   * Returns all allowed activity types.
   *
   * @return a list of the allowed activity type names
   */
  private List<String> allowedTypes() {
    return List.of(Activity.TYPE_MOUNTAIN_HIKE, Activity.TYPE_GLACIER_HIKE,
        Activity.TYPE_SUMMIT_TRIP);
  }

  /**
   * Returns whether the given type is one of the allowed activity types,
   * ignoring case.
   *
   * @param type the type to check
   * @return {@code true} if the type is allowed
   */
  private boolean isAllowedType(String type) {
    return allowedTypes().stream().anyMatch(allowed -> allowed.equalsIgnoreCase(type.trim()));
  }

  /**
   * Fills the registers with demonstration data: three loggers, one group and
   * four activities, of which one is private and two are shared.
   */
  private void createDemoData() {
    Person alex = new Person("Alex");
    Person nora = new Person("Nora");
    Person jonas = new Person("Jonas");
    personRegister.addPerson(alex);
    personRegister.addPerson(nora);
    personRegister.addPerson(jonas);

    Group fjellgjengen = new Group("Fjellgjengen");
    fjellgjengen.addMember(nora);
    fjellgjengen.addMember(jonas);
    groupRegister.addGroup(fjellgjengen);

    Activity besseggen = new Activity(alex, Activity.TYPE_MOUNTAIN_HIKE, "Tur til Besseggen",
        "Klassisk ryggtrase over Besseggen ved Gjende.", LocalDateTime.of(2026, 9, 12, 9, 30));
    besseggen.setDurationMinutes(300);
    besseggen.setDistanceKm(21.5);
    besseggen.setRating(5);
    besseggen.setLocation("Gjendesheim");

    Activity gaustatoppen = new Activity(nora, Activity.TYPE_SUMMIT_TRIP,
        "Gaustatoppen i solnedgang", "Kveldstur til Gaustatoppen, 1882 moh.",
        LocalDateTime.of(2026, 10, 3, 17, 0));
    gaustatoppen.setDurationMinutes(240);
    gaustatoppen.setDistanceKm(12.0);
    gaustatoppen.setRating(4);

    Activity storronden = new Activity(jonas, Activity.TYPE_MOUNTAIN_HIKE, "Storronden runde",
        "Rundtur i Rondane med pause ved Rondvassbu.", LocalDateTime.of(2026, 10, 18, 10, 15));

    Activity morgenstund = new Activity(alex, Activity.TYPE_MOUNTAIN_HIKE,
        "Morgenstund pa Vettakollen", "Kort morgenstur med utsikt over Oslofjorden.",
        LocalDateTime.of(2026, 10, 25, 7, 45));
    morgenstund.setDurationMinutes(90);

    gaustatoppen.shareWith(fjellgjengen);
    morgenstund.shareWith(nora);

    activityRegister.addActivity(besseggen);
    activityRegister.addActivity(gaustatoppen);
    activityRegister.addActivity(storronden);
    activityRegister.addActivity(morgenstund);
  }
}
