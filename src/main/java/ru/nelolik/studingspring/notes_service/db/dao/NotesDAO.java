package ru.nelolik.studingspring.notes_service.db.dao;

import ru.nelolik.studingspring.notes_service.db.dataset.Note;

import java.util.List;

public interface NotesDAO {

    List<Note> getAllNotes();

    Note getOneNote(long noteId);

    List<Note> getNotesByUserId(long userId);

    long addNote(Note note);

    void removeNoteByNoteId(long noteId);

    void removeNotesByUserId(long userId);
}
