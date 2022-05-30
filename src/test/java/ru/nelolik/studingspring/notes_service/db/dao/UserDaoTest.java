package ru.nelolik.studingspring.notes_service.db.dao;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ru.nelolik.studingspring.notes_service.config.TestConfig;
import ru.nelolik.studingspring.notes_service.db.dataset.Role;
import ru.nelolik.studingspring.notes_service.db.dataset.User;
import ru.nelolik.studingspring.notes_service.db.dataset.UserRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, UserRoleDAOImplementation.class}, loader = AnnotationConfigContextLoader.class)
public class UserDaoTest {


    private UsersDAO usersDao;

    @Autowired
    public UserDaoTest(UsersDAO usersDAO) {
        this.usersDao = usersDAO;
    }

    private static List<User> users;

    @BeforeAll
    static void setup() {
        users = new ArrayList<>();
        users.add(new User((long)1, "Aleks", "password", Collections.singletonList(new UserRole(1L, Role.ROLE_USER.name()))));
        users.add(new User((long)2, "Mikle", "password", Collections.singletonList(new UserRole(2L, Role.ROLE_USER.name()))));
        users.add(new User((long)3, "Jessika", "password", Collections.singletonList(new UserRole(3L, Role.ROLE_USER.name()))));

    }

    @AfterEach
    void clearContext() {
        clearDb();
    }

    @Test
    void insertValuesTest() {
        for (User user :
                users) {
            usersDao.insertUser(user);
        }
        List<User> usersFromDb = usersDao.getAllUsers();
        assertThat(usersFromDb).isNotNull()
                .containsExactlyInAnyOrderElementsOf(users);
    }

    @Test
    void indexTest() {
        insertUsers();
        List<User> usersFromDb = usersDao.getAllUsers();

        assertThat(usersFromDb).isNotNull().containsExactlyInAnyOrderElementsOf(users);
    }

    @Test
    void readUserTest() {
        insertUsers();
        User userFromDb = usersDao.getAllUsers().get(0);
        if (userFromDb != null) {
            User user = usersDao.getUserById(userFromDb.getId());
            Assertions.assertEquals(user, userFromDb, "Users with the same id are not equal.");
        } else {
            Assertions.fail("Empty list of users in db");
        }
    }

    @Test
    void readNotExistingUserTest() {
        insertUsers();
        long maxId = usersDao.getAllUsers().stream().map(User::getId).max(Long::compareTo).orElse(1L);
        User userFromDb = usersDao.getUserById(maxId + 10L);
        Assertions.assertNull(userFromDb, "Db return User with not existing id.");
    }

    @Test
    void testEdit() {
        insertUsers();
        User userFromDb = usersDao.getAllUsers().get(1);
        String oldName = userFromDb.getUsername();
        userFromDb.setUsername("New name");
        usersDao.editUser(userFromDb);
        User newUser = usersDao.getUserById(userFromDb.getId());
        Assertions.assertEquals(newUser, userFromDb, "Edited name is not the same like new one. Old name: "
                + oldName + " New name: " + newUser.getUsername());
    }

    @Test
    void testDelete() {
        insertUsers();
        User userFromDb = usersDao.getAllUsers().get(1);
        usersDao.deleteUserById(userFromDb.getId());
        List<User> allUsers = usersDao.getAllUsers();
        boolean deleted = true;
        for (User u :
                allUsers) {
            if (userFromDb.equals(u)) {
                deleted = false;
                break;
            }
        }
        Assertions.assertTrue(deleted, "User was not deleted from db.");
    }

    @Test
    void testAutoincrement() {
        insertUsers();
        long maxOldId = users.stream().map(User::getId).max(Long::compareTo).orElse(-1L);
        User newUser = new User();
        long oldId = 1;
        newUser.setId(oldId);
        newUser.setUsername("Autoname");
        newUser.setPassword("autopassword");
        newUser.setRoles(Collections.singletonList(new UserRole(oldId, Role.ROLE_USER.name())));
        usersDao.insertUser(newUser);
        List<User> result = usersDao.getAllUsers();
        assertThat(result).isNotNull()
                .extracting(User::getUsername, User::getId)
                .containsOnlyOnce(Tuple.tuple("Autoname", maxOldId + 1));
    }

    @Test
    void insertUserSetsRightUserIdInUserRole() {
        insertUsers();
        User newUser = new User();
        newUser.setId(1L);
        newUser.setUsername("New Name");
        newUser.setPassword("NewPassword");
        newUser.setRoles(Collections.singletonList(new UserRole(1L, Role.ROLE_USER.name())));
        long userId = usersDao.insertUser(newUser);
        User fromDb = usersDao.getUserById(userId);
        Assertions.assertEquals(fromDb.getId(), fromDb.getRoles().get(0).getUserid());
    }

    private void insertUsers() {
        for (User user :
                users) {
            usersDao.insertUser(user);
            for (UserRole r :
                    user.getRoles()) {
                r.setUserid(user.getId());
            }
        }
    }

    private void clearDb() {
        List<User> allUsers = usersDao.getAllUsers();
        for (User u :
                allUsers) {
            usersDao.deleteUserById(u.getId());
        }
    }




}
