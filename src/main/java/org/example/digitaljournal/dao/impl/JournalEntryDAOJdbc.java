package org.example.digitaljournal.dao.impl;

import org.example.digitaljournal.dao.JournalEntryDAO;
import org.example.digitaljournal.model.JournalEntry;
import org.example.digitaljournal.model.Rating;
import org.example.digitaljournal.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JournalEntryDAOJdbc implements JournalEntryDAO {

    @Override
    public void saveEntry(JournalEntry entry) throws SQLException {
        String sql = "INSERT INTO journal_entry (user_id, entry_date, rating, text) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entry.getUserID());
            ps.setDate(2, Date.valueOf(entry.getEntryDate()));
            ps.setString(3, entry.getRating().name());
            ps.setString(4, entry.getText());

            ps.executeUpdate();
        }
    }

    @Override
    public List<JournalEntry> findByUserAndDay(int userID, LocalDate date) throws SQLException {
        List<JournalEntry> entries = new ArrayList<>();

        String sql ="""
                SELECT id,
                       user_id,
                       entry_date,
                       rating,
                       text
                FROM journal_entry
                WHERE user_id = ?
                  AND entry_date = ?
                ORDER BY entry_date
                """;

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    JournalEntry entry = new JournalEntry(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getDate("entry_date").toLocalDate(),
                            Rating.valueOf(rs.getString("rating")),
                            rs.getString("text")
                    );
                    entries.add(entry);
                }
            }
        }

        return entries;
    }

    @Override
    public boolean deleteByUserAndDate(int userID, LocalDate date) throws SQLException {
        String sql = """
                DELETE FROM journal_entry
                WHERE user_id = ?
                  AND entry_date = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ps.setDate(2, Date.valueOf(date));

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        }
    }
}
