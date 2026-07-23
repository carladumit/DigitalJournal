package org.carladumit.digitaljournal.exceptions;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
