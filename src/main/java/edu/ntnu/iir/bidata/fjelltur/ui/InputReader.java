package edu.ntnu.iir.bidata.fjelltur.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Reads and validates user input from the console.
 *
 * <p>Responsibility: turn raw console lines into validated values (integers,
 * dates, times, non-blank text). All methods re-prompt until the input is
 * valid, so callers never receive invalid values and the application never
 * crashes on bad user input.</p>
 */
public class InputReader {

  /** The scanner the input is read from. */
  private final Scanner scanner;

  /**
   * Creates an input reader that reads from the given scanner.
   *
   * @param scanner the scanner to read from; must not be {@code null}
   */
  public InputReader(Scanner scanner) {
    this.scanner = scanner;
  }

  /**
   * Reads an integer, re-prompting until a valid integer is entered.
   *
   * @param prompt the prompt to print before reading
   * @return the integer read from the user
   */
  public int readInt(String prompt) {
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
   * Reads a date, re-prompting until a valid date in the ISO format
   * yyyy-MM-dd is entered.
   *
   * @param prompt the prompt to print before reading
   * @return the date read from the user
   */
  public LocalDate readDate(String prompt) {
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
   * Reads a time of day, re-prompting until a valid time in the format HH:mm
   * is entered.
   *
   * @param prompt the prompt to print before reading
   * @return the time read from the user
   */
  public LocalTime readTime(String prompt) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    while (true) {
      String line = readLine(prompt);
      try {
        return LocalTime.parse(line.trim(), formatter);
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
  public String readLine(String prompt) {
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
  public String readNonBlankLine(String prompt) {
    while (true) {
      String line = readLine(prompt).trim();
      if (!line.isEmpty()) {
        return line;
      }
      System.out.println("The input must not be empty.");
    }
  }
}
