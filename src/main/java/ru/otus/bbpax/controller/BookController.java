package ru.otus.bbpax.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.bbpax.service.BookService;
import ru.otus.bbpax.service.CommentService;
import ru.otus.bbpax.service.model.BookView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static ru.otus.bbpax.controller.Templates.BOOK;
import static ru.otus.bbpax.controller.Templates.BOOKS;

@Slf4j
@Controller
@AllArgsConstructor
public class BookController {
    private final BookService service;
    private final CommentService commentService;

    @PostMapping("/book")
    public void createBook(String name,
                           Integer publicationDate,
                           String publishingOffice,
                           BigDecimal price,
                           String genre,
                           String authorFirstName,
                           String authorLastname, Model model) {
        log.info("Create book: '{}' published in {} y. as {}, created by {} and costs - {}.",
                name, publicationDate, genre, authorFirstName + " " + authorLastname, price);
        model.addAttribute("error", "xax");
        BookView book = new BookView(
                name,
                publicationDate,
                publishingOffice,
                price.setScale(2, RoundingMode.HALF_UP),
                genre,
                authorFirstName,
                authorLastname
        );

        book.getAuthorFullName();
        log.info("Registration of new book: {}", book);
        service.create(book);
    }

    @PutMapping("/book/{id}")
    public void updateBook(@PathVariable String id,
                           String name,
                           Integer publicationDate,
                           String publishingOffice,
                           BigDecimal price,
                           String genre,
                           String authorFirstName,
                           String authorLastname, Model model) {

//        service.update(new BookView(
//                id,
//                name,
//                publicationDate,
//                publishingOffice,
//                price.setScale(2, RoundingMode.HALF_UP),
//                genre,
//                author
//        ));
    }

    @GetMapping(name = "/book/{id}")
    public String getBook(@PathVariable String id, Model model) {
//        log.info("edit: {}", model.containsAttribute("edit"));
        model.addAttribute("book", service.getBookById(id));
        model.addAttribute("comments", commentService.getCommentsFor(id));
        return BOOK;
    }

    @GetMapping("/book")
    public String getBooks(Model model) {
        model.addAttribute("books", service.getAll());
        return BOOKS;
    }

    @DeleteMapping("/book/{id}")
    public void deleteBookById(@PathVariable String id) {
        service.deleteById(id);
    }
}
