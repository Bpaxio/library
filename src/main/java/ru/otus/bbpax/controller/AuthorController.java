package ru.otus.bbpax.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.bbpax.service.AuthorService;
import ru.otus.bbpax.service.model.AuthorView;

import static ru.otus.bbpax.controller.Templates.*;

@Controller
@AllArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @PostMapping("/author")
    public String createAuthor(String name,
                             String surname,
                             String country
    ) {
        service.create(new AuthorView(null, name, surname, country));
        return AUTHOR;
    }

    @PostMapping("/author/{id}")
    public String updateAuthor(@PathVariable("id") String id,
                             String name,
                             String surname,
                             String country
    ) {
        service.update(new AuthorView(id, name, surname, country));
        return AUTHOR;
    }

    @GetMapping("/author/{id}")
    public String getAuthor(@PathVariable String id,
                            @RequestParam(value = "action", required = false) String action,
                            Model model
    ) {
        model.addAttribute("author", service.getAuthorById(id));
        if ("edit".equals(action)) {
            return AUTHOR_EDIT;
        } else if ("delete".equals(action)) {
            service.deleteById(id);
            return getAllAuthors(model);
        }
        return AUTHOR;
    }

    @GetMapping("/author")
    public String getAuthors(@RequestParam(value = "action", required = false) String action,
                             Model model
    ) {
        if ("create".equals(action)) {
            return AUTHOR_CREATE;
        }
        return getAllAuthors(model);
    }

    private String getAllAuthors(Model model) {
        model.addAttribute("authors", service.getAll());
        return AUTHORS;
    }

    @DeleteMapping("/author/{id}")
    public void deleteAuthorById(@PathVariable("id") String id) {
        service.deleteById(id);
    }
}
