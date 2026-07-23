package org.carladumit.digitaljournal;

import org.carladumit.digitaljournal.dao.JournalEntryDAO;
import org.carladumit.digitaljournal.exceptions.EntryAlreadyExistsException;
import org.carladumit.digitaljournal.exceptions.EntryNotFoundException;
import org.carladumit.digitaljournal.model.JournalEntry;
import org.carladumit.digitaljournal.model.User;
import org.carladumit.digitaljournal.service.JournalService;
import org.carladumit.digitaljournal.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JournalServiceTest {

    @Mock
    private JournalEntryDAO journalDAO;

    @Mock
    private UserService userService;

    @InjectMocks
    private JournalService journalService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("carla", "123");
    }

    @Test
    void createEntry_Success() throws Exception {
        LocalDate date = LocalDate.now();
        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(journalDAO.findEntryByUserAndDate(mockUser.getId(), date)).thenReturn(null);

        assertDoesNotThrow(() -> journalService.createEntry(date, "GOOD", "Nice day!"));

        verify(journalDAO, times(1)).saveEntry(any(JournalEntry.class));
    }

    @Test
    void createEntry_AlreadyExists_ThrowsException() {
        LocalDate date = LocalDate.now();
        JournalEntry existingEntry = new JournalEntry(mockUser.getId(), date, "GOOD", "Good");

        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(journalDAO.findEntryByUserAndDate(mockUser.getId(), date)).thenReturn(existingEntry);

        assertThrows(EntryAlreadyExistsException.class, () ->
                journalService.createEntry(date, "GREAT", "Duplicity")
        );

        verify(journalDAO, never()).saveEntry(any());
    }

    @Test
    void readEntriesByDate_NotFound_ThrowsException() {
        LocalDate date = LocalDate.now();
        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(journalDAO.findEntryByUserAndDate(mockUser.getId(), date)).thenReturn(null);

        assertThrows(EntryNotFoundException.class, () ->
                journalService.readEntriesByDate(date)
        );
    }
}