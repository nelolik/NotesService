package ru.nelolik.studingspring.NotesService.db.dao;

import ru.nelolik.studingspring.NotesService.db.dataset.Note;

import java.util.List;

public interface NotesDAO {

    List<Note> getAllNotes();

    Note getOneNote(long noteId);

    List<Note> getNotesByUserId(long userId);

    long addNote(Note note);

    void removeNoteByNoteId(long noteId);

    void removeNotesByUserId(long userId);
}
