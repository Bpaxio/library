package ru.otus.bbpax.rest.webflux;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.otus.bbpax.repository.reactive.BookReactiveRepo;
import ru.otus.bbpax.rest.exception.WrongRequestParamsException;
import ru.otus.bbpax.service.model.BookDto;

import java.util.Objects;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("flux/book")
@AllArgsConstructor
public class BookRestFluxController {
    private final BookReactiveRepo reactiveRepo;

    @PostMapping
    public Mono<BookDto> createBook(@RequestBody BookDto bookDto) {
        if (!valid(bookDto))
            throw new WrongRequestParamsException();
        log.info("Registration of new book: {}", bookDto);
        return reactiveRepo.save(bookDto.toNewEntity())
                .map(BookDto::fromEntity);
    }

    @PutMapping
    public Mono<BookDto> updateBook(@RequestBody BookDto bookDto) {
        if (!valid(bookDto) || Objects.isNull(bookDto.getId()))
            throw new WrongRequestParamsException();
        return reactiveRepo.save(bookDto.toEntity())
                .map(BookDto::fromEntity);
    }

    @GetMapping("{id}")
    @ResponseBody
    public Mono<BookDto> getBook(@PathVariable String id) {
        return reactiveRepo.findById(id).map(BookDto::fromEntity);
    }

    @GetMapping
    @ResponseBody
    public Flux<BookDto> getBooks() {
        return reactiveRepo.findAll().map(BookDto::fromEntity);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBookById(@PathVariable String id) {
        return reactiveRepo.deleteById(id);
    }

    private boolean valid(BookDto bookDto) {
        return Objects.nonNull(bookDto)
                && Objects.nonNull(bookDto.getName())
                && Objects.nonNull(bookDto.getPublicationDate())
                && Objects.nonNull(bookDto.getPublishingOffice())
                && Objects.nonNull(bookDto.getPrice())
                && Objects.nonNull(bookDto.getGenreId())
                && Objects.nonNull(bookDto.getAuthorId());
    }
}
