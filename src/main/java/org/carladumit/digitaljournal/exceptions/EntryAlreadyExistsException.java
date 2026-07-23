package org.carladumit.digitaljournal.exceptions;

public class EntryAlreadyExistsException extends Throwable {
    public EntryAlreadyExistsException() {
        super(message, cause);
    }
}
