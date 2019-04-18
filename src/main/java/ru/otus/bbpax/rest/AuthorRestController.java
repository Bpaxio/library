package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.bbpax.service.AuthorService;
import ru.otus.bbpax.service.model.AuthorDto;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/author")
@AllArgsConstructor
public class AuthorRestController {

    private final AuthorService service;

    @PostMapping
    public void createAuthor(@RequestBody AuthorDto authorDto) {
        service.create(authorDto);
    }

    @PutMapping("{id}")
    public void updateAuthor(@PathVariable String id,
                             String name,
                             String surname,
                             String country) {
        service.update(new AuthorDto(id, name, surname, country));
    }

    @GetMapping("{id}")
    @ResponseBody
    public AuthorDto getAuthor(@PathVariable String id) {
        return service.getAuthorById(id);
    }

    @GetMapping
    @ResponseBody
    public List<AuthorDto> getAuthors() {
        return service.getAll();
    }

    @DeleteMapping("{id}")
    public void deleteAuthorById(@PathVariable String id) {
        service.deleteById(id);
    }
}
