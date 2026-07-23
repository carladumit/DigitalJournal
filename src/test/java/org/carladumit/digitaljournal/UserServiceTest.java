package org.carladumit.digitaljournal;

import org.carladumit.digitaljournal.dao.UserDAO;
import org.carladumit.digitaljournal.exceptions.InvalidPasswordException;
import org.carladumit.digitaljournal.exceptions.UserAlreadyExistsException;
import org.carladumit.digitaljournal.exceptions.UserNotFoundException;
import org.carladumit.digitaljournal.model.User;
import org.carladumit.digitaljournal.service.UserService;
import org.carladumit.digitaljournal.util.PasswordHash;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserDAO userDAO;

    @InjectMocks
    UserService service;

    @Test
    void loginSuccess() {

        String hash = PasswordHash.createHash("123");
        User user = new User(1,"carla",hash);

        when(userDAO.findByUsername("carla"))
                .thenReturn(user);

        User result = service.login("carla","123");

        assertNotNull(result);
        assertEquals("carla",result.getUsername());
    }

    @Test
    void loginUserNotFound() {

        when(userDAO.findByUsername("carla"))
                .thenReturn(null);

        assertThrows(
                UserNotFoundException.class,
                () -> service.login("carla","1234")
        );
    }

    @Test
    void loginWrongPassword() {

        User user = new User(
                1,
                "carla",
                PasswordHash.createHash("123")
        );

        when(userDAO.findByUsername("carla"))
                .thenReturn(user);

        assertThrows(
                InvalidPasswordException.class,
                () -> service.login("carla","aaaa")
        );
    }

    @Test
    void registerSuccess() {

        when(userDAO.findByUsername("carla"))
                .thenReturn(null);

        service.register("carla","123");

        verify(userDAO).registerUser(any(User.class));
    }

}