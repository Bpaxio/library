package ru.otus.bbpax.rest.webflux;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.repository.reactive.BookReactiveRepo;
import ru.otus.bbpax.repository.reactive.GenreReactiveRepo;
import ru.otus.bbpax.service.model.GenreDto;

@RestController("flux/genre")
@AllArgsConstructor
public class GenreRestFluxController {

    private final GenreReactiveRepo reactiveRepo;
    private final BookReactiveRepo bookReactiveRepo;

    @PostMapping
    @ResponseBody
    public Mono<GenreDto> createGenre(@RequestBody String name) {
        return reactiveRepo.insert(new Genre(name))
                .map(GenreDto::fromEntity);
    }

    @PutMapping("{id}")
    @ResponseBody
    public Mono<GenreDto> updateGenre(@RequestBody GenreDto genre) {
        return reactiveRepo.save(genre.toEntity())
                .map(GenreDto::fromEntity);
    }

    @GetMapping("{id}")
    @ResponseBody
    public Mono<GenreDto> getGenre(@PathVariable String id) {
        return reactiveRepo.findById(id)
                .map(GenreDto::fromEntity);
    }

    @GetMapping
    @ResponseBody
    public Flux<GenreDto> getGenres() {
        return reactiveRepo.findAll()
                .map(GenreDto::fromEntity);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGenreById(@PathVariable String id) {
        return reactiveRepo.deleteById(id);
    }

    @GetMapping("{id}/book")
    @ResponseBody
    public Flux<Book> getBooks(@PathVariable String id) {
        return bookReactiveRepo.findAllByGenreId(id);
    }

}
