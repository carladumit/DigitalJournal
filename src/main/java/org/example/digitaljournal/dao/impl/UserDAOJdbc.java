package org.example.digitaljournal.dao.impl;

import org.example.digitaljournal.dao.UserDAO;
import org.example.digitaljournal.model.User;
import org.example.digitaljournal.util.DBConnection;
import org.example.digitaljournal.util.PasswordHash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOJdbc implements UserDAO {

    @Override
    public void registerUser(User user) throws SQLException {
        String hash = PasswordHash.createHash(user.getPassword());
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, hash);

            ps.executeUpdate();
        }
    }

    @Override
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT username, password FROM user WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new User(
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
            }
        }
        return null;
    }
}