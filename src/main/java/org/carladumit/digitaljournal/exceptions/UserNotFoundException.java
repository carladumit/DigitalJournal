package org.carladumit.digitaljournal.exceptions;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException() {
        super(message, cause);
    }
}
