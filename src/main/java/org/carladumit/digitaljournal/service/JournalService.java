package org.carladumit.digitaljournal.service;

import org.carladumit.digitaljournal.dao.JournalEntryDAO;
import org.carladumit.digitaljournal.exceptions.EntryAlreadyExistsException;
import org.carladumit.digitaljournal.exceptions.EntryNotFoundException;
import org.carladumit.digitaljournal.model.JournalEntry;
import org.carladumit.digitaljournal.model.User;

import java.time.LocalDate;

public class JournalService {

    private final JournalEntryDAO journalDAO;
    private final UserService userService;

    public JournalService(JournalEntryDAO journalDAO, UserService userService) {
        this.journalDAO = journalDAO;
        this.userService = userService;
    }

    public void createEntry(LocalDate entryDate, String rating, String text) throws EntryAlreadyExistsException {
        User currentUser = userService.getCurrentUser();

        if(journalDAO.findEntryByUserAndDate(currentUser.getId(), entryDate)!=null)
            throw new EntryAlreadyExistsException();

        JournalEntry newEntry = new JournalEntry(currentUser.getId(), entryDate, rating, text);
        journalDAO.saveEntry(newEntry);
    }

    public JournalEntry readEntriesByDate(LocalDate entryDate) throws EntryNotFoundException{
        User currentUser = userService.getCurrentUser();

        JournalEntry entry = journalDAO.findEntryByUserAndDate(currentUser.getId(), entryDate);
        if(entry == null)
            throw new EntryNotFoundException();

        return entry;
    }

    public void deleteEntry(LocalDate date) throws EntryNotFoundException {
        User currentUser = userService.getCurrentUser();

        JournalEntry entry = journalDAO.findEntryByUserAndDate(currentUser.getId(), date);
        if (entry == null) {
            throw new EntryNotFoundException();
        }
        journalDAO.deleteEntryByUserAndDate(currentUser.getId(), date);
    }
}
