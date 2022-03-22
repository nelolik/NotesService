package ru.nelolik.studingspring.NotesService.db.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO;
import ru.nelolik.studingspring.NotesService.db.dao.UsersDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.Role;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;
import ru.nelolik.studingspring.NotesService.dto.NoteDTO;
import ru.nelolik.studingspring.NotesService.dto.UserDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserDataServiceTest {

    @MockBean
    UsersDAO usersDAO;

    @MockBean
    NotesDAO notesDAO;

    @Autowired
    UserDataService service;

    private static final long USER_ID = 1;
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

    @Test
    void getUserByIdTest() {
        when(usersDAO.getUserById(USER_ID)).thenReturn(USER);
        UserDTO u = service.getUserById(USER_ID);
        Assertions.assertEquals(new UserDTO(USER), u, "Mocked user and returned user ane not equal.");
    }

    @Test
    void insertUserTest() {
        service.insertUser(new UserDTO(USER));
        verify(usersDAO, atLeastOnce()).insertUser(eq(USER));
    }

    @Test
    void editUserTest() {
        service.editUser(new UserDTO(USER));
        verify(usersDAO, atLeastOnce()).editUser(eq(USER));
    }

    @Test
    void removeUserByIdTest() {
        service.removeUserById(USER_ID);
        verify(usersDAO,atLeastOnce()).deleteUserById(eq(USER_ID));
    }

    @Test
    void getAllUsersTest() {
        when(usersDAO.getAllUsers()).thenReturn(USER_LIST);
        List<UserDTO> result = service.getAllUsers();
        Assertions.assertEquals(USER_LIST.stream().map(UserDTO::new),
                result, "Method index returned wrong result");
    }

    @Test
    void getAllNotesTest() {
        when(notesDAO.getAllNotes()).thenReturn(NOTES);
        List<NoteDTO> result = service.getAllNotes();
        List<NoteDTO> noteDTOS = NOTES.stream().map(NoteDTO::new).toList();
        assertThat(result).isNotNull().containsExactlyInAnyOrderElementsOf(noteDTOS);
    }

    @Test
    void getNotesByUserIdTest() {
        when(notesDAO.getNotesByUserId(USER_ID1)).thenReturn(NOTES_USER1);
        List<NoteDTO> result = service.getNotesByUserId(USER_ID1);
        List<NoteDTO> noteDTOS = NOTES_USER1.stream().map(NoteDTO::new).toList();
        Assertions.assertEquals(NOTES_USER1, result);
    }

    @Test
    void getNoteByIdTest() {
        when(notesDAO.getOneNote(NOTE_ID)).thenReturn(NOTE1);
        NoteDTO result = service.getNoteById(NOTE_ID);
        Assertions.assertEquals(new NoteDTO(NOTE1), result);
    }

    @Test
    void addNoteTest() {
        when(notesDAO.addNote(NOTE1)).thenReturn(NOTE_ID);
        long result = service.addNote(new NoteDTO(NOTE1));
        Assertions.assertEquals(NOTE_ID, result);
    }

    @Test
    void removeNotesByUserIdTest() {
        service.removeNotesByUserId(USER_ID1);
        verify(notesDAO, atLeastOnce()).removeNotesByUserId(eq(USER_ID1));
    }

    @Test
    void removeNote() {
        service.removeNote(NOTE_ID);
        verify(notesDAO, atLeastOnce()).removeNoteByNoteId(eq(NOTE_ID));
    }
}
