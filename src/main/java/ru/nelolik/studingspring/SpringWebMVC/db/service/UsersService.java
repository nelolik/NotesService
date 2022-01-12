package ru.nelolik.studingspring.SpringWebMVC.db.service;

import ru.nelolik.studingspring.SpringWebMVC.db.dataset.User;

import java.util.List;


public interface UsersService {
    List<User> index();
    User user(long id);
}
