package ru.nelolik.studingspring.SpringWebMVC.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.nelolik.studingspring.SpringWebMVC.db.service.UsersService;

@Controller
@RequestMapping("/users")
public class UsersController {

    private UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping()
    public String index() {
        return "users/index";
    }

    @GetMapping("/{id}")
    public String showUser() {
        return "users/user";
    }
}
