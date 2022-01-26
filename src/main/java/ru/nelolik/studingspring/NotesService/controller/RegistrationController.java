package ru.nelolik.studingspring.NotesService.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.nelolik.studingspring.NotesService.db.dataset.Role;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.service.UsersService;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    UsersService usersService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User userFromDb = usersService.getUserByName(user.getName());

        if (userFromDb != null) {
            model.addAttribute("message", "User exists");
            return registration();
        }
        user.setRoles(Collections.singleton(Role.USER));
        return "redirect:/login";
    }
}
