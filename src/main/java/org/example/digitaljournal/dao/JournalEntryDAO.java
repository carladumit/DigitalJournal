package org.example.digitaljournal.dao;

import org.example.digitaljournal.model.JournalEntry;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface JournalEntryDAO {
    void saveEntry(JournalEntry entry) throws SQLException;
    List<JournalEntry> findByUserAndDay(int userID, LocalDate date) throws SQLException;
    boolean deleteByUserAndDate(int userID, LocalDate date) throws SQLException;
}
