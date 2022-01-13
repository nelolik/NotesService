package ru.nelolik.studingspring.SpringWebMVC.db.service;

import org.apache.catalina.LifecycleState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ru.nelolik.studingspring.SpringWebMVC.config.TestConfig;
import ru.nelolik.studingspring.SpringWebMVC.db.dataset.Note;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
public class NoteServiceTest {

    @Autowired
    private NotesService notesService;

    private static List<Note> notes;

    @BeforeAll
    public static void setup() {
        notes = new ArrayList<>();
        notes.add(new Note(1L, 1L, "Note1"));
        notes.add(new Note(2L, 1L, "Note2"));
        notes.add(new Note(3L, 1L, "Note3"));
        notes.add(new Note(4L, 2L, "Note4"));
        notes.add(new Note(5L, 3L, "Note5"));
        notes.add(new Note(6L, 3L, "Note6"));
    }

    @Test
    public void  addNoteTest() {
        clearDb();
        long lastId = 0;
        for (Note n :
                notes) {
            notesService.addNote(n);
            lastId++;
        }
        Assertions.assertEquals(notes.size(), lastId,
                "Count of inserted values is not equal to users size");
        clearDb();
    }

    @Test
    public void removeTest() {
        insertNotes();
        Note noteFromDb = notesService.index().get(1);
        notesService.removeNote(noteFromDb.getId());
        List<Note> restNotes = notesService.index();
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
    public void getNoteByIdTest() {
//        clearDb();
        insertNotes();
        Note firstNote = notesService.getNoteById(1L);
        Assertions.assertNotNull(firstNote, "Could not get Note by id 1");
    }

    @Test
    public void indexTest() {
        clearDb();
        insertNotes();
        List<Note> notesFromDb = notesService.index();
        Assertions.assertTrue(notes.equals(notesFromDb), "Recorded and returned list are not equals.");
    }

    @Test
    public void getAllByUserIdTest() {
        clearDb();
        insertNotes();
        List<Note> notesOfUser1 = notesService.getNotesByUserId(1L);
        List<Note> notesOfUser2 = notesService.getNotesByUserId(2L);
        List<Note> notesOfUser100 = notesService.getNotesByUserId(100L);
        Assertions.assertEquals(3, notesOfUser1.size(), "Count of notes of user with id=1 does not match.");
        Assertions.assertEquals(1, notesOfUser2.size(), "Count of notes of user with id=2 does not match.");
        Assertions.assertEquals(0, notesOfUser100.size(), "Count of notes of user with id=100 should be 0.");
        clearDb();
    }

    @Test
    public void removeUserNotesTest() {
        insertNotes();
        List<Note> user1Notes = notesService.getNotesByUserId(1);
        Assertions.assertTrue(user1Notes.size() > 0, "Db doesn`t contains notes for userId = 1.");
        notesService.removeUserNotes(1);
        List<Note> removedNotes = notesService.getNotesByUserId(1);
        Assertions.assertEquals(0, removedNotes.size(), "Notes od user with userId = 1 was not removed.");
        clearDb();
    }

    private void insertNotes() {
        for (Note n :
                notes) {
            notesService.addNote(n);
        }
    }
    
    private void clearDb() {
        List<Note> notes = notesService.index();
        for (Note n :
                notes) {
            notesService.removeNote(n.getId());
        }
    }
}
