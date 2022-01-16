package ru.nelolik.studingspring.NotesService.db.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UsersDAO usersDAO;

    private UsersService usersService;

    private static long USER_ID = 1;
    private static User USER = new User(USER_ID, "User name");
    private static User USER2 = new User(USER_ID + 1, "User name2");
    private static User USER3 = new User(USER_ID + 2, "User name3");
    private static List<User> USER_LIST = Arrays.asList(USER, USER2, USER3);

    {
        usersDAO = mock(UsersDAO.class);
        usersService = new UserServiceImpl(usersDAO);
    }

    @Test
    public void getUserByIdTest() {
        when(usersDAO.user(USER_ID)).thenReturn(USER);
        User u = usersService.user(USER_ID);
        Assertions.assertEquals(USER, u, "Mocked user and returned user ane not equal.");
    }

    @Test
    public void insertNewUserTest() {
        usersService.insert(USER);
        verify(usersDAO, atLeastOnce()).insert(eq(USER));
    }

    @Test
    public void editUserTest() {
        usersService.edit(USER);
        verify(usersDAO, atLeastOnce()).edit(eq(USER));
    }

    @Test
    public void deleteUserTest() {
        usersService.delete(USER_ID);
        verify(usersDAO,atLeastOnce()).delete(eq(USER_ID));
    }

    @Test
    public void indexTest() {
        when(usersDAO.index()).thenReturn(USER_LIST);
        List<User> result = usersService.index();
        Assertions.assertEquals(USER_LIST, result, "Method index returned wrong result");
    }
}
