package ru.nelolik.studingspring.NotesService.db.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ru.nelolik.studingspring.NotesService.config.TestConfig;
import ru.nelolik.studingspring.NotesService.db.dataset.Role;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
public class UserDaoTest {


    private UsersDAO usersDao;

    @Autowired
    public UserDaoTest(UsersDAO usersService) {
        this.usersDao = usersService;
    }

    private static List<User> users;

    @BeforeAll
    public static void setup() {
        users = new ArrayList<>();
        users.add(new User((long)1, "Aleks", "password", Collections.singleton(Role.USER)));
        users.add(new User((long)2, "Mikle", "password", Collections.singleton(Role.USER)));
        users.add(new User((long)3, "Jessika", "password", Collections.singleton(Role.USER)));

    }

    @Test
    public void insertValuesTest() {
        long lastId = 0;
        for (User user :
                users) {
            lastId = usersDao.insertUser(user);
        }
        Assertions.assertEquals(users.size(), lastId,
                "Count of inserted values is not equal to users size");
    }

    @Test
    public void indexTest() {
        clearDb();
        insertUsers();
        List<User> usersFromDb = usersDao.getAllUsers();
        boolean sizeEqual = users.size() == usersFromDb.size();
        Assertions.assertTrue(sizeEqual, "Count of written and read records differs");
        boolean elementsAreEqual = true;

        if (sizeEqual) {
            for (User u :
                    users) {
                elementsAreEqual &= usersFromDb.contains(u);
            }
        } else {
            elementsAreEqual = false;
        }
        Assertions.assertTrue(elementsAreEqual, "Recorde list of users is not equal to written list of users");
    }

    @Test
    public void readUserTest() {
        User userFromDb = usersDao.getAllUsers().get(0);
        if (userFromDb != null) {
            User user = usersDao.getUserById(userFromDb.getId());
            Assertions.assertTrue(user.equals(userFromDb), "Users with the same id are not equal.");
        } else {
            Assertions.assertEquals(true, false, "Empty index");
        }
    }

    @Test
    public void testEdit() {
        User userFromDb = usersDao.getAllUsers().get(1);
        String oldName = userFromDb.getUsername();
        userFromDb.setUsername("New name");
        usersDao.editUser(userFromDb);
        User newUser = usersDao.getUserById(userFromDb.getId());
        Assertions.assertTrue(newUser.equals(userFromDb), "Edited name is not the same like new one. Old name: "
                + oldName + " New name: " + newUser.getUsername());
    }

    @Test
    public void testDelete() {
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
        clearDb();
    }

    @Test
    public void testAutoincrement() {
        insertUsers();
        User newUser = new User();
        long oldId = 1;
        newUser.setId(oldId);
        newUser.setUsername("Autoname");
        usersDao.insertUser(newUser);
        List<User> result = usersDao.getAllUsers();
        boolean changed = true;
        for (User u :
                result) {
            if (u.getUsername().equals("Autoname") && u.getId() == oldId) {
                changed = false;
                break;
            }
        }
        Assertions.assertTrue(changed, "Id of new record wasn`t auto incremented");
        clearDb();
    }

    private void insertUsers() {
        for (User user :
                users) {
            usersDao.insertUser(user);
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
