package org.carladumit.digitaljournal.util;

import org.carladumit.digitaljournal.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:sqlite:database/digitaljournal.db";

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            try (Statement st = conn.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON;");
            }
            return conn;
        } catch (SQLException e) {
            throw new DatabaseException("Unable to connect to the database.", e);
        }
    }
}