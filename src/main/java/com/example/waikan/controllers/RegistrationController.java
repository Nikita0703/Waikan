package com.example.waikan.controllers;

import com.example.waikan.models.User;
import com.example.waikan.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public String registerNewUser(Principal principal, User user, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        boolean registrationWasSuccessful = userService.createUser(user);
        if (!registrationWasSuccessful) {
            model.addAttribute("user", user);
            model.addAttribute("errorRegistration", "Пользователь уже существует");
            return "registration";
        } else {
            return "redirect:/register-successfully?email=" + user.getEmail();
        }
    }

    @GetMapping("/register-successfully")
    public String registerSuccessfully(Principal principal, @RequestParam(required = false) String email,
                                        Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("email", email);
        return "register-successfully";
    }

    @GetMapping("/activate/{code}")
    public String activateUser(Principal principal, Model model, @PathVariable("code") String code) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        if (userService.activateUser(code)) {
            model.addAttribute("activate", true);
        } else {
            model.addAttribute("activate", false);
        }
        return "activate";
    }

    @GetMapping("/registration")
    public String registration(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "registration";
    }

    @GetMapping("/login")
    public String login(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "login";
    }
}
