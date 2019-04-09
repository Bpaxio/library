package ru.otus.bbpax.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.bbpax.service.AuthorService;
import ru.otus.bbpax.service.model.AuthorView;

@Controller
@AllArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @PostMapping("/author")
    public void createAuthor(String name,
                             String surname,
                             String country) {
        service.create(new AuthorView(null, name, surname, country));
    }

    @PutMapping("/author/{id}")
    public void updateAuthor(@PathVariable("id") String id,
                             String name,
                             String surname,
                             String country) {
        service.update(new AuthorView(id, name, surname, country));
    }

    @GetMapping("/author/{id}")
    public AuthorView getAuthor(String id) {
        return service.getAuthorById(id);
    }

    @GetMapping("/author")
    public String getAuthors() {
//        return service.getAll();
        return "test";
    }

    @DeleteMapping("/author/{id}")
    public void deleteAuthorById(@PathVariable("id") String id) {
        service.deleteById(id);
    }
}
