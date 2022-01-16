package ru.nelolik.studingspring.NotesService.db.dao;

import ru.nelolik.studingspring.NotesService.db.dataset.User;

import java.util.List;

public interface UsersDAO {
    List<User> index();
    User user(long id);
    long insert(User user);
    void edit(User user);
    void delete(long id);
}
