package ru.otus.bbpax.service;

import ru.otus.bbpax.service.model.BookDto;

import java.util.List;

public interface BookService {

    BookDto create(BookDto book);

    BookDto update(BookDto book);

    BookDto getBookById(String id);

    List<BookDto> getBooksByAuthor(String authorId);

    List<BookDto> getBooksByGenre(String genreId);

    List<BookDto> getAll();

    void deleteById(String id);
}
