package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.bbpax.service.BookService;
import ru.otus.bbpax.service.model.BookView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@RestController("/book")
@AllArgsConstructor
public class BookController {
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
        BookView book = new BookView(
                name,
                publicationDate,
                publishingOffice,
                price.setScale(2, RoundingMode.HALF_UP),
                genre,
                author
        );

        log.info("Registration of new book: {}", book);
        service.create(book);
    }

    @PutMapping("/${id}")
    public void updateBook(String id,
                           String name,
                           Integer publicationDate,
                           String publishingOffice,
                           BigDecimal price,
                           String genre,
                           String author) {
        service.update(new BookView(
                id,
                name,
                publicationDate,
                publishingOffice,
                price.setScale(2, RoundingMode.HALF_UP),
                genre,
                author
        ));
    }

    @GetMapping("/${id}")
    public BookView getBook(String id) {
        return service.getBookById(id);
    }

    @GetMapping
    public List<BookView> getBooks() {
        return service.getAll();
    }

    @DeleteMapping("/${id}")
    public void deleteBookById(String id) {
        service.deleteById(id);
    }
}
