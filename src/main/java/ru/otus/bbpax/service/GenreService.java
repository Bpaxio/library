package ru.otus.bbpax.service;

import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.service.model.GenreDto;

import java.util.List;

public interface GenreService {

    Genre create(GenreDto genre);

    void update(GenreDto genre);

    GenreDto getGenreById(String id);

    List<GenreDto> getAll();

    void deleteById(String id);
}
