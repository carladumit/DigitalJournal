package org.carladumit.digitaljournal.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Incorrect password.");
    }
}
