package org.carladumit.digitaljournal.dao;

import org.carladumit.digitaljournal.model.JournalEntry;

import java.sql.SQLException;
import java.time.LocalDate;

public interface JournalEntryDAO {
    void saveEntry(JournalEntry entry);
    JournalEntry findEntryByUserAndDate(int userID, LocalDate date);
    boolean deleteEntryByUserAndDate(int userID, LocalDate date);
}
