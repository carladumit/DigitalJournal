package org.carladumit.digitaljournal.service;

import org.carladumit.digitaljournal.dao.UserDAO;
import org.carladumit.digitaljournal.dao.impl.UserDAOJdbc;
import org.carladumit.digitaljournal.exceptions.InvalidPasswordException;
import org.carladumit.digitaljournal.exceptions.UserAlreadyExistsException;
import org.carladumit.digitaljournal.exceptions.UserNotFoundException;
import org.carladumit.digitaljournal.model.User;
import org.carladumit.digitaljournal.util.PasswordHash;

public class UserService {

    private static final UserDAO userDAO = new UserDAOJdbc();
    private static User currentUser;

    public static User login(String username, String password) throws UserNotFoundException, InvalidPasswordException {
        User user = userDAO.findByUsername(username);

        if (user == null)
            throw new UserNotFoundException();

        if (!PasswordHash.checkHash(password, user.getPassword())){
            throw new InvalidPasswordException();
        }
        currentUser = user;
        return user;
    }

    public static void register(String username, String password) throws UserAlreadyExistsException {
        User user = userDAO.findByUsername(username);

        if(userDAO.findByUsername(username)!=null)
            throw new UserAlreadyExistsException();

        userDAO.registerUser(new User(username, password));
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
}
