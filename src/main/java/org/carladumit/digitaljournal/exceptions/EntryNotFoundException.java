package org.carladumit.digitaljournal.exceptions;

public class EntryNotFoundException extends RuntimeException {
    public EntryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
