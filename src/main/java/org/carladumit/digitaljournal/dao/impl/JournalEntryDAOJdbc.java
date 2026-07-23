package org.carladumit.digitaljournal.dao.impl;

import org.carladumit.digitaljournal.dao.JournalEntryDAO;
import org.carladumit.digitaljournal.exceptions.DatabaseException;
import org.carladumit.digitaljournal.model.JournalEntry;
import org.carladumit.digitaljournal.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;

public class JournalEntryDAOJdbc implements JournalEntryDAO {

    @Override
    public void saveEntry(JournalEntry entry) {
        String sql = "INSERT INTO journal_entry (user_id, entry_date, rating, text) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, entry.getUserID());
                ps.setDate(2, Date.valueOf(entry.getEntryDate()));
                ps.setString(3, entry.getRating());
                ps.setString(4, entry.getText());

                ps.executeUpdate();
        }catch(SQLException e) {
            throw new DatabaseException("Unable save entry.", e);
        }
    }

    @Override
    public JournalEntry findEntryByUserAndDate(int userID, LocalDate date) {
        String sql = """
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

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, userID);
                ps.setDate(2, Date.valueOf(date));

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        return new JournalEntry(
                                rs.getInt("user_id"),
                                rs.getDate("entry_date").toLocalDate(),
                                rs.getString("rating"),
                                rs.getString("text")
                        );
                    }
                return null;
                }
            }catch (SQLException e) {
            throw new DatabaseException("Unable to find journal entry.", e);
        }
    }

    @Override
    public boolean deleteEntryByUserAndDate(int userID, LocalDate date) {
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
        } catch (SQLException e) {
            throw new DatabaseException("Unable to delete entry.", e);
        }
    }
}