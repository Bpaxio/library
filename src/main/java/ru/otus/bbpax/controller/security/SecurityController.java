package ru.otus.bbpax.controller.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static ru.otus.bbpax.controller.Templates.FORBIDDEN;
import static ru.otus.bbpax.controller.Templates.LOGIN;
import static ru.otus.bbpax.controller.Templates.WELCOME;

@Slf4j
@Controller
public class SecurityController {

    @PostMapping("/accessDenied")
    public String accessDenied() {
        return FORBIDDEN;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false, defaultValue = "false") boolean hasError, Model model) {
        model.addAttribute("error", hasError);
        return LOGIN;
    }
    @GetMapping("/welcome")
    public String welcome() {
        return WELCOME;
    }

}
