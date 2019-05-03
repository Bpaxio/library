package ru.otus.bbpax.rest.webflux;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.bbpax.repository.reactive.AuthorReactiveRepo;
import ru.otus.bbpax.repository.reactive.BookReactiveRepo;
import ru.otus.bbpax.rest.exception.WrongRequestParamsException;
import ru.otus.bbpax.service.model.AuthorDto;
import ru.otus.bbpax.service.model.BookDto;

import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("flux/author")
@AllArgsConstructor
public class AuthorRestFluxController {

    private final AuthorReactiveRepo reactiveRepo;
    private final BookReactiveRepo bookReactiveRepo;

    @PostMapping
    public Mono<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        if (Objects.isNull(authorDto)
                || Objects.isNull(authorDto.getName())
                || Objects.isNull(authorDto.getSurname())
                || Objects.isNull(authorDto.getCountry())
        ) throw new WrongRequestParamsException();

        return reactiveRepo.save(authorDto.toEntity()).map(AuthorDto::fromEntity);
    }

    @PutMapping
    public Mono<AuthorDto> updateAuthor(@RequestBody AuthorDto authorDto) {
        if (Objects.isNull(authorDto)
                || Objects.isNull(authorDto.getId())
                || Objects.isNull(authorDto.getName())
                || Objects.isNull(authorDto.getSurname())
                || Objects.isNull(authorDto.getCountry())
        )
            throw new WrongRequestParamsException();
        return reactiveRepo.save(authorDto.toEntity()).map(AuthorDto::fromEntity);
    }

    @GetMapping("{id}")
    @ResponseBody
    public Mono<AuthorDto> getAuthor(@PathVariable String id) {
        return reactiveRepo.findById(id).map(AuthorDto::fromEntity);
    }

    @GetMapping
    @ResponseBody
    public Flux<AuthorDto> getAuthors() {
        return reactiveRepo.findAll().map(AuthorDto::fromEntity);
    }

    @GetMapping("{id}/book")
    @ResponseBody
    public Flux<BookDto> getBooks(@PathVariable String id) {
        return bookReactiveRepo.findAllByAuthorId(id)
                .map(BookDto::fromEntity);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAuthorById(@PathVariable String id) {
        return reactiveRepo.deleteById(id);
    }
}
