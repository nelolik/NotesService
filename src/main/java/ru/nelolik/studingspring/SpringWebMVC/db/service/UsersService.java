package ru.nelolik.studingspring.SpringWebMVC.db.service;

import ru.nelolik.studingspring.SpringWebMVC.db.dataset.User;

import java.util.List;


public interface UsersService {
    List<User> index();
    User user(long id);
    long insert(User user);
    void edit(User user);
    void delete(long id);
}
