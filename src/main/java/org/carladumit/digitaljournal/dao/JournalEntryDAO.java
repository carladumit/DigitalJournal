package org.carladumit.digitaljournal.dao;

import org.carladumit.digitaljournal.model.JournalEntry;

import java.time.LocalDate;
import java.util.List;

public interface JournalEntryDAO {
    void saveEntry(JournalEntry entry);
    JournalEntry findEntryByUserAndDate(int userID, LocalDate date);
    List<JournalEntry> findAllEntriesByUser(int userID);
    List<JournalEntry> findEntriesByUserAndMonthAndDay(int userID, int month, int day);
    boolean deleteEntryByUserAndDate(int userID, LocalDate date);
}
