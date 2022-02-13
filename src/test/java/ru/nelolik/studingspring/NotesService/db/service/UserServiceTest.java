package ru.nelolik.studingspring.NotesService.db.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.Role;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UsersDAO usersDAO;

    private UsersService usersService;

    private static long USER_ID = 1;
    private static User USER = new User(USER_ID, "User name", "p1",
            Collections.singletonList(new UserRole(USER_ID, "ROLE_USER")));
    private static User USER2 = new User(USER_ID + 1, "User name2", "p2",
            Collections.singletonList(new UserRole(USER_ID + 1, "ROLE_USER")));
    private static User USER3 = new User(USER_ID + 2, "User name3", "p3",
            Collections.singletonList(new UserRole(USER_ID + 2, "ROLE_USER")));
    private static List<User> USER_LIST = Arrays.asList(USER, USER2, USER3);

    {
        usersDAO = mock(UsersDAO.class);
        usersService = new UserServiceImpl(usersDAO);
    }

    @Test
    void getUserByIdTest() {
        when(usersDAO.getUserById(USER_ID)).thenReturn(USER);
        User u = usersService.getUserById(USER_ID);
        Assertions.assertEquals(USER, u, "Mocked user and returned user ane not equal.");
    }

    @Test
    void insertUserTest() {
        usersService.insertUser(USER);
        verify(usersDAO, atLeastOnce()).insertUser(eq(USER));
    }

    @Test
    void editUserTest() {
        usersService.editUser(USER);
        verify(usersDAO, atLeastOnce()).editUser(eq(USER));
    }

    @Test
    void removeUserByIdTest() {
        usersService.removeUserById(USER_ID);
        verify(usersDAO,atLeastOnce()).deleteUserById(eq(USER_ID));
    }

    @Test
    void getAllUsersTest() {
        when(usersDAO.getAllUsers()).thenReturn(USER_LIST);
        List<User> result = usersService.getAllUsers();
        Assertions.assertEquals(USER_LIST, result, "Method index returned wrong result");
    }
}
