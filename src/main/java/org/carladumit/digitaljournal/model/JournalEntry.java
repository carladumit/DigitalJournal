package org.carladumit.digitaljournal.model;

import java.time.LocalDate;

public class JournalEntry {

    int id;
    int userID;
    LocalDate entryDate;
    String rating;
    String text;

    public JournalEntry(int id, int userID, LocalDate entryDate, String rating, String text) {
        this.id = id;
        this.userID = userID;
        this.entryDate = entryDate;
        this.rating = rating;
        this.text = text;
    }

    public JournalEntry(int userID, LocalDate entryDate, String rating, String text) {
        this.userID = userID;
        this.entryDate = entryDate;
        this.rating = rating;
        this.text = text;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "JournalEntry{" +
                "id=" + id +
                ", user id=" + userID +
                ", entry date=" + entryDate +
                ", rating=" + rating +
                ", text='" + text + '\'' +
                '}';
    }
}
