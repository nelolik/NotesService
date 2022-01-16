package ru.nelolik.studingspring.NotesService.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

import java.util.List;


@Component
public class UserServiceImpl implements UsersService{

    private UsersDAO usersDao;

    @Autowired
    public UserServiceImpl(UsersDAO usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public List<User> index() {
        return usersDao.index();
    }

    @Override
    public User user(long id) {
        return usersDao.user(id);
    }

    @Override
    public long insert(User user) {
        return usersDao.insert(user);
    }

    @Override
    public void edit(User user) {
        usersDao.edit(user);
    }

    @Override
    public void delete(long id) {
        usersDao.delete(id);
    }
}
