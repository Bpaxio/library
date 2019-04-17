package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.bbpax.service.AuthorService;
import ru.otus.bbpax.service.model.AuthorView;

import java.util.List;

@RestController
@RequestMapping("api/author/")
@AllArgsConstructor
public class AuthorRestController {

    private final AuthorService service;

    @PostMapping
    public void createAuthor(String name,
                             String surname,
                             String country) {
        service.create(new AuthorView(null, name, surname, country));
    }

    @PutMapping("{id}")
    public void updateAuthor(@PathVariable String id,
                             String name,
                             String surname,
                             String country) {
        service.update(new AuthorView(id, name, surname, country));
    }

    @GetMapping("{id}")
    public AuthorView getAuthor(@PathVariable String id) {
        return service.getAuthorById(id);
    }

    @GetMapping
    public List<AuthorView> getAuthors() {
        return service.getAll();
    }

    @DeleteMapping("{id}")
    public void deleteAuthorById(@PathVariable String id) {
        service.deleteById(id);
    }
}
