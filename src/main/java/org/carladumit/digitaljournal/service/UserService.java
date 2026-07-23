package org.carladumit.digitaljournal.service;

import org.carladumit.digitaljournal.dao.UserDAO;
import org.carladumit.digitaljournal.exceptions.InvalidPasswordException;
import org.carladumit.digitaljournal.exceptions.UserAlreadyExistsException;
import org.carladumit.digitaljournal.exceptions.UserNotFoundException;
import org.carladumit.digitaljournal.model.User;
import org.carladumit.digitaljournal.util.PasswordHash;

public class UserService {

    private final UserDAO userDAO;
    private User currentUser;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String username, String password) throws UserNotFoundException, InvalidPasswordException {
        User user = userDAO.findByUsername(username);

        if (user == null)
            throw new UserNotFoundException();

        if (!PasswordHash.checkHash(password, user.getPassword())){
            throw new InvalidPasswordException();
        }
        this.currentUser = user;
        return user;
    }

    public void register(String username, String password) throws UserAlreadyExistsException {
        User newUser = userDAO.findByUsername(username);

        if(newUser != null)
            throw new UserAlreadyExistsException();

        userDAO.registerUser(new User(username, password));
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        this.currentUser = null;
    }
}
