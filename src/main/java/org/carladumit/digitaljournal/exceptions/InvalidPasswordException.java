package org.carladumit.digitaljournal.exceptions;

public class InvalidPasswordException extends Throwable {
    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
