package org.carladumit.digitaljournal.dao.impl;

import org.carladumit.digitaljournal.dao.UserDAO;
import org.carladumit.digitaljournal.exceptions.DatabaseException;
import org.carladumit.digitaljournal.model.User;
import org.carladumit.digitaljournal.util.DBConnection;
import org.carladumit.digitaljournal.util.PasswordHash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOJdbc implements UserDAO {

    @Override
    public void registerUser(User user) {
        String hash = PasswordHash.createHash(user.getPassword());
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, hash);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Unable to register user.", e);
        }
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password FROM user WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Unable to find user.", e);
        }
        return null;
    }
}