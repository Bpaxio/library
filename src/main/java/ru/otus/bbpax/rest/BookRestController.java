package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.bbpax.rest.exception.WrongRequestParamsException;
import ru.otus.bbpax.service.BookService;
import ru.otus.bbpax.service.model.BookDto;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("api/book")
@AllArgsConstructor
public class BookRestController {
    private final BookService service;

    @PostMapping
    public BookDto createBook(@RequestBody BookDto bookDto) {
        if (!valid(bookDto))
            throw new WrongRequestParamsException();

        log.info("Registration of new book: {}", bookDto);
        return service.create(bookDto);
    }

    @PutMapping
    public BookDto updateBook(@RequestBody BookDto bookDto) {
        if (!valid(bookDto) || Objects.isNull(bookDto.getId()))
            throw new WrongRequestParamsException();

        return service.update(bookDto);
    }

    @GetMapping("{id}")
    @ResponseBody
    public BookDto getBook(@PathVariable String id) {
        return service.getBookById(id);
    }

    @GetMapping
    @ResponseBody
    public List<BookDto> getBooks() {
        return service.getAll();
    }

    @DeleteMapping("{id}")
    public void deleteBookById(@PathVariable String id) {
        service.deleteById(id);
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
