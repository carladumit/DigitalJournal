package org.carladumit.digitaljournal.exceptions;

public class EntryAlreadyExistsException extends Throwable {
    public EntryAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
