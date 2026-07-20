package org.example.digitaljournal.model;

import java.util.Date;

public class JournalEntry {

    int id;
    int user_id;
    Date entry_date;
    Rating rating;
    String text;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(Date entry_date) {
        this.entry_date = entry_date;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
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
                ", user_id=" + user_id +
                ", entry_date=" + entry_date +
                ", rating=" + rating +
                ", text='" + text + '\'' +
                '}';
    }
}
