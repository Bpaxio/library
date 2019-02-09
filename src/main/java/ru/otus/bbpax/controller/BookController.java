package ru.otus.bbpax.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.bbpax.controller.model.BookView;
import ru.otus.bbpax.service.BookService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@AllArgsConstructor
@ShellComponent("book")
public class BookController {
    private final BookService service;

    @ShellMethod(key = {"book --create", "book -c"}, value = "book creation")
    public void createBook(@ShellOption(help = "Book's name.") String name,
                             @ShellOption(help = "Year of Book's publication.") Integer publicationDate,
                           @ShellOption(help = "Office or Name of company where this book was published.") String publishingOffice,
                             @ShellOption(help = "Book's price. Scale will be rounded to the value - 2") BigDecimal price,
                             @ShellOption(help = "Book's genre.") String genre,
                             @ShellOption(help = "Book's author.") String author) {
        log.info("create book: '{}' published in {} y. as {}, created by {} and costs - {}.",
                name, publicationDate, genre, author, price);
        BookView book = new BookView(
                null,
                name,
                publicationDate,
                publishingOffice,
                price.setScale(2, RoundingMode.HALF_UP),
                genre,
                author
        );

        log.info("registration of new book: {}", book);
        service.create(book);
    }

    @ShellMethod(key = {"book --update", "book -u"}, value = "update book with id")
    public void updateBook(@ShellOption(help = "Book's id.") Long id,
                           @ShellOption(help = "Book's name.") String name,
                           @ShellOption(help = "Year of Book's publication.") Integer publicationDate,
                           @ShellOption(help = "Office or Name of company where this book was published.") String publishingOffice,
                           @ShellOption(help = "Book's price. Scale will be rounded to the value - 2") BigDecimal price,
                           @ShellOption(help = "Book's genre.") String genre,
                           @ShellOption(help = "Book's author.") String author) {
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

    @ShellMethod(key = {"book --get", "book -g"}, value = "Get Book with id")
    public BookView getBook(@ShellOption(help = "Book's id.") Long id) {
        return service.getBookById(id);
    }

    @ShellMethod(key = {"book --list", "book -l"}, value = "List of all Books")
    public List<BookView> getBooks() {
        return service.getAll();
    }

    @ShellMethod(key = {"book --delete", "book -d"}, value = "Delete Book by id")
    public void deleteBookById(@ShellOption(help = "Book's id.") Long id) {
        service.deleteById(id);
    }
}
