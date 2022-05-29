package ru.nelolik.studingspring.NotesService.db.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class UserDataServiceImpl implements UserDataService {

    private final UsersDAO usersDao;

    private final NotesDAO notesDAO;


    @Override
    @Cacheable("users")
    public List<User> getAllUsers() {
        log.debug("Running getAllUsers");
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
    @CacheEvict(value = "users", allEntries = true)
    public void editUser(User user) {
        usersDao.editUser(user);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
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
