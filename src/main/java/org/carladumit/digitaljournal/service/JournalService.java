package org.carladumit.digitaljournal.service;

import org.carladumit.digitaljournal.dao.impl.JournalEntryDAOJdbc;
import org.carladumit.digitaljournal.exceptions.EntryAlreadyExistsException;
import org.carladumit.digitaljournal.model.JournalEntry;
import org.carladumit.digitaljournal.model.User;

import java.sql.SQLException;
import java.time.LocalDate;

public class JournalService {
    private static final JournalEntryDAOJdbc journalDAO = new JournalEntryDAOJdbc();
    private static final User currentUser = UserService.getCurrentUser();

    public static void createEntry(LocalDate entryDate, String rating, String text) throws EntryAlreadyExistsException {
        JournalEntry newEntry = new JournalEntry(currentUser.getId(), entryDate, rating, text);
        try  {
            journalDAO.saveEntry(newEntry);
        }catch(SQLException e){
            throw new EntryAlreadyExistsException("This username already exists.", e);
        }
    }

    public static JournalEntry readEntriesByDate(LocalDate entryDate) {
        return journalDAO.findEntryByUserAndDate(currentUser.getId(), entryDate);
    }

    public static void deleteEntry(LocalDate date) throws SQLException {
        JournalEntry entry = journalDAO.findEntryByUserAndDate(currentUser.getId(), date);
        if (entry == null) {
            System.out.println("No entry found.");
            return;
        }
        journalDAO.deleteEntryByUserAndDate(currentUser.getId(), date);
        System.out.println("Entry deleted successfully.");
    }

}
