package ru.otus.bbpax.service;

import ru.otus.bbpax.service.model.AuthorDto;
import ru.otus.bbpax.service.model.BookDto;

import java.util.List;

public interface AuthorService {

    AuthorDto create(AuthorDto authorDto);

    AuthorDto update(AuthorDto authorDto);

    AuthorDto getAuthorById(String id);

    List<AuthorDto> getAll();

    List<BookDto> getBooksById(String id);

    void deleteById(String id);
}
