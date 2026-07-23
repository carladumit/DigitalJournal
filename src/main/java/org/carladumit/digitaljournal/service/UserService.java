package org.carladumit.digitaljournal.service;

import org.carladumit.digitaljournal.dao.impl.UserDAOJdbc;
import org.carladumit.digitaljournal.model.User;
import org.carladumit.digitaljournal.util.PasswordHash;

import java.sql.SQLException;

public class UserService {

    private static final UserDAOJdbc userDAO = new UserDAOJdbc();
    private static User currentUser;

    public static User login(String username, String password) throws SQLException {
        User user = userDAO.findByUsername(username);
        if (user != null) {
            if (PasswordHash.checkHash(password, user.getPassword())){
                System.out.println("Log in successful.");
                currentUser = user;
                return user;
            }else { System.out.println("Incorrect password");}
        }else { System.out.println("User not found."); }
        return null;
    }

    public static boolean register(String username, String password) throws SQLException {
        User user = userDAO.findByUsername(username);
        if (user == null){
            User newUser = new User(username, password);
            userDAO.registerUser(newUser);
            System.out.println("Registration successful. Welcome!");
            return true;
        }else { System.out.println("Username not available.");}
        return false;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
}
