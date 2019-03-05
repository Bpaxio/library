package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.bbpax.controller.model.AuthorView;
import ru.otus.bbpax.service.AuthorService;

import java.util.List;

@AllArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @PostMapping(value = "author")
    public void createAuthor(String name,
                             String surname,
                             String country) {
        service.create(new AuthorView(null, name, surname, country));
    }

    @PutMapping(value = "author")
    public void updateAuthor(String id,
                             String name,
                             String surname,
                             String country) {
        service.update(new AuthorView(id, name, surname, country));
    }

    @GetMapping(value = "author")
    public AuthorView getAuthor(String id) {
        return service.getAuthorById(id);
    }

    @GetMapping(value = "author")
    public List<AuthorView> getAuthors() {
        return service.getAll();
    }

    @PutMapping(value = "author")
    public void deleteAuthorById(String id) {
        service.deleteById(id);
    }
}
