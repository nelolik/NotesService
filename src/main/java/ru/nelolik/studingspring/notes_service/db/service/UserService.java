package ru.nelolik.studingspring.notes_service.db.service;

import ru.nelolik.studingspring.notes_service.db.dataset.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(long id);

    User getUserByName(String name);

    long insertUser(User user);

    void editUser(User user);

    void removeUserById(long id);
}
