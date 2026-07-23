package org.carladumit.digitaljournal.service;

import org.carladumit.digitaljournal.dao.JournalEntryDAO;
import org.carladumit.digitaljournal.dao.impl.JournalEntryDAOJdbc;
import org.carladumit.digitaljournal.exceptions.EntryAlreadyExistsException;
import org.carladumit.digitaljournal.exceptions.EntryNotFoundException;
import org.carladumit.digitaljournal.model.JournalEntry;
import org.carladumit.digitaljournal.model.User;

import java.time.LocalDate;

public class JournalService {
    private static final JournalEntryDAO journalDAO = new JournalEntryDAOJdbc();
    private static User currentUser;

    public static void createEntry(LocalDate entryDate, String rating, String text) throws EntryAlreadyExistsException {
        currentUser = UserService.getCurrentUser();

        if(journalDAO.findEntryByUserAndDate(currentUser.getId(), entryDate)!=null)
            throw new EntryAlreadyExistsException();

        JournalEntry newEntry = new JournalEntry(currentUser.getId(), entryDate, rating, text);
        journalDAO.saveEntry(newEntry);
    }

    public static JournalEntry readEntriesByDate(LocalDate entryDate) throws EntryNotFoundException{
        currentUser = UserService.getCurrentUser();

        JournalEntry entry = journalDAO.findEntryByUserAndDate(currentUser.getId(), entryDate);
        if(entry == null)
            throw new EntryNotFoundException();

        return entry;
    }

    public static void deleteEntry(LocalDate date) throws EntryNotFoundException {
        currentUser = UserService.getCurrentUser();

        JournalEntry entry = journalDAO.findEntryByUserAndDate(currentUser.getId(), date);
        if (entry == null) {
            throw new EntryNotFoundException();
        }
        journalDAO.deleteEntryByUserAndDate(currentUser.getId(), date);
    }
}
