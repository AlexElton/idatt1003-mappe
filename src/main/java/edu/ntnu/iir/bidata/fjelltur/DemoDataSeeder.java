package edu.ntnu.iir.bidata.fjelltur;

import edu.ntnu.iir.bidata.fjelltur.model.Activity;
import edu.ntnu.iir.bidata.fjelltur.model.Group;
import edu.ntnu.iir.bidata.fjelltur.model.Person;
import edu.ntnu.iir.bidata.fjelltur.register.ActivityRegister;
import edu.ntnu.iir.bidata.fjelltur.register.GroupRegister;
import edu.ntnu.iir.bidata.fjelltur.register.PersonRegister;
import java.time.LocalDateTime;

/**
 * Fills the registers with demonstration data.
 *
 * <p>Responsibility: create a small, realistic set of loggers, groups and
 * activities so the application has something to show on first start. This is
 * application setup, not user interaction, so it lives outside the UI class.</p>
 */
public class DemoDataSeeder {

  /** The register of all activities. */
  private final ActivityRegister activityRegister;

  /** The register of all loggers. */
  private final PersonRegister personRegister;

  /** The register of all groups. */
  private final GroupRegister groupRegister;

  /**
   * Creates a seeder for the given registers.
   *
   * @param activityRegister the register to add activities to
   * @param personRegister   the register to add loggers to
   * @param groupRegister    the register to add groups to
   */
  public DemoDataSeeder(ActivityRegister activityRegister, PersonRegister personRegister,
      GroupRegister groupRegister) {
    this.activityRegister = activityRegister;
    this.personRegister = personRegister;
    this.groupRegister = groupRegister;
  }

  /**
   * Fills the registers with demonstration data: three loggers, one group and
   * four activities, of which one is private and two are shared.
   */
  public void seed() {
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
