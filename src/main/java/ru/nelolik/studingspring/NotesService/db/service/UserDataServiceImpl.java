package ru.nelolik.studingspring.NotesService.db.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.Role;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;
import ru.nelolik.studingspring.NotesService.dto.NoteDTO;
import ru.nelolik.studingspring.NotesService.dto.UserDTO;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserDataServiceImpl implements UserDataService {

    private final UsersDAO usersDao;

    private final NotesDAO notesDAO;


    @Override
    public List<UserDTO> getAllUsers() {
        List<User> allUsers = usersDao.getAllUsers();
        return allUsers.stream().map(UserDTO::new).toList();
    }

    @Override
    public UserDTO getUserById(long id) {
        User user = usersDao.getUserById(id);
        if (user == null) {
            return null;
        }
        return new UserDTO(user);
    }

    @Override
    public UserDTO getUserByName(String name) {
        User user = usersDao.getUserByName(name);
        if (user == null) {
            return null;
        }
        return new UserDTO(user);
    }

    @Override
    public User getUserByNameOrigin(String name) {
        return usersDao.getUserByName(name);
    }

    @Override
    public long insertUser(UserDTO userDTO) {
        User user = new User(userDTO.getId(), userDTO.getUsername(), "",
                Collections.singletonList(new UserRole(0L, Role.ROLE_USER.name())));
        return usersDao.insertUser(user);
    }

    @Override
    public long insertUserOrigin(User user) {
        return usersDao.insertUser(user);
    }

    @Override
    public void editUser(UserDTO userDTO) {
        User userFromDb = usersDao.getUserById(userDTO.getId());
        if (userFromDb == null) {
            log.error("User with id={} is not found and could not be edited.", userDTO.getId());
            return;
        }
        userFromDb.setUsername(userDTO.getUsername());
        usersDao.editUser(userFromDb);
    }

    @Override
    public void removeUserById(long id) {
        usersDao.deleteUserById(id);
    }


    @Override
    public List<NoteDTO> getAllNotes() {
        List<Note> notes = notesDAO.getAllNotes();
        return notes.stream().map(NoteDTO::new).toList();
    }

    @Override
    public List<NoteDTO> getNotesByUserId(long userId) {
        List<Note> notes = notesDAO.getNotesByUserId(userId);
        return notes.stream().map(NoteDTO::new).toList();
    }

    @Override
    public NoteDTO getNoteById(long noteId) {
        Note note = notesDAO.getOneNote(noteId);
        if (note == null) {
            return null;
        }
        return new NoteDTO(note);
    }

    @Override
    public long addNote(NoteDTO noteDTO) {
        Note note = new Note(noteDTO.getId(), noteDTO.getUserId(), noteDTO.getRecord());
        return notesDAO.addNote(note);
    }


    @Override
    public void removeNotesByUserId(long userId) {
        notesDAO.removeNotesByUserId(userId);
    }

    @Override
    public void removeNote(long id) {
        notesDAO.removeNoteByNoteId(id);
    }
}
