package ru.otus.bbpax.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.bbpax.service.AuthorService;
import ru.otus.bbpax.service.BookService;
import ru.otus.bbpax.service.CommentService;
import ru.otus.bbpax.service.GenreService;
import ru.otus.bbpax.service.model.BookDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static ru.otus.bbpax.controller.Templates.BOOK;
import static ru.otus.bbpax.controller.Templates.BOOKS;
import static ru.otus.bbpax.controller.Templates.BOOK_CREATE;
import static ru.otus.bbpax.controller.Templates.BOOK_EDIT;

@Slf4j
@Controller
@AllArgsConstructor
public class BookController {
    private final BookService service;
    private final CommentService commentService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @PostMapping("/book")
    public String createBook(String name,
                           Integer publicationDate,
                           String publishingOffice,
                           BigDecimal price,
                           String genreId,
                           String authorId, Model model) {
        log.info("Create bookDto: '{}' published in {} y. as {}, created by {} and costs - {}.",
                name, publicationDate, genreId, authorId, price);
        BookDto bookDto = new BookDto(
                name,
                publicationDate,
                publishingOffice,
                price.setScale(2, RoundingMode.HALF_UP),
                genreId,
                authorId
        );
        log.info("Registration of new bookDto: {}", bookDto);
        BookDto book = service.create(bookDto);
        return "redirect:book/" + (Objects.nonNull(book) && Objects.nonNull(book.getId()) ? book.getId() : "");
    }

    @PostMapping("/book/{id}")
    public String updateBook(@PathVariable String id,
                             String name,
                             Integer publicationDate,
                             String publishingOffice,
                             BigDecimal price,
                             String genreId,
                             String authorId) {

        log.info("Update book: '{}[id={}]' published in {} y. as {}, created by {} and costs - {}.",
                name, id, publicationDate, genreId, authorId, price);
        service.update(new BookDto(
                id,
                name,
                publicationDate,
                publishingOffice,
                price.setScale(2, RoundingMode.HALF_UP),
                genreId,
                authorId
        ));
        return "redirect:" + id;
    }

    @PostMapping("/book/{id}/delete")
    public String deleteBookById(@PathVariable String id, Model model) {
        service.deleteById(id);
        return getAllBooks(model);
    }

    @GetMapping("/book/{id}")
    public String getBook(@PathVariable String id,
                          @RequestParam(value = "action", required = false) String action,
                          @RequestParam(value = "punish", required = false, defaultValue = "false") boolean punish,
                          Model model) {
        BookDto book = service.getBookById(id);
        model.addAttribute("book", book);
        if ("edit".equals(action)) {
            model.addAttribute("authors", authorService.getAll());
            model.addAttribute("genres", genreService.getAll());
            return BOOK_EDIT;
        }
        model.addAttribute("punish", punish);
        model.addAttribute("author", authorService.getAuthorById(book.getAuthorId()));
        model.addAttribute("genre", genreService.getGenreById(book.getGenreId()));
        model.addAttribute("comments", commentService.getCommentsFor(id));
        return BOOK;
    }

    @GetMapping("/book")
    public String getBooks(@RequestParam(value = "action", required = false) String action,
                           @RequestParam(value = "author", required = false) String authorId,
                           @RequestParam(value = "genre", required = false) String genreId,
                           Model model) {
        if ("create".equals(action)) {
            model.addAttribute("authors", authorService.getAll());
            model.addAttribute("genres", genreService.getAll());
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
}
