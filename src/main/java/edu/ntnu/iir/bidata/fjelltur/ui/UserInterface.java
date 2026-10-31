package edu.ntnu.iir.bidata.fjelltur.ui;

import edu.ntnu.iir.bidata.fjelltur.model.Activity;
import edu.ntnu.iir.bidata.fjelltur.model.Person;
import edu.ntnu.iir.bidata.fjelltur.register.ActivityRegister;
import edu.ntnu.iir.bidata.fjelltur.register.GroupRegister;
import edu.ntnu.iir.bidata.fjelltur.register.PersonRegister;
import java.time.LocalDateTime;

/**
 * The text-based user interface of the Fjellturregister application.
 *
 * <p>Responsibility: handle all user interaction — reading input, printing
 * output and presenting menus. All business logic lives in the register
 * classes; this class only orchestrates them, which keeps the coupling between
 * UI and business logic low (layered architecture).</p>
 *
 * <p>For Del 1 and Del 2 of the portfolio project, {@link #start()} only runs
 * hardcoded demonstration code. The full menu is implemented in Del 3.</p>
 */
public class UserInterface {

  /** The register of all activities. */
  private ActivityRegister activityRegister;

  /** The register of all loggers. */
  private PersonRegister personRegister;

  /** The register of all groups. */
  private GroupRegister groupRegister;

  /**
   * Initialises the application: creates the registers and fills them with
   * demonstration data.
   */
  public void init() {
    this.activityRegister = new ActivityRegister();
    this.personRegister = new PersonRegister();
    this.groupRegister = new GroupRegister();
    createDemoData();
  }

  /**
   * Starts the application. For Del 1 and Del 2 this only demonstrates the
   * register functionality with hardcoded data; the interactive menu is
   * implemented in Del 3.
   */
  public void start() {
    System.out.println("=== Fjellturregister ===");
    System.out.println();
    printAllActivities();
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

    edu.ntnu.iir.bidata.fjelltur.model.Group fjellgjengen =
        new edu.ntnu.iir.bidata.fjelltur.model.Group("Fjellgjengen");
    fjellgjengen.addMember(nora);
    fjellgjengen.addMember(jonas);
    groupRegister.addGroup(fjellgjengen);

    Activity besseggen = new Activity(alex, Activity.TYPE_MOUNTAIN_HIKE, "Tur til Besseggen",
        "Klassisk ryggtrase over Besseggen ved Gjende.", LocalDateTime.of(2026, 9, 12, 9, 30));
    Activity gaustatoppen = new Activity(nora, Activity.TYPE_SUMMIT_TRIP, "Gaustatoppen i solnedgang",
        "Kveldstur til Gaustatoppen, 1882 moh.", LocalDateTime.of(2026, 10, 3, 17, 0));
    final Activity storronden = new Activity(jonas, Activity.TYPE_MOUNTAIN_HIKE, "Storronden runde",
        "Rundtur i Rondane med pause ved Rondvassbu.", LocalDateTime.of(2026, 10, 18, 10, 15));
    Activity morgenstund = new Activity(alex, Activity.TYPE_MOUNTAIN_HIKE, "Morgenstund pa Vettakollen",
        "Kort morgenstur med utsikt over Oslofjorden.", LocalDateTime.of(2026, 10, 25, 7, 45));

    gaustatoppen.shareWith(fjellgjengen);
    morgenstund.shareWith(nora);

    activityRegister.addActivity(besseggen);
    activityRegister.addActivity(gaustatoppen);
    activityRegister.addActivity(storronden);
    activityRegister.addActivity(morgenstund);
  }

  /**
   * Prints all activities in the register, newest first, in a structured and
   * readable format.
   */
  private void printAllActivities() {
    System.out.println("Alle registrerte aktiviteter (nyeste forst):");
    System.out.println();
    for (Activity activity : activityRegister.getAllActivitiesSortedByDate()) {
      printActivity(activity);
      System.out.println();
    }
  }

  /**
   * Prints the details of a single activity, including logger, sharing state
   * and who the activity is shared with.
   *
   * @param activity the activity to print; must not be {@code null}
   */
  private void printActivity(Activity activity) {
    System.out.println("Tittel:       " + activity.getTitle());
    System.out.println("Loggfoert av: " + activity.getLogger().getName());
    System.out.println("Dato/tid:     " + activity.getRegisteredAt());
    System.out.println("Beskrivelse:  " + activity.getDescription());
    if (activity.isPrivate()) {
      System.out.println("Deling:       Privat");
    } else {
      System.out.println("Deling:       Delt");
      activity.getSharedWith().forEach(person ->
          System.out.println("  - Delt med person: " + person.getName()));
      activity.getSharedWithGroups().forEach(group ->
          System.out.println("  - Delt med gruppe: " + group.getName()));
    }
  }
}
