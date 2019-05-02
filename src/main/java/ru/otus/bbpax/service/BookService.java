package ru.otus.bbpax.service;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.bbpax.service.model.BookDto;

import java.util.List;

@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public interface BookService {

    @PreAuthorize("hasRole('ADMIN') or @ComponentOwner.isBookOwner(principal, #book)")
    BookDto create(BookDto book);

    @PreAuthorize("hasRole('ADMIN') or @ComponentOwner.isBookOwner(principal, #book)")
    BookDto update(BookDto book);

    BookDto getBookById(String id);

    List<BookDto> getBooksByAuthor(String authorId);

    List<BookDto> getBooksByGenre(String genreId);

    List<BookDto> getAll();

    @PreAuthorize("hasAnyRole('ADMIN')")
    void deleteById(String id);
}
