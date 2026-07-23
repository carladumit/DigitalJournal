package org.carladumit.digitaljournal.ui;

import org.carladumit.digitaljournal.dao.JournalEntryDAO;
import org.carladumit.digitaljournal.dao.UserDAO;
import org.carladumit.digitaljournal.dao.impl.JournalEntryDAOJdbc;
import org.carladumit.digitaljournal.dao.impl.UserDAOJdbc;
import org.carladumit.digitaljournal.exceptions.*;
import org.carladumit.digitaljournal.model.JournalEntry;
import org.carladumit.digitaljournal.model.Rating;
import org.carladumit.digitaljournal.service.JournalService;
import org.carladumit.digitaljournal.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner sc;
    private final UserService userService;
    private final JournalService journalService;

    public ConsoleUI() {
        this.sc = new Scanner(System.in);
        UserDAO userDAO = new UserDAOJdbc();
        JournalEntryDAO journalDAO = new JournalEntryDAOJdbc();
        this.userService = new UserService(userDAO);
        this.journalService = new JournalService(journalDAO, userService);
    }

    public void start (){
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

    private int readOption() {
        while(true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introduce a valid option.");
            }
        }
    }

    private void promptLogin() {
        System.out.println("-------------------");
        System.out.println("      LOG IN       ");
        System.out.println("-------------------");

        String username = readUsername();
        String password = readPassword();

        try {
            userService.login(username, password);
            System.out.println("Log in successful.");
            journalMenu();
        } catch (UserNotFoundException e) {
            System.out.println("User not found.");
        } catch (InvalidPasswordException e) {
            System.out.println("Incorrect password.");
        } catch (DatabaseException e) {
            System.out.println("Database unavailable.");
        }
    }

    private void promptRegister(){
        System.out.println("-------------------");
        System.out.println("      SIGN UP      ");
        System.out.println("-------------------");

        String username = readUsername();
        String password = readPassword();
        System.out.print("Confirm ");
        String passwordConfirmation = readPassword();

        if (!password.equals(passwordConfirmation)) {
            System.out.println("Passwords do not match.");
            return;
        }

        try {
            userService.register(username, password);
            System.out.println("Registration successful.");
            journalMenu();
        } catch (UserAlreadyExistsException e) {
            System.out.println("Username already exists.");
        } catch (DatabaseException e) {
            System.out.println("Database unavailable.");
        }
    }

    private String readUsername() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim().toLowerCase();
        while (username.isEmpty()) {
            System.out.println("Please input username: ");
            username = sc.nextLine().trim();
        }
        return username;
    }

    private String readPassword() {
        System.out.print("Password: ");
        String password = sc.nextLine().trim();
        while (password.isEmpty()) {
            System.out.println("Please input password: ");
            password = sc.nextLine().trim();
        }
        return password;
    }

    private void journalMenu(){
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
                case 2 -> promptReadEntryByDate();
                case 3 -> promptDeleteEntry();
                case 0 -> {
                    userService.logout();
                    System.out.println("Closing your journal...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void promptJournalEntry(){
        System.out.println("-------------------");
        System.out.println("     NEW ENTRY     ");
        System.out.println("-------------------");

        LocalDate entryDate = LocalDate.now();
        String rating = promptRating();
        String text = promptText();

        try {
            journalService.createEntry(entryDate, rating, text);
            System.out.println("Entry saved successfully.");
        } catch (EntryAlreadyExistsException e) {
            System.out.println("You have already written today's entry.");
        } catch (DatabaseException e) {
            System.out.println("Database unavailable.");
        }
    }

    private String promptRating() {
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

    private String promptText() {
        System.out.print("Spill your thoughts...");
        return sc.nextLine();
    }

    private void promptReadEntryByDate() {
        System.out.println("-------------------");
        System.out.println("   YOUR ENTRIES    ");
        System.out.println("-------------------");

        LocalDate date = promptDate();

        try{
            JournalEntry entry = journalService.readEntriesByDate(date);
            System.out.println("----------------------------");
            System.out.println("Date: " + entry.getEntryDate());
            System.out.println("It was a " + entry.getRating() + " day.");
            System.out.println(entry.getText());

        } catch (EntryNotFoundException e) {
            System.out.println("No entry found for this date.");
        } catch (DatabaseException e) {
            System.out.println("Database unavailable.");
        }
    }

    private LocalDate promptDate(){
        while (true) {
            System.out.print("Date (yyyy-mm-dd): ");
            try {
                return LocalDate.parse(sc.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please try again.");
            }
        }
    }

    private void promptDeleteEntry(){
        System.out.println("-------------------");
        System.out.println("    DELETE ENTRY   ");
        System.out.println("-------------------");
        LocalDate date = promptDate();
        System.out.print("Are you sure? (Y/N): ");

        if (!sc.nextLine().trim().equalsIgnoreCase("Y")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        try {
            journalService.deleteEntry(date);
            System.out.println("Entry deleted successfully.");
        } catch (EntryNotFoundException e) {
            System.out.println("No entry found for this date.");
        } catch (DatabaseException e) {
            System.out.println("Database unavailable.");
        }
    }
}
