package ru.nelolik.studingspring.notes_service.db.service;

import ru.nelolik.studingspring.notes_service.db.dataset.Note;

import java.util.List;

public interface NoteService {
    List<Note> getAllNotes();

    List<Note> getNotesByUserId(long userId);

    Note getNoteById(long noteId);

    long addNote(Note note);

    void removeNotesByUserId(long userId);

    void removeNote(long id, long userId);
}
