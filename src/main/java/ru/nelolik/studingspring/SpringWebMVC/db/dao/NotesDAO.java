package ru.nelolik.studingspring.SpringWebMVC.db.dao;

import ru.nelolik.studingspring.SpringWebMVC.db.dataset.Note;

import java.util.List;

public interface NotesDAO {
    List<Note> getAllNotes();
    Note getOneNote(long noteId);
    List<Note> getNotesByUserId(long userId);
    long addNote(Note note);
    void removeNote(long noteId);
    void removeUserNotes(long userId);
}
