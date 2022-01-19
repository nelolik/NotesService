package ru.nelolik.studingspring.NotesService.db.dao;

import ru.nelolik.studingspring.NotesService.db.dataset.User;

import java.util.List;

public interface UsersDAO {
    List<User> getAllUsers();
    User getUserById(long id);
    long insertUser(User user);
    void editUser(User user);
    void deleteUserById(long id);
}
