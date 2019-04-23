package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.bbpax.rest.exception.WrongRequestParamsException;
import ru.otus.bbpax.service.AuthorService;
import ru.otus.bbpax.service.model.AuthorDto;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("api/author")
@AllArgsConstructor
public class AuthorRestController {

    private final AuthorService service;

    @PostMapping
    public void createAuthor(@RequestBody AuthorDto authorDto) {
        if (Objects.isNull(authorDto)
                || Objects.isNull(authorDto.getName())
                || Objects.isNull(authorDto.getSurname())
                || Objects.isNull(authorDto.getCountry())
        ) throw new WrongRequestParamsException();

        service.create(authorDto);
    }

    @PutMapping
    public void updateAuthor(@RequestBody AuthorDto authorDto) {
        if (Objects.isNull(authorDto)
                || Objects.isNull(authorDto.getId())
                || Objects.isNull(authorDto.getName())
                || Objects.isNull(authorDto.getSurname())
                || Objects.isNull(authorDto.getCountry())
        )
            throw new WrongRequestParamsException();
        service.update(authorDto);
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
