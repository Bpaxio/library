package ru.otus.bbpax.service;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.bbpax.service.model.AuthorDto;
import ru.otus.bbpax.service.model.BookDto;

import java.util.List;

@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public interface AuthorService {

    @PreAuthorize("hasRole('ADMIN')")
    AuthorDto create(AuthorDto authorDto);

    @PreAuthorize("hasAnyRole('ADMIN')")
    AuthorDto update(AuthorDto authorDto);

    AuthorDto getAuthorById(String id);

    List<AuthorDto> getAll();

    List<BookDto> getBooksById(String id);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteById(String id);
}
