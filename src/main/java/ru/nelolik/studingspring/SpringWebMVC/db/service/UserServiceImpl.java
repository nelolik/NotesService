package ru.nelolik.studingspring.SpringWebMVC.db.service;

import org.springframework.stereotype.Component;
import ru.nelolik.studingspring.SpringWebMVC.db.dao.UsersDao;
import ru.nelolik.studingspring.SpringWebMVC.db.dataset.User;

import java.util.List;


@Component
public class UserServiceImpl implements UsersService{

    private UsersDao usersDao;

    public UserServiceImpl(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public List<User> index() {
        return null;
    }

    @Override
    public User user(long id) {
        return null;
    }
}
