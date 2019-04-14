package ru.otus.bbpax.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.bbpax.service.BookService;
import ru.otus.bbpax.service.CommentService;
import ru.otus.bbpax.service.model.BookView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static ru.otus.bbpax.controller.Templates.*;

@Slf4j
@Controller
@AllArgsConstructor
public class BookController {
    private final BookService service;
    private final CommentService commentService;

    @PostMapping("/book")
    public String createBook(String name,
                           Integer publicationDate,
                           String publishingOffice,
                           BigDecimal price,
                           String genreName,
                           String authorFirstName,
                           String authorLastname, Model model) {
        log.info("Create book: '{}' published in {} y. as {}, created by {} and costs - {}.",
                name, publicationDate, genreName, authorFirstName + " " + authorLastname, price);
        model.addAttribute("error", "xax");
        BookView book = new BookView(
                name,
                publicationDate,
                publishingOffice,
                price.setScale(2, RoundingMode.HALF_UP),
                genreName,
                authorFirstName,
                authorLastname
        );

        book.getAuthorFullName();
        log.info("Registration of new book: {}", book);
        model.addAttribute("book", BookView.fromEntity(service.create(book)));

        // TODO: 13.04.2019 to book get byId
        return BOOK;
    }

    @PostMapping("/book/{id}")
    public String updateBook(@PathVariable String id,
                           String name,
                           Integer publicationDate,
                           String publishingOffice,
                           BigDecimal price,
                           String genreName,
                           String authorFirstName,
                           String authorLastName) {

        log.info("Update book: '{}[id={}]' published in {} y. as {}, created by {} and costs - {}.",
                name, id, publicationDate, genreName, authorFirstName + " " + authorLastName, price);
        service.update(new BookView(
                id,
                name,
                publicationDate,
                publishingOffice,
                price.setScale(2, RoundingMode.HALF_UP),
                genreName,
                authorFirstName,
                authorLastName
        ));
        return "redirect:" + id;
    }

    @GetMapping("/book/{id}")
    public String getBook(@PathVariable String id, @RequestParam(value = "action", required = false) String action, Model model) {
        model.addAttribute("book", service.getBookById(id));
        if ("edit".equals(action)) {
            return BOOK_EDIT;
        } else if ("delete".equals(action)) {
            service.deleteById(id);
            return getAllBooks(model);
        }
        model.addAttribute("comments", commentService.getCommentsFor(id));
        return BOOK;
    }

    @GetMapping("/book")
    public String getBooks(@RequestParam(value = "action", required = false) String action,
                           @RequestParam(value = "author", required = false) String authorId,
                           @RequestParam(value = "genre", required = false) String genreId,
                           Model model) {
        if ("create".equals(action)) {
            return BOOK_CREATE;
        }

        if (Objects.nonNull(authorId)) {
            model.addAttribute("books", service.getBooksByAuthor(authorId));
            return BOOKS;
        } else if (Objects.nonNull(genreId)) {
            model.addAttribute("books", service.getBooksByGenre(genreId));
            return BOOKS;
        } else {
            return getAllBooks(model);
        }
    }

    private String getAllBooks(Model model) {
        model.addAttribute("books", service.getAll());
        return BOOKS;
    }

    @DeleteMapping("/book/{id}")
    public void deleteBookById(@PathVariable String id) {
        service.deleteById(id);
    }
}
