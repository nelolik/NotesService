package ru.nelolik.studingspring.NotesService.db.service;

import ru.nelolik.studingspring.NotesService.db.dataset.User;

import java.util.List;


public interface UsersService {
    List<User> getAllUsers();
    User getUserById(long id);
    long insertUser(User user);
    void editUser(User user);
    void removeUserById(long id);
}