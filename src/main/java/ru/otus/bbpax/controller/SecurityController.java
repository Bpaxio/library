package ru.otus.bbpax.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import static ru.otus.bbpax.controller.Templates.FORBIDDEN;

@Controller
public class SecurityController {

    @PostMapping("/accessDenied")
    public String accessDenied() {
        return FORBIDDEN;
    }

}
