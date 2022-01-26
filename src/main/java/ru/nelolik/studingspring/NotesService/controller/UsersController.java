package ru.nelolik.studingspring.NotesService.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.Role;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.service.NotesService;
import ru.nelolik.studingspring.NotesService.db.service.UsersService;
import ru.nelolik.studingspring.NotesService.model.UserInput;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/users")
public class UsersController {

    private UsersService usersService;
    private NotesService notesService;

    @Autowired
    public UsersController(UsersService usersService, NotesService notesService) {
        this.usersService = usersService;
        this.notesService = notesService;
    }

    @GetMapping()
    public String getAllUsers(Model model) {
        List<User> users = usersService.getAllUsers();
        model.addAttribute("users", users);
        log.debug("Request GET to address /users. Method getAllUsers()");
        return "users/index";
    }

    @GetMapping(value = "/json", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody()
    public String getAllUsersJson() throws JSONException {
        List<User> users = usersService.getAllUsers();
        List<String> usersString = users.stream().map(u -> {
            try {
                return new JSONObject().put("userId", u.getId()).put("userName", u.getUsername()).toString();
            } catch (JSONException e) {
                log.error("Request GET to /users/json. Error while making JSONObject with userId={}, userName={}.",
                        u.getId(), u.getUsername());
                e.printStackTrace();
            }
            return "error";
        }).toList();
        JSONArray jsonArray = new JSONArray(usersString);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("users", jsonArray);
        log.debug("Request GET to /users/json. Method getAllUsersJson().");
        return jsonObject.toString();
    }

    @GetMapping("/new")
    public String addNewUser(@ModelAttribute UserInput input) {
        usersService.insertUser(new User(0L, input.getInput(), "", Collections.singleton(Role.USER)));
        log.debug("Request GET to /users/new. Method addNewUser(). New username: {}", input.getInput());
        return "redirect:/users";
    }

    @GetMapping("/manage")
    public String manageUsers(Model model) {
        List<User> users = usersService.getAllUsers();
        model.addAttribute("users", users);
        log.debug("Request GET to /users/manage. Method manageUsers().");
        return "users/manage";
    }

    @PostMapping("/manage/delete")
    public String deleteUser(User user, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (id != null) {
            long userId = Long.valueOf(id);
            usersService.removeUserById(userId);
            notesService.removeNotesByUserId(userId);
            log.debug("Request POST to /users/manage/delete. Method deleteUser(). Removed user and his notes with userId={}",
                    userId);
        } else {
            log.debug("Request POST to /users/manage/delete. Method deleteUser(). User id is null");
        }
        return "redirect:/users/manage";
    }

    @PostMapping("manage/edit")
    public String editUser(User user, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (id != null && user.getUsername() != null) {
            usersService.editUser(new User(Long.valueOf(id), user.getUsername(),
                    "", Collections.singleton(Role.USER)));
            log.debug("Request POST to /users/manage/edit. Method manageUsers(). Edited user with id={}, name={}",
                    id, user.getUsername());
        } else {
            log.debug("Request POST to /users/manage/edit. Method manageUsers(). User`s id or name is null: id={}, name={}",
                    id, user.getUsername());
        }
        return "redirect:/users/manage";
    }


        @GetMapping("/{id}")
    public String showUser(@PathVariable("id") long id, Model model) {
        User user = usersService.getUserById(id);
        List<Note> notes = notesService.getNotesByUserId(id);
        if (user == null) {
            user = new User();
        }
        if (notes == null) {
            notes = new ArrayList<>();
        }
        model.addAttribute("user", user);
        model.addAttribute("notes", notes);
        log.debug("Request GET to /users/{}. Method showUser()", id);
        return "users/user";
    }

    @GetMapping(value = "{id}/json", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String showUserJson(@PathVariable("id") long id) throws JSONException {
        User user = usersService.getUserById(id);
        List<Note> notes = notesService.getNotesByUserId(id);
        List<String> convertedNotes = notes.stream().map(n -> {
                    try {
                        return new JSONObject().put("noteId", n.getId())
                                .put("userId", n.getUserId())
                                .put("text", n.getRecord()).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return "Ups";
                }).toList();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", user.getId());
        jsonObject.put("userName", user.getUsername());
        jsonObject.put("notes", convertedNotes);
        log.debug("Request GET to /users/{}/json. Method showUserJson. userId={}, userName={}, notes: {}",
                id, user.getId(), user.getUsername(), convertedNotes);
        return jsonObject.toString();
    }


    @PostMapping("/{id}")
    public String addUserNote(@ModelAttribute UserInput input,
                              BindingResult bindingResult,
                              @PathVariable long id) {
        if (!bindingResult.hasErrors()) {
            Note note = new Note(0L, id, input.getInput());
            notesService.addNote(note);
        } else {
            System.out.println("Note object is null");
        }
        log.debug("Request POST to /users/{}. Method addUserNote()", id);
        return "redirect:/users/" + id;
    }

    @GetMapping("{userId}/{noteId}")
    public String removeUsersNote(@PathVariable("userId") long userId,
                                  @PathVariable("noteId") long noteId) {
        notesService.removeNote(noteId);
        log.debug("Request GET to /users/{}/{}. Method removeUsersNote().", userId, noteId);
        return "redirect:/users/" + userId;
    }

    @ModelAttribute
    public void addUserInputAttribute(Model model) {
        model.addAttribute("input", new UserInput());
    }
}

