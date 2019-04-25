package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.bbpax.service.AuthorService;
import ru.otus.bbpax.service.model.AuthorView;

import java.util.List;

// @RestController("/author")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @PostMapping
    public void createAuthor(String name,
                             String surname,
                             String country) {
        service.create(new AuthorView(null, name, surname, country));
    }

    @PutMapping("/${id}")
    public void updateAuthor(@PathVariable("id") String id,
                             String name,
                             String surname,
                             String country) {
        service.update(new AuthorView(id, name, surname, country));
    }

    @GetMapping("/${id}")
    public AuthorView getAuthor(String id) {
        return service.getAuthorById(id);
    }

    @GetMapping
    public List<AuthorView> getAuthors() {
        return service.getAll();
    }

    @DeleteMapping("/${id}")
    public void deleteAuthorById(@PathVariable("id") String id) {
        service.deleteById(id);
    }
}