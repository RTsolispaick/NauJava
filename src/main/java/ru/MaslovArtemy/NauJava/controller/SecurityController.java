package ru.MaslovArtemy.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.MaslovArtemy.NauJava.model.User;
import ru.MaslovArtemy.NauJava.service.UserService;

@Controller
public class SecurityController {
    private final UserService userService;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String getRegistration() {
        return "registration";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/registration")
    public String registerUser(User user) {
        userService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String getHome() {
        return "home";
    }
}
