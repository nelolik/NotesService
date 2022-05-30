package ru.nelolik.studingspring.notes_service.db.dao;

import ru.nelolik.studingspring.notes_service.db.dataset.User;

import java.util.List;

public interface UsersDAO {
    List<User> getAllUsers();
    User getUserById(long id);
    User getUserByName(String name);
    long insertUser(User user);
    void editUser(User user);
    void deleteUserById(long id);
}
