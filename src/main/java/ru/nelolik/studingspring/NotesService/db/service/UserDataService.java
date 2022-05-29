package ru.nelolik.studingspring.NotesService.db.service;

import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

import java.util.List;

public interface UserDataService {

    List<User> getAllUsers();

    User getUserById(long id);

    User getUserByName(String name);

    long insertUser(User user);

    void editUser(User user);

    void removeUserById(long id);

    List<Note> getAllNotes();

    List<Note> getNotesByUserId(long userId);

    Note getNoteById(long noteId);

    long addNote(Note note);

    void removeNotesByUserId(long userId);

    void removeNote(long id, long userId);
}
