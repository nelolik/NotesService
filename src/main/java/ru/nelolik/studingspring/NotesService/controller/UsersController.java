package ru.nelolik.studingspring.NotesService.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.service.UserDataService;
import ru.nelolik.studingspring.NotesService.dto.NoteDTO;
import ru.nelolik.studingspring.NotesService.dto.NoteToDtoConverter;
import ru.nelolik.studingspring.NotesService.dto.UserDTO;
import ru.nelolik.studingspring.NotesService.dto.UserToDtoConverter;
import ru.nelolik.studingspring.NotesService.model.UserInput;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {

    private final UserDataService service;

    private final UserToDtoConverter userToDtoConverter;

    private final NoteToDtoConverter noteToDtoConverter;

    @GetMapping()
    public String getAllUsers(Model model) {
        List<User> users = service.getAllUsers();
        List<UserDTO> dtos = userToDtoConverter.usersToDto(users);
        model.addAttribute("users", dtos);
        log.debug("Request GET to address /users. Method getAllUsers()");
        return "users/index";
    }

    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public String getAllUsersJson() throws JsonProcessingException {
        List<User> users = service.getAllUsers();
        List<UserDTO> dtos = userToDtoConverter.usersToDto(users);
        ObjectMapper mapper = new ObjectMapper();
        log.debug("Request GET to address /users. Method getAllUsersJson()");
        return mapper.writeValueAsString(dtos);
    }

    @GetMapping("/new")
    public String addNewUser(@ModelAttribute UserInput input) {
        if (input == null || input.getInput() == null || input.getInput().isBlank()) {
            log.error("Attempt to add user with empty name");
            return "redirect:/users";
        }
        UserDTO userDTO = new UserDTO(0L, input.getInput());
        service.insertUser(userToDtoConverter.userDtoToUser(userDTO));
        log.debug("Request GET to /users/new. Method addNewUser(). New username: {}", input.getInput());
        return "redirect:/users";
    }

    @GetMapping("/manage")
    public String manageUsers(Model model) {
        List<User> users = service.getAllUsers();
        List<UserDTO> dtos = userToDtoConverter.usersToDto(users);
        model.addAttribute("users", dtos);
        log.debug("Request GET to /users/manage. Method manageUsers().");
        return "users/manage";
    }

    @PostMapping("/manage/delete")
    public String deleteUser(User user, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (id != null) {
            long userId = Long.valueOf(id);
            service.removeUserById(userId);
            service.removeNotesByUserId(userId);
            log.debug("Request POST to /users/manage/delete. Method deleteUser(). Removed user and his notes with userId={}",
                    userId);
        } else {
            log.debug("Request POST to /users/manage/delete. Method deleteUser(). User id is null");
        }
        return "redirect:/users/manage";
    }

    @PostMapping("manage/edit")
    public String editUser(HttpServletRequest request) {
        String id_string = request.getParameter("id");
        String name = request.getParameter("name");
        if (id_string == null || name == null) {
            log.debug("Request POST to /users/manage/edit. Method manageUsers(). User`s id or name is null: id ={}, name = {}",
                    id_string, name);
            return "redirect:/users/manage";
        }
        Long id = Long.valueOf(id_string);
        User user = service.getUserById(id);
        if (user == null) {
            log.debug("Request POST to /users/manage/edit. Method manageUsers(). User with id ={} is not registered",
                    id);
            return "redirect:/users/manage";
        }
        user.setUsername(name);
        service.editUser(user);
        log.debug("Request POST to /users/manage/edit. Method manageUsers(). Edited user with id={}, name={}",
                id, name);
        return "redirect:/users/manage";
    }


        @GetMapping("/{id}")
    public String showUser(@PathVariable("id") long id, Model model) {
        User user = service.getUserById(id);
        List<Note> notes = service.getNotesByUserId(id);
        if (user == null) {
            user = new User();
        }
        if (notes == null) {
            notes = new ArrayList<>();
        }
        UserDTO userDTO = userToDtoConverter.userToDto(user);
        List<NoteDTO> noteDTOS = noteToDtoConverter.notesToDto(notes);
        model.addAttribute("user", userDTO);
        model.addAttribute("notes", noteDTOS);
        log.debug("Request GET to /users/{}. Method showUser()", id);
        return "users/user";
    }

    @GetMapping(value = "{id}/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String showUserJson(@PathVariable("id") long id) throws JsonProcessingException {
        User user = service.getUserById(id);
        List<Note> notes = service.getNotesByUserId(id);
        UserDTO userDTO = userToDtoConverter.userToDto(user);
        List<NoteDTO> noteDTOS = noteToDtoConverter.notesToDto(notes);
        Map<String, Object> m = new HashMap<>();
        m.put("user", userDTO);
        m.put("notes", noteDTOS);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(m);
    }


    @PostMapping("/{id}")
    public String addUserNote(@ModelAttribute UserInput input,
                              BindingResult bindingResult,
                              @PathVariable long id) {
        if (!bindingResult.hasErrors()) {
            NoteDTO noteDTO = new NoteDTO(0L, id, input.getInput());
            service.addNote(noteToDtoConverter.noteDtoToNote(noteDTO));
        } else {
            log.debug("Note object is null");
        }
        log.debug("Request POST to /users/{}. Method addUserNote()", id);
        return "redirect:/users/" + id;
    }

    @GetMapping("{userId}/{noteId}")
    public String removeUsersNote(@PathVariable("userId") long userId,
                                  @PathVariable("noteId") long noteId) {
        service.removeNote(noteId, userId);
        log.debug("Request GET to /users/{}/{}. Method removeUsersNote().", userId, noteId);
        return "redirect:/users/" + userId;
    }

    @ModelAttribute
    public void addUserInputAttribute(Model model) {
        model.addAttribute("input", new UserInput());
    }
}

