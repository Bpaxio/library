package ru.otus.bbpax.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.bbpax.service.AuthorService;
import ru.otus.bbpax.service.model.AuthorDto;

import static ru.otus.bbpax.controller.Templates.AUTHOR;
import static ru.otus.bbpax.controller.Templates.AUTHORS;
import static ru.otus.bbpax.controller.Templates.AUTHOR_CREATE;
import static ru.otus.bbpax.controller.Templates.AUTHOR_EDIT;

@Controller
@AllArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @PostMapping("/author")
    public String createAuthor(String name,
                               String surname,
                               String country,
                               Model model
    ) {
        model.addAttribute("author", service.create(new AuthorDto(null, name, surname, country)));
        return AUTHOR;
    }

    @PostMapping("/author/{id}")
    public String updateAuthor(@PathVariable("id") String id,
                               String name,
                               String surname,
                               String country
    ) {
        service.update(new AuthorDto(id, name, surname, country));
        return "redirect:" + id;
    }

    @PostMapping("/author/{id}/delete")
    public String deleteAuthorById(@PathVariable("id") String id, Model model) {
        service.deleteById(id);
        return getAllAuthors(model);
    }

    @GetMapping("/author/{id}")
    public String getAuthor(@PathVariable String id,
                            @RequestParam(value = "action", required = false) String action,
                            Model model
    ) {
        model.addAttribute("author", service.getAuthorById(id));
        if ("edit".equals(action)) {
            return AUTHOR_EDIT;
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
}
