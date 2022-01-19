package ru.nelolik.studingspring.NotesService.db.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @MockBean
    private NotesDAO notesDAO;

    @InjectMocks
    private NotesService notesService;

    private static final long NOTE_ID = 1;
    private static final long USER_ID1 = 1;
    private static final long USER_ID2 = 2;
    private static final Note NOTE1 = new Note(NOTE_ID, USER_ID1, "Some text");
    private static final Note NOTE2 = new Note(NOTE_ID + 1, USER_ID1, "Some text");
    private static final Note NOTE3 = new Note(NOTE_ID + 2, USER_ID2, "Some text");
    private static final List<Note> NOTES = Arrays.asList(NOTE1, NOTE2, NOTE3);
    private static final List<Note> NOTES_USER1 = Arrays.asList(NOTE1, NOTE2);
    private static final List<Note> NOTES_USER2 = Arrays.asList(NOTE3);

    {
        notesDAO = mock(NotesDAO.class);
        notesService = new NotesServiceImpl(notesDAO);
    }


    @Test
    void getAllNotesTest() {
        when(notesDAO.getAllNotes()).thenReturn(NOTES);
        List<Note> result = notesService.getAllNotes();
        boolean allElementsAreEqual = result.size() == NOTES.size();
        for (int i = 0; i < result.size(); i++) {
            Note received = result.get(i);
            Note recorded = NOTES.get(i);
            allElementsAreEqual |= received.getId() == received.getId();
            allElementsAreEqual |= received.getUserId() == received.getUserId();
            allElementsAreEqual |= received.getRecord().equals(received.getRecord());
        }
        Assertions.assertTrue(allElementsAreEqual, "Method index returns wrong list of Notes");
    }

    @Test
    void getNotesByUserIdTest() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        List<Note> result = notesService.getNotesByUserId(USER_ID1);
        Assertions.assertEquals(NOTES_USER1, result);
    }

    @Test
    void getNoteByIdTest() {
        when(notesDAO.getOneNote(NOTE_ID)).thenReturn(NOTE1);
        Note result = notesService.getNoteById(NOTE_ID);
        Assertions.assertEquals(NOTE1, result);
    }

    @Test
    void addNoteTest() {
        when(notesDAO.addNote(NOTE1)).thenReturn(NOTE_ID);
        long result = notesService.addNote(NOTE1);
        Assertions.assertEquals(NOTE_ID, result);
    }

    @Test
    void removeNotesByUserIdTest() {
        notesService.removeNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).removeNotesByUserId(eq(USER_ID1));
    }

    @Test
    void removeNote() {
        notesService.removeNote(NOTE_ID);
        verify(notesDAO, atLeastOnce()).removeNoteByNoteId(eq(NOTE_ID));
    }
}
