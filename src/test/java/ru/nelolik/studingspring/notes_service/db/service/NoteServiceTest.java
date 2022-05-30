package ru.nelolik.studingspring.notes_service.db.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import ru.nelolik.studingspring.notes_service.config.CacheNames;
import ru.nelolik.studingspring.notes_service.config.TestConfig;
import ru.nelolik.studingspring.notes_service.db.dao.NotesDAO;
import ru.nelolik.studingspring.notes_service.db.dao.UsersDAO;
import ru.nelolik.studingspring.notes_service.db.dataset.Note;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class, UsersDAO.class, NotesDAO.class, NoteServiceImpl.class})
class NoteServiceTest {

    @Autowired
    NoteService service;

    @MockBean
    NotesDAO notesDAO;

    @Autowired
    CacheManager cacheManager;

    private static long USER_ID = 1;

    private static final long NOTE_ID = 1;
    private static final long USER_ID1 = 1;
    private static final long USER_ID2 = 2;
    private static final Note NOTE1 = new Note(NOTE_ID, USER_ID1, "Some text");
    private static final Note NOTE2 = new Note(NOTE_ID + 1, USER_ID1, "Some text");
    private static final Note NOTE3 = new Note(NOTE_ID + 2, USER_ID2, "Some text");
    private static final List<Note> NOTES = Arrays.asList(NOTE1, NOTE2, NOTE3);
    private static final List<Note> NOTES_USER1 = Arrays.asList(NOTE1, NOTE2);
    private static final List<Note> NOTES_USER2 = Arrays.asList(NOTE3);

    @BeforeEach
    void beforeEach() {
        Cache cache = cacheManager.getCache(CacheNames.NOTES);
        if (cache != null) {
            cache.clear();
        }
    }

    @Test
    void getAllNotesTest() {
        when(notesDAO.getAllNotes()).thenReturn(NOTES);
        List<Note> result = service.getAllNotes();
        assertThat(result).isNotNull().containsExactlyInAnyOrderElementsOf(NOTES);
    }

    @Test
    void getNotesByUserIdTest() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        List<Note> result = service.getNotesByUserId(USER_ID1);
        Assertions.assertEquals(NOTES_USER1, result);
    }

    @Test
    void getNotesByUserIdCachedTest() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID1);

        reset(notesDAO);
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, never()).getNotesByUserId(USER_ID1);
    }

    @Test
    void getNotesByUserIdCachedOnRightKey() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID1);

        reset(notesDAO);
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        when(notesDAO.getNotesByUserId(USER_ID2)).thenReturn(NOTES_USER2);
        service.getNotesByUserId(USER_ID2);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID2);
    }

    @Test
    void getNotesByUserIdCacheEvictedOnUserRemove() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID1);

        reset(notesDAO);
        service.removeNotesByUserId(USER_ID1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID1);
    }

    @Test
    void getNotesByUserIdCacheEvictedOnNoteRemove() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID1);

        reset(notesDAO);
        service.removeNote(NOTE_ID, USER_ID1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID1);
    }

    @Test
    void getNoteByIdTest() {
        when(notesDAO.getOneNote(NOTE_ID)).thenReturn(NOTE1);
        Note result = service.getNoteById(NOTE_ID);
        Assertions.assertEquals(NOTE1, result);
    }

    @Test
    void addNoteTest() {
        when(notesDAO.addNote(NOTE1)).thenReturn(NOTE_ID);
        long result = service.addNote(NOTE1);
        Assertions.assertEquals(NOTE_ID, result);
    }

    @Test
    void removeNotesByUserIdTest() {
        service.removeNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).removeNotesByUserId(eq(USER_ID1));
    }

    @Test
    void removeNote() {
        service.removeNote(NOTE_ID, USER_ID);
        verify(notesDAO, atLeastOnce()).removeNoteByNoteId(eq(NOTE_ID));
    }

}
