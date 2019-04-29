package ru.otus.bbpax.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import static ru.otus.bbpax.controller.SecurityUtils.getAuthenticated;
import static ru.otus.bbpax.controller.SecurityUtils.getRoles;
import static ru.otus.bbpax.controller.SecurityUtils.getUsername;

@ControllerAdvice
public class UserControllerAdvice {

    @ModelAttribute("roles")
    public void roles(Model model) {
        model.addAttribute("roles", getRoles());
    }

    @ModelAttribute("authenticated")
    public void authenticated(Model model) {
        model.addAttribute("authenticated", getAuthenticated());
    }

    @ModelAttribute("username")
    public void username(Model model) {
        model.addAttribute("username", getUsername());
    }
}
