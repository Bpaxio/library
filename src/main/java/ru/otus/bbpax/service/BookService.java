package ru.otus.bbpax.service;

import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.service.model.BookDto;

import java.util.List;

public interface BookService {

    Book create(BookDto book);

    void update(BookDto book);

    BookDto getBookById(String id);

    List<BookDto> getBooksByAuthor(String authorId);

    List<BookDto> getBooksByGenre(String genreId);

    List<BookDto> getAll();

    void deleteById(String id);
}
