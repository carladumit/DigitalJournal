package org.carladumit.digitaljournal.exceptions;

public class EntryNotFoundException extends RuntimeException {
    public EntryNotFoundException() {
        super("Entry not found.");
    }

}
