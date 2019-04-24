package ru.otus.bbpax.service;

import ru.otus.bbpax.service.model.GenreDto;

import java.util.List;

public interface GenreService {

    GenreDto create(GenreDto genre);

    GenreDto update(GenreDto genre);

    GenreDto getGenreById(String id);

    List<GenreDto> getAll();

    void deleteById(String id);
}
