package org.example.digitaljournal.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:sqlite:database/digitaljournal.db";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void initSchema() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             InputStream in = DBConnection.class.getClassLoader()
                     .getResourceAsStream("sql/schema.sql")) {

            Files.createDirectories(Paths.get("data"));
            String schema = new String(in.readAllBytes());
            for (String sentence : schema.split(";")) {
                if (!sentence.isBlank()) {
                    st.execute(sentence);
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Error initializing schema.", e);
        }
    }
}
