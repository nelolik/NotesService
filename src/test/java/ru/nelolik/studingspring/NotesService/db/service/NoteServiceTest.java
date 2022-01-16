package ru.nelolik.studingspring.NotesService.db.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class NoteServiceTest {

    @Mock
    private NotesDAO notesDAO;

    private NotesService notesService;

    private static long NOTE_ID = 1;
    private static long USER_ID1 = 1;
    private static long USER_ID2 = 2;
    private static Note NOTE1 = new Note(NOTE_ID, USER_ID1, "Some text");
    private static Note NOTE2 = new Note(NOTE_ID + 1, USER_ID1, "Some text");
    private static Note NOTE3 = new Note(NOTE_ID + 2, USER_ID2, "Some text");
    private static List<Note> NOTES = Arrays.asList(NOTE1, NOTE2, NOTE3);
    private static List<Note> NOTES_USER1 = Arrays.asList(NOTE1, NOTE2);
    private static List<Note> NOTES_USER2 = Arrays.asList(NOTE3);

    {
        notesDAO = mock(NotesDAO.class);
        notesService = new NotesServiceImpl(notesDAO);
    }


    @Test
    public void indexTest() {
        when(notesDAO.getAllNotes()).thenReturn(NOTES);
        List<Note> result = notesService.index();
        Assertions.assertEquals(NOTES, result, "Method index returns wrong list of Notes");
    }

    @Test
    public void getNotesByUserIdTest() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        List<Note> result = notesService.getNotesByUserId(USER_ID1);
        Assertions.assertEquals(NOTES_USER1, result);
    }

    @Test
    public void getNoteByIdTest() {
        when(notesDAO.getOneNote(NOTE_ID)).thenReturn(NOTE1);
        Note result = notesService.getNoteById(NOTE_ID);
        Assertions.assertEquals(NOTE1, result);
    }

    @Test
    public void addNoteTest() {
        when(notesDAO.addNote(NOTE1)).thenReturn(NOTE_ID);
        long result = notesService.addNote(NOTE1);
        Assertions.assertEquals(NOTE_ID, result);
    }

    @Test
    public void removeUserNotesTest() {
        notesService.removeUserNotes(USER_ID1);
        verify(notesDAO, atLeastOnce()).removeUserNotes(eq(USER_ID1));
    }

    @Test
    public void removeNote() {
        notesService.removeNote(NOTE_ID);
        verify(notesDAO, atLeastOnce()).removeNote(eq(NOTE_ID));
    }
}
