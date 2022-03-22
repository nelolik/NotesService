package ru.nelolik.studingspring.NotesService.db.service;

import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.dto.NoteDTO;
import ru.nelolik.studingspring.NotesService.dto.UserDTO;

import java.util.List;

public interface UserDataService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(long id);

    UserDTO getUserByName(String name);

    User getUserByNameOrigin(String name);

    long insertUser(UserDTO user);

    long insertUserOrigin(User user);

    void editUser(UserDTO user);

    void removeUserById(long id);

    List<NoteDTO> getAllNotes();

    List<NoteDTO> getNotesByUserId(long userId);

    NoteDTO getNoteById(long noteId);

    long addNote(NoteDTO note);

    void removeNotesByUserId(long userId);

    void removeNote(long id);
}
