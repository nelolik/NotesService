package ru.nelolik.studingspring.notes_service.db.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ru.nelolik.studingspring.notes_service.config.TestConfig;
import ru.nelolik.studingspring.notes_service.db.dataset.Note;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
class NoteDaoTest {

    @Autowired
    private NotesDAO notesDAO;

    private static List<Note> notes;

    @BeforeAll
    static void setup() {
        notes = new ArrayList<>();
        notes.add(new Note(1L, 1L, "Note1"));
        notes.add(new Note(2L, 1L, "Note2"));
        notes.add(new Note(3L, 1L, "Note3"));
        notes.add(new Note(4L, 2L, "Note4"));
        notes.add(new Note(5L, 3L, "Note5"));
        notes.add(new Note(6L, 3L, "Note6"));
    }

    @Test
    void  addNoteTest() {
        clearDb();
        long lastId = 0;
        for (Note n :
                notes) {
            notesDAO.addNote(n);
            lastId++;
        }
        Assertions.assertEquals(notes.size(), lastId,
                "Count of inserted values is not equal to users size");
        clearDb();
    }

    @Test
    void removeTest() {
        insertNotes();
        Note noteFromDb = notesDAO.getAllNotes().get(1);
        notesDAO.removeNoteByNoteId(noteFromDb.getId());
        List<Note> restNotes = notesDAO.getAllNotes();
        boolean removed = true;
        for (Note n :
                restNotes) {
            if (n.equals(noteFromDb)) {
                removed = false;
                break;}
        }

        Assertions.assertTrue(removed, "Record was not removed.");
        clearDb();
    }

    @Test
    void getNoteByIdTest() {
        insertNotes();
        Note firstNote = notesDAO.getOneNote(1L);
        Assertions.assertNotNull(firstNote, "Could not get Note by id 1");
    }

    @Test
    void getNoteWithNotExistingIdTest() {
        insertNotes();
        List<Note> notes = notesDAO.getAllNotes();
        long maxId = notes.stream().map(Note::getId).max(Long::compareTo).orElse(100L);
        Note notExistingNote = notesDAO.getOneNote(maxId + 10);
        Assertions.assertNull(notExistingNote, "Db returned note with not existing id");
    }

    @Test
    void getNoteWithNotExistingUserIdTest() {
        insertNotes();
        List<Note> notes = notesDAO.getAllNotes();
        long maxUserId = notes.stream().map(Note::getUserId).max(Long::compareTo).orElse(100L);
        List<Note> notExistingNotes = notesDAO.getNotesByUserId(maxUserId + 10L);
        Assertions.assertEquals(notExistingNotes.size(), 0, "Db returned note with not existing userId");
    }

    @Test
    void indexTest() {
        clearDb();
        insertNotes();
        List<Note> notesFromDb = notesDAO.getAllNotes();
        assertThat(notesFromDb).isNotNull()
                .containsExactlyInAnyOrderElementsOf(notes);
    }

    @Test
    void getAllByUserIdTest() {
        clearDb();
        insertNotes();
        List<Note> notesOfUser1 = notesDAO.getNotesByUserId(1L);
        List<Note> notesOfUser2 = notesDAO.getNotesByUserId(2L);
        List<Note> notesOfUser100 = notesDAO.getNotesByUserId(100L);
        Assertions.assertEquals(3, notesOfUser1.size(), "Count of notes of user with id=1 does not match.");
        Assertions.assertEquals(1, notesOfUser2.size(), "Count of notes of user with id=2 does not match.");
        Assertions.assertEquals(0, notesOfUser100.size(), "Count of notes of user with id=100 should be 0.");
        clearDb();
    }

    @Test
    void removeUserNotesTest() {
        insertNotes();
        List<Note> user1Notes = notesDAO.getNotesByUserId(1);
        Assertions.assertTrue(user1Notes.size() > 0, "Db doesn`t contains notes for userId = 1.");
        notesDAO.removeNotesByUserId(1);
        List<Note> removedNotes = notesDAO.getNotesByUserId(1);
        Assertions.assertEquals(0, removedNotes.size(), "Notes od user with userId = 1 was not removed.");
        clearDb();
    }

    private void insertNotes() {
        for (Note n :
                notes) {
            notesDAO.addNote(n);
        }
    }
    
    private void clearDb() {
        List<Note> notes = notesDAO.getAllNotes();
        for (Note n :
                notes) {
            notesDAO.removeNoteByNoteId(n.getId());
        }
    }
}
