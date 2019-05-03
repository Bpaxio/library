package ru.otus.bbpax.service;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.bbpax.service.model.BookDto;
import ru.otus.bbpax.service.model.GenreDto;

import java.util.List;

@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public interface GenreService {

    @PreAuthorize("hasRole('ADMIN')")
    GenreDto create(GenreDto genre);

    @PreAuthorize("hasRole('ADMIN')")
    GenreDto update(GenreDto genre);

    GenreDto getGenreById(String id);

    List<GenreDto> getAll();

    List<BookDto> getBooksById(String id);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteById(String id);
}
