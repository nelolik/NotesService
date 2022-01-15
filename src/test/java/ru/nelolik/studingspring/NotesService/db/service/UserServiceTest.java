package ru.nelolik.studingspring.NotesService.db.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ru.nelolik.studingspring.NotesService.config.TestConfig;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
public class UserServiceTest {

    @Autowired
    private UsersService usersService;

    private static List<User> users;

    @BeforeAll
    public static void setup() {
        users = new ArrayList<>();
        users.add(new User((long)1, "Aleks"));
        users.add(new User((long)2, "Mikle"));
        users.add(new User((long)3, "Jessika"));

    }

    @Test
    public void insertValuesTest() {
        long lastId = 0;
        for (User user :
                users) {
            lastId = usersService.insert(user);
        }
        Assertions.assertEquals(users.size(), lastId,
                "Count of inserted values is not equal to users size");
    }

    @Test
    public void indexTest() {
        clearDb();
        insertUsers();
        List<User> usersFromDb = usersService.index();
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
        User userFromDb = usersService.index().get(0);
        if (userFromDb != null) {
            User user = usersService.user(userFromDb.getId());
            Assertions.assertTrue(user.equals(userFromDb), "Users with the same id are not equal.");
        } else {
            Assertions.assertEquals(true, false, "Empty index");
        }
    }

    @Test
    public void testEdit() {
        User userFromDb = usersService.index().get(1);
        String oldName = userFromDb.getName();
        userFromDb.setName("New name");
        usersService.edit(userFromDb);
        User newUser = usersService.user(userFromDb.getId());
        Assertions.assertTrue(newUser.equals(userFromDb), "Edited name is not the same like new one. Old name: "
                + oldName + " New name: " + newUser.getName());
    }

    @Test
    public void testDelete() {
        insertUsers();
        User userFromDb = usersService.index().get(1);
        usersService.delete(userFromDb.getId());
        List<User> allUsers = usersService.index();
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
        newUser.setName("Autoname");
        usersService.insert(newUser);
        List<User> result = usersService.index();
        boolean changed = true;
        for (User u :
                result) {
            if (u.getName().equals("Autoname") && u.getId() == oldId) {
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
            usersService.insert(user);
        }
    }

    private void clearDb() {
        List<User> allUsers = usersService.index();
        for (User u :
                allUsers) {
            usersService.delete(u.getId());
        }
    }




}
