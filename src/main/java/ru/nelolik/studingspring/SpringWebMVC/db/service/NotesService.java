package ru.nelolik.studingspring.SpringWebMVC.db.service;

import ru.nelolik.studingspring.SpringWebMVC.db.dataset.Note;

import java.util.List;

public interface NotesService {
    List<Note> index();
    List<Note> getNotesByUserId(long userId);
    Note getNoteById(long noteId);
    long addNote(Note note);
    void removeUserNotes(long userId);
    void removeNote(long id);
}
