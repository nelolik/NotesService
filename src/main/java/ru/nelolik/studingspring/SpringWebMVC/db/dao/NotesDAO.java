package ru.nelolik.studingspring.SpringWebMVC.db.dao;

import ru.nelolik.studingspring.SpringWebMVC.db.dataset.Note;

import java.util.List;

public interface NotesDAO {
    List<String> getOneNote(long noteId);
    List<String> getAllNotes(long userId);
    long addNote(Note note);
    long removeNote(long noteId);
    void removeUserNotes(long userId);
}
