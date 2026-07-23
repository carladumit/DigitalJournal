package org.carladumit.digitaljournal.model;

public enum Rating {
    SAD, BAD, NEUTRAL, GOOD, GREAT;

    public String toEmoji() {
        return switch (this) {
            case SAD -> "😭";
            case BAD -> "🙁";
            case NEUTRAL -> "😐";
            case GOOD -> "🙂";
            case GREAT -> "😄";
        };
    }
}
