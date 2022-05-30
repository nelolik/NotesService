package ru.nelolik.studingspring.NotesService.db.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import ru.nelolik.studingspring.NotesService.config.CacheNames;
import ru.nelolik.studingspring.NotesService.config.TestConfig;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.Role;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class, UsersDAO.class, NotesDAO.class, UserDataServiceImpl.class})
public class UserDataServiceTest {

    @MockBean
    UsersDAO usersDAO;

    @MockBean
    NotesDAO notesDAO;

    @Autowired
    UserDataService service;

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

    private static final long NOTE_ID = 1;
    private static final long USER_ID1 = 1;
    private static final long USER_ID2 = 2;
    private static final Note NOTE1 = new Note(NOTE_ID, USER_ID1, "Some text");
    private static final Note NOTE2 = new Note(NOTE_ID + 1, USER_ID1, "Some text");
    private static final Note NOTE3 = new Note(NOTE_ID + 2, USER_ID2, "Some text");
    private static final List<Note> NOTES = Arrays.asList(NOTE1, NOTE2, NOTE3);
    private static final List<Note> NOTES_USER1 = Arrays.asList(NOTE1, NOTE2);
    private static final List<Note> NOTES_USER2 = Arrays.asList(NOTE3);

    @BeforeEach
    void beforeEach() {
        Cache cache = cacheManager.getCache(CacheNames.NOTES);
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

    @Test
    void getAllNotesTest() {
        when(notesDAO.getAllNotes()).thenReturn(NOTES);
        List<Note> result = service.getAllNotes();
        assertThat(result).isNotNull().containsExactlyInAnyOrderElementsOf(NOTES);
    }

    @Test
    void getNotesByUserIdTest() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        List<Note> result = service.getNotesByUserId(USER_ID1);
        Assertions.assertEquals(NOTES_USER1, result);
    }

    @Test
    void getNotesByUserIdCachedTest() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID1);

        reset(notesDAO);
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, never()).getNotesByUserId(USER_ID1);
    }

    @Test
    void getNotesByUserIdCachedOnRightKey() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID1);

        reset(notesDAO);
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        when(notesDAO.getNotesByUserId(USER_ID2)).thenReturn(NOTES_USER2);
        service.getNotesByUserId(USER_ID2);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID2);
    }

    @Test
    void getNotesByUserIdCacheEvictedOnUserRemove() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID1);

        reset(notesDAO);
        service.removeNotesByUserId(USER_ID1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID1);
    }

    @Test
    void getNotesByUserIdCacheEvictedOnNoteRemove() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID1);

        reset(notesDAO);
        service.removeNote(NOTE_ID, USER_ID1);
        service.getNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).getNotesByUserId(USER_ID1);
    }

    @Test
    void getNoteByIdTest() {
        when(notesDAO.getOneNote(NOTE_ID)).thenReturn(NOTE1);
        Note result = service.getNoteById(NOTE_ID);
        Assertions.assertEquals(NOTE1, result);
    }

    @Test
    void addNoteTest() {
        when(notesDAO.addNote(NOTE1)).thenReturn(NOTE_ID);
        long result = service.addNote(NOTE1);
        Assertions.assertEquals(NOTE_ID, result);
    }

    @Test
    void removeNotesByUserIdTest() {
        service.removeNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).removeNotesByUserId(eq(USER_ID1));
    }

    @Test
    void removeNote() {
        service.removeNote(NOTE_ID, USER_ID);
        verify(notesDAO, atLeastOnce()).removeNoteByNoteId(eq(NOTE_ID));
    }
}
