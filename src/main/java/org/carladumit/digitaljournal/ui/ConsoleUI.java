package org.carladumit.digitaljournal.ui;

import org.carladumit.digitaljournal.exceptions.EntryAlreadyExistsException;
import org.carladumit.digitaljournal.model.JournalEntry;
import org.carladumit.digitaljournal.model.Rating;
import org.carladumit.digitaljournal.model.User;
import org.carladumit.digitaljournal.service.JournalService;
import org.carladumit.digitaljournal.service.UserService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleUI {

    public static Scanner sc = new Scanner(System.in);

    public static void start () throws SQLException, EntryAlreadyExistsException {

        while(true) {
            System.out.println("----------------------------");
            System.out.println("       5-YEAR JOURNAL       ");
            System.out.println("----------------------------");
            System.out.println("[1] log in");
            System.out.println("[2] sign up");
            System.out.println("[0] exit");

            int option = readOption();

            switch (option) {
                case 1 -> promptLogin();
                case 2 -> promptRegister();
                case 0 -> {
                    System.out.println("Closing...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static int readOption() throws SQLException {
        while(true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introduce a valid option.");
            }
        }
    }

    public static void promptLogin() throws SQLException, EntryAlreadyExistsException {
        System.out.println("-------------------");
        System.out.println("      LOG IN       ");
        System.out.println("-------------------");

        String username = readUsername();
        String password = readPassword();

        User user = UserService.login(username, password);
        if (user != null) {
            journalMenu();
        }
    }

    public static void promptRegister() throws SQLException, EntryAlreadyExistsException {
        System.out.println("-------------------");
        System.out.println("      SIGN UP      ");
        System.out.println("-------------------");

        String username = readUsername();
        String password = readPassword();
        System.out.print("Confirm ");
        String passwordConfirmation = readPassword();

        if (password.equals(passwordConfirmation)) {
            boolean ok = UserService.register(username, password);
            if (ok) {
                journalMenu();
            } else { System.out.println("Passwords do not match."); }
        }
    }

    static String readUsername() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim().toLowerCase();
        while (username.isEmpty()) {
            System.out.println("Please input username: ");
            username = sc.nextLine().trim();
        }
        return username;
    }

    static String readPassword() {
        System.out.print("Password: ");
        String password = sc.nextLine().trim();
        while (password.isEmpty()) {
            System.out.println("Please input password: ");
            password = sc.nextLine().trim();
        }
        return password;
    }

    public static void journalMenu() throws EntryAlreadyExistsException, SQLException {
        while (true) {
            System.out.println("----------------------------");
            System.out.println("        WELCOME BACK!       ");
            System.out.println("----------------------------");
            System.out.println("[1] new entry");
            System.out.println("[2] read journal");
            System.out.println("[3] delete entry");
            System.out.println("[0] sign out");

            int option = readOption();

            switch (option) {
                case 1 -> promptJournalEntry();
                case 2 -> promptReadEntriesByDate();
                case 3 -> promptDeleteEntry();
                case 0 -> {
                    UserService.logout();
                    System.out.println("Closing your journal...");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void promptJournalEntry() throws EntryAlreadyExistsException {
        System.out.println("-------------------");
        System.out.println("     NEW ENTRY     ");
        System.out.println("-------------------");

        LocalDate entryDate = LocalDate.now();
        String rating = promptRating();
        String text = promptText();

        JournalService.createEntry(entryDate, rating, text);
    }

    static String promptRating() {
        while (true) {
            System.out.println("How was your day? ");
            System.out.println("😭 SAD | 🙁 BAD | 😐 NEUTRAL | 🙂 GOOD | 😄 GREAT");
            String rating = sc.nextLine().trim().toUpperCase();
            try {
                Rating selected = Rating.valueOf(rating);
                return selected.name();
            } catch (IllegalArgumentException e) {
            System.out.println("Please choose one valid option.");
             }
        }
    }

    static String promptText() {
        System.out.print("Spill your thoughts...");
        return sc.nextLine();
    }

    public static void promptReadEntriesByDate() throws SQLException {
        System.out.println("-------------------");
        System.out.println("   YOUR ENTRIES    ");
        System.out.println("-------------------");

        LocalDate date = promptDate();
        JournalEntry entry = JournalService.readEntriesByDate(date);
        if (entry == null) {
            System.out.println("No entry found for " + date);
            return;
        }

        System.out.println("----------------------------");
        System.out.println("Date: " + entry.getEntryDate());
        System.out.println("It was a " + entry.getRating() + " day.");
        System.out.println(entry.getText());
        }

    public static LocalDate promptDate(){
        while (true) {
            System.out.print("Date (yyyy-mm-dd): ");
            try {
                return LocalDate.parse(sc.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please try again.");
            }
        }
    }

    public static void promptDeleteEntry() throws SQLException {
        System.out.println("-------------------");
        System.out.println("    DELETE ENTRY   ");
        System.out.println("-------------------");
        LocalDate date = promptDate();
        JournalService.deleteEntry(date);
    }
}
