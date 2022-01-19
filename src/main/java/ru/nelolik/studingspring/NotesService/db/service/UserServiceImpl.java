package ru.nelolik.studingspring.NotesService.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

import java.util.List;


@Service
public class UserServiceImpl implements UsersService{

    private UsersDAO usersDao;

    @Autowired
    public UserServiceImpl(UsersDAO usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public List<User> getAllUsers() {
        return usersDao.getAllUsers();
    }

    @Override
    public User getUserById(long id) {
        return usersDao.getUserById(id);
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
}
