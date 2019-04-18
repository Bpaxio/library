package ru.otus.bbpax.service;

import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.service.model.AuthorDto;

import java.util.List;

public interface AuthorService {

    Author create(AuthorDto authorDto);

    void update(AuthorDto authorDto);

    AuthorDto getAuthorById(String id);

    List<AuthorDto> getAll();

    void deleteById(String id);
}
