package ru.nelolik.studingspring.SpringWebMVC.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nelolik.studingspring.SpringWebMVC.db.dataset.Note;
import ru.nelolik.studingspring.SpringWebMVC.db.dataset.User;
import ru.nelolik.studingspring.SpringWebMVC.db.service.NotesService;
import ru.nelolik.studingspring.SpringWebMVC.db.service.UsersService;
import ru.nelolik.studingspring.SpringWebMVC.model.UserInput;

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
    public String index(Model model) {
        List<User> users = usersService.index();
        model.addAttribute("users", users);
        return "users/index";
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable("id") long id, Model model) {
        User user = usersService.user(id);
        List<Note> notes = notesService.getNotesByUserId(id);
        model.addAttribute("user", user);
        model.addAttribute("notes", notes);
        model.addAttribute("input", new UserInput());
        return "users/user";
    }

    @PostMapping("/{id}")
    public String addUserNote(@ModelAttribute UserInput input,
                              BindingResult bindingResult,
                              @PathVariable long id) {
        if (!bindingResult.hasErrors()) {
            Note note = new Note(0L, id, input.getInput());
            notesService.addNote(note);
        } else{
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
}

