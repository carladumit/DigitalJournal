package org.example.digitaljournal.dao;

import org.example.digitaljournal.model.User;

import java.sql.SQLException;

public interface UserDAO {
    void registerUser (User user) throws SQLException;
    User findByUsername (String username) throws SQLException;
}
