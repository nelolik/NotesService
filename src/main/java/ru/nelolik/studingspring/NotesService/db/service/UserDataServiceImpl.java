package ru.nelolik.studingspring.NotesService.db.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

import java.util.List;


@Service
@AllArgsConstructor
public class UserDataServiceImpl implements UserDataService {

    private UsersDAO usersDao;

    private NotesDAO notesDAO;


    @Override
    public List<User> getAllUsers() {
        return usersDao.getAllUsers();
    }

    @Override
    public User getUserById(long id) {
        return usersDao.getUserById(id);
    }

    @Override
    public User getUserByName(String name) {
        return usersDao.getUserByName(name);
    }

    @Override
    public long insertUser(User user) {
        return usersDao.insertUser(user);
    }

    @Override
    public void editUser(User user) {
        usersDao.editUser(user);
    }

    @Override
    public void removeUserById(long id) {
        usersDao.deleteUserById(id);
    }


    @Override
    public List<Note> getAllNotes() {
        return notesDAO.getAllNotes();
    }

    @Override
    public List<Note> getNotesByUserId(long userId) {
        return notesDAO.getNotesByUserId(userId);
    }

    @Override
    public Note getNoteById(long noteId) {
        return notesDAO.getOneNote(noteId);
    }

    @Override
    public long addNote(Note note) {
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
