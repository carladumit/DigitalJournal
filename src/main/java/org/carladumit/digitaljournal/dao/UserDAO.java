package org.carladumit.digitaljournal.dao;

import org.carladumit.digitaljournal.model.User;

import java.sql.SQLException;

public interface UserDAO {
    void registerUser (User user);
    User findByUsername (String username);
}
