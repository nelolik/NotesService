package ru.nelolik.studingspring.notes_service.db.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import ru.nelolik.studingspring.notes_service.config.CacheNames;
import ru.nelolik.studingspring.notes_service.config.TestConfig;
import ru.nelolik.studingspring.notes_service.db.dao.NotesDAO;
import ru.nelolik.studingspring.notes_service.db.dao.UsersDAO;
import ru.nelolik.studingspring.notes_service.db.dataset.Role;
import ru.nelolik.studingspring.notes_service.db.dataset.User;
import ru.nelolik.studingspring.notes_service.db.dataset.UserRole;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class, UsersDAO.class, NotesDAO.class, UserServiceImpl.class})
class UserServiceTest {

    @Autowired
    UserService service;

    @MockBean
    UsersDAO usersDAO;

    @Autowired
    CacheManager cacheManager;

    private static long USER_ID = 1;
    private static final User USER = new User(USER_ID, "User name", "p1",
            Collections.singletonList(new UserRole(USER_ID, Role.ROLE_USER.name())));
    private static final User USER2 = new User(USER_ID + 1, "User name2", "p2",
            Collections.singletonList(new UserRole(USER_ID + 1, Role.ROLE_USER.name())));
    private static final User USER3 = new User(USER_ID + 2, "User name3", "p3",
            Collections.singletonList(new UserRole(USER_ID + 2, Role.ROLE_USER.name())));
    private static final List<User> USER_LIST = Arrays.asList(USER, USER2, USER3);


    @BeforeEach
    void beforeEach() {
        Cache cache = cacheManager.getCache(CacheNames.ALL_USERS);
        if (cache != null) {
            cache.clear();
        }
        cache = cacheManager.getCache(CacheNames.USER);
        if (cache != null) {
            cache.clear();
        }
    }

    @Test
    void getUserByIdTest() {
        when(usersDAO.getUserById(USER_ID)).thenReturn(USER);
        User u = service.getUserById(USER_ID);
        Assertions.assertEquals(USER, u, "Mocked user and returned user ane not equal.");
    }

    @Test
    void getUserByIdIsCached() {
        when(usersDAO.getUserById(USER_ID)).thenReturn(USER);
        service.getUserById(USER_ID);
        verify(usersDAO, atLeastOnce()).getUserById(USER_ID);

        reset(usersDAO);
        when(usersDAO.getUserById(USER_ID)).thenReturn(USER);
        service.getUserById(USER_ID);
        verify(usersDAO, never()).getUserById(USER_ID);
    }

    @Test
    void getUserByIdCacheIsEvictedOnEditUser() {
        when(usersDAO.getUserById(USER_ID)).thenReturn(USER);
        service.getUserById(USER_ID);
        verify(usersDAO, atLeastOnce()).getUserById(USER_ID);

        reset(usersDAO);
        when(usersDAO.getUserById(USER_ID)).thenReturn(USER);
        service.editUser(USER);
        service.getUserById(USER_ID);
        verify(usersDAO, atLeastOnce()).getUserById(USER_ID);
    }

    @Test
    void getUserByIdCacheIsEvictedOnRemoveUser() {
        when(usersDAO.getUserById(USER_ID)).thenReturn(USER);
        service.getUserById(USER_ID);
        verify(usersDAO, atLeastOnce()).getUserById(USER_ID);

        reset(usersDAO);
        service.removeUserById(USER_ID);
        service.getUserById(USER_ID);
        verify(usersDAO, atLeastOnce()).getUserById(USER_ID);
    }

    @Test
    void insertUserTest() {
        service.insertUser(USER);
        verify(usersDAO, atLeastOnce()).insertUser(eq(USER));
    }

    @Test
    void editUserTest() {
        service.editUser(USER);
        verify(usersDAO, atLeastOnce()).editUser(eq(USER));
    }

    @Test
    void removeUserByIdTest() {
        service.removeUserById(USER_ID);
        verify(usersDAO, atLeastOnce()).deleteUserById(eq(USER_ID));
    }

    @Test
    void getAllUsersTest() {
        when(usersDAO.getAllUsers()).thenReturn(USER_LIST);
        List<User> result = service.getAllUsers();
        Assertions.assertEquals(USER_LIST, result, "Method index returned wrong result");
    }

    @Test
    void getAllUsersCachedTest() {
        when(usersDAO.getAllUsers()).thenReturn(USER_LIST);
        List<User> result = service.getAllUsers();
        verify(usersDAO, atLeastOnce()).getAllUsers();

        reset(usersDAO);
        when(usersDAO.getAllUsers()).thenReturn(USER_LIST);
        List<User> resultCached = service.getAllUsers();
        verify(usersDAO, never()).getAllUsers();
        Assertions.assertEquals(result, resultCached);
    }

    @Test
    void getAllUsersCacheEvictedOnModification() {
        when(usersDAO.getAllUsers()).thenReturn(USER_LIST);
        List<User> result = service.getAllUsers();
        verify(usersDAO, atLeastOnce()).getAllUsers();

        reset(usersDAO);
        service.editUser(result.get(0));
        when(usersDAO.getAllUsers()).thenReturn(USER_LIST);
        service.getAllUsers();
        verify(usersDAO, atLeastOnce()).getAllUsers();
    }

    @Test
    void getAllUsersCacheEvictedOnDelete() {
        when(usersDAO.getAllUsers()).thenReturn(USER_LIST);
        service.getAllUsers();
        verify(usersDAO, atLeastOnce()).getAllUsers();

        reset(usersDAO);
        service.removeUserById(USER_ID);
        service.getAllUsers();
        verify(usersDAO, atLeastOnce()).getAllUsers();
    }

}
