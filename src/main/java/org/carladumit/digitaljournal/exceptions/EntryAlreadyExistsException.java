package org.carladumit.digitaljournal.exceptions;

public class EntryAlreadyExistsException extends RuntimeException {
    public EntryAlreadyExistsException() {
        super("Entry already exists.");
    }
}
