package ru.nelolik.studingspring.notes_service.db.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import ru.nelolik.studingspring.notes_service.config.CacheNames;
import ru.nelolik.studingspring.notes_service.db.dao.NotesDAO;
import ru.nelolik.studingspring.notes_service.db.dao.UsersDAO;
import ru.nelolik.studingspring.notes_service.db.dataset.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersDAO usersDao;

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
}
