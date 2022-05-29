package ru.nelolik.studingspring.NotesService.db.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import ru.nelolik.studingspring.NotesService.config.CacheNames;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

import java.util.List;


@Service
@AllArgsConstructor
public class UserDataServiceImpl implements UserDataService {

    private final UsersDAO usersDao;

    private final NotesDAO notesDAO;


    @Override
    @Cacheable(CacheNames.ALL_USERS)
    public List<User> getAllUsers() {
        return usersDao.getAllUsers();
    }

    @Override
    @Cacheable(value = CacheNames.USER, key = "#id")
    public User getUserById(long id) {
        return usersDao.getUserById(id);
    }

    @Override
    public User getUserByName(String name) {
        return usersDao.getUserByName(name);
    }

    @Override
    @CacheEvict(value = CacheNames.ALL_USERS, allEntries = true)
    public long insertUser(User user) {
        return usersDao.insertUser(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheNames.ALL_USERS, allEntries = true),
            @CacheEvict(value = CacheNames.USER, key = "#user.getId()")
    })
    public void editUser(User user) {
        usersDao.editUser(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheNames.ALL_USERS, allEntries = true),
            @CacheEvict(value = CacheNames.USER, key = "#id")
    })
    public void removeUserById(long id) {
        usersDao.deleteUserById(id);
    }


    @Override
    public List<Note> getAllNotes() {
        return notesDAO.getAllNotes();
    }

    @Override
    @Cacheable(value = CacheNames.NOTES, key = "#userId")
    public List<Note> getNotesByUserId(long userId) {
        return notesDAO.getNotesByUserId(userId);
    }

    @Override
    public Note getNoteById(long noteId) {
        return notesDAO.getOneNote(noteId);
    }

    @Override
    @CacheEvict(value = CacheNames.NOTES, key = "#note.getUserId()")
    public long addNote(Note note) {
        return notesDAO.addNote(note);
    }


    @Override
    @CacheEvict(value = CacheNames.NOTES, key = "#userId")
    public void removeNotesByUserId(long userId) {
        notesDAO.removeNotesByUserId(userId);
    }

    @Override
    @CacheEvict(value = CacheNames.NOTES, key = "#userId")
    public void removeNote(long id, long userId) {
        notesDAO.removeNoteByNoteId(id);
    }
}
