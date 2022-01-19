package ru.nelolik.studingspring.NotesService.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.service.NotesService;
import ru.nelolik.studingspring.NotesService.db.service.UsersService;
import ru.nelolik.studingspring.NotesService.model.UserInput;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
        return "users/index";
    }

    @GetMapping("/json")
    @ResponseBody
    public String getAllUsersJson() throws JSONException {
        List<User> users = usersService.getAllUsers();
        JSONArray jsonArray = new JSONArray(users);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("users", jsonArray);
        return jsonObject.toString(4);
    }

    @GetMapping("/new")
    public String addNewUser(@ModelAttribute UserInput input) {
        usersService.insertUser(new User(0L, input.getInput()));
        return "redirect:/users";
    }

    @GetMapping("/manage")
    public String manageUsers(Model model) {
        List<User> users = usersService.getAllUsers();
        model.addAttribute("users", users);
        System.out.println("We are in get controller /manage");
        return "users/manage";
    }

    //TODO: divide to 2 independent methods
    @PostMapping("/manage")
    public String deleteUser(User user, HttpServletRequest request) {
        String method = request.getParameter("method");
        String id = request.getParameter("id");
        if (method == null) {
            return "redirect:/users/manage";
        }
        if (method.equals("edit") && id != null && user.getName() != null) {
            usersService.editUser(new User(Long.valueOf(id), user.getName()));
        } else if (method.equals("delete") && id != null) {
            long userId = Long.valueOf(id);
            usersService.removeUserById(userId);
            notesService.removeNotesByUserId(userId);
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
        return "users/user";
    }

    @GetMapping("{id}/json")
    @ResponseBody
    public String showUserJson(@PathVariable("id") long id) throws JSONException {
        User user = usersService.getUserById(id);
        List<Note> notes = notesService.getNotesByUserId(id);

        JSONArray notesJson = new JSONArray(notes);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", user.getId());
        jsonObject.put("userName", user.getName());
        jsonObject.put("notes", notes);
        return jsonObject.toString(4);
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
        return "redirect:/users/" + id;
    }

    @GetMapping("{userId}/{noteId}")
    public String removeUsersNote(@PathVariable("userId") long userId,
                                  @PathVariable("noteId") long noteId) {
        notesService.removeNote(noteId);
        return "redirect:/users/" + userId;
    }

    @ModelAttribute
    public void addUserInputAttribute(Model model) {
        model.addAttribute("input", new UserInput());
    }
}

