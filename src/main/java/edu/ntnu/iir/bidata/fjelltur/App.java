package edu.ntnu.iir.bidata.fjelltur;

import edu.ntnu.iir.bidata.fjelltur.ui.UserInterface;

/**
 * The application class of the Fjellturregister. Creates the user interface
 * and hands control over to it.
 *
 * <p>Responsibility: be the entry point of the application. Contains no
 * business logic — that lives in the model and register classes.</p>
 */
public final class App {

  /** Private constructor to prevent instantiation of a utility class. */
  private App() {
  }

  /**
   * Application entry point. Creates the user interface, initialises it and
   * starts it.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    UserInterface userInterface = new UserInterface();
    userInterface.init();
    userInterface.start();
  }
}
