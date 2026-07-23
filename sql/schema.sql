PRAGMA foreign_keys = ON;

-- DROP TABLE user;
-- DROP TABLE journal_entry;
-- DELETE FROM user;
DELETE FROM sqlite_sequence
WHERE name = 'user';

CREATE TABLE IF NOT EXISTS user (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    username      VARCHAR(30) NOT NULL UNIQUE,
    password      VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS journal_entry (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id    INTEGER NOT NULL,
    entry_date DATE NOT NULL,
    rating     VARCHAR(10) NOT NULL,
    text       TEXT,

    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,

    UNIQUE (user_id, entry_date)
);

CREATE INDEX IF NOT EXISTS idx_entries_day_month
    ON journal_entry (substr(entry_date, 6, 5));
