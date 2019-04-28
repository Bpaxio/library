package ru.otus.bbpax.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class SecurityController {
    @ModelAttribute("user")
    public void setUser(Model model) {

    }
}
