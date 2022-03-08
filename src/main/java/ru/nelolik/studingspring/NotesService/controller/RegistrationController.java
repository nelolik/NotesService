package ru.nelolik.studingspring.NotesService.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.nelolik.studingspring.NotesService.db.dataset.Role;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;
import ru.nelolik.studingspring.NotesService.db.service.UserDataService;
import ru.nelolik.studingspring.NotesService.db.service.UserDataServiceImpl;

import java.util.Collections;

@Slf4j
@Controller
public class RegistrationController {

    @Autowired
    UserDataService usersService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User userFromDb = usersService.getUserByName(user.getUsername());
        if (userFromDb != null) {
            model.addAttribute("message", "User exists");
            log.info("RegistrationController.addUser. User with name /{} already exists.", user.getUsername());
            return "registration";
        }
        user.setRoles(Collections.singletonList(new UserRole(1L, Role.ROLE_USER.name())));
        usersService.insertUser(user);
        log.info("RegistrationController.addUser. User with name /{} successfully added.", user.getUsername());
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        log.debug("Request GET to /users/login. Method login().");
        return "login";
    }

}
