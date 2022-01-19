package ru.nelolik.studingspring.NotesService.db.service;

import ru.nelolik.studingspring.NotesService.db.dataset.Note;

import java.util.List;

public interface NotesService {

    List<Note> getAllNotes();

    List<Note> getNotesByUserId(long userId);

    Note getNoteById(long noteId);

    long addNote(Note note);

    void removeNotesByUserId(long userId);

    void removeNote(long id);
}
