package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.otus.bbpax.service.BookService;
import ru.otus.bbpax.service.model.BookDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("api/book")
@AllArgsConstructor
public class BookRestController {
    private final BookService service;

    @PostMapping
    public void createBook(String name,
                           Integer publicationDate,
                           String publishingOffice,
                           BigDecimal price,
                           String genre,
                           String author) {
        log.info("Create book: '{}' published in {} y. as {}, created by {} and costs - {}.",
                name, publicationDate, genre, author, price);
        BookDto book = new BookDto(
                name,
                publicationDate,
                publishingOffice,
                price.setScale(2, RoundingMode.HALF_UP),
                genre,
                author,author
        );

        log.info("Registration of new book: {}", book);
        service.create(book);
    }

    @PutMapping("{id}")
    public void updateBook(String id,
                           String name,
                           Integer publicationDate,
                           String publishingOffice,
                           BigDecimal price,
                           String genre,
                           String author) {
        service.update(new BookDto(
                id,
                name,
                publicationDate,
                publishingOffice,
                price.setScale(2, RoundingMode.HALF_UP),
                genre,
                author,author
        ));
    }

    @GetMapping("{id}")
    @ResponseBody
    public BookDto getBook(String id) {
        return service.getBookById(id);
    }

    @GetMapping
    @ResponseBody
    public List<BookDto> getBooks() {
        return service.getAll();
    }

    @DeleteMapping("{id}")
    public void deleteBookById(String id) {
        service.deleteById(id);
    }
}
