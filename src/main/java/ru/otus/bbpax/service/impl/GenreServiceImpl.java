package ru.otus.bbpax.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.repository.BookRepo;
import ru.otus.bbpax.repository.GenreRepo;
import ru.otus.bbpax.service.GenreService;
import ru.otus.bbpax.service.model.BookDto;
import ru.otus.bbpax.service.model.GenreDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepo repo;
    private final BookRepo bookRepo;

    @Override
    public GenreDto create(GenreDto genre) {
        return GenreDto.fromEntity(
                repo.save(genre.toEntity())
        );
    }

    @Override
    public GenreDto update(GenreDto genre) {
        return GenreDto.fromEntity(
                repo.save(genre.toEntity())
        );
    }

    @Override
    public GenreDto getGenreById(String id) {
        Optional<Genre> result = repo.findById(id);
        return result.map(GenreDto::fromEntity)
                .orElse(null);
    }

    @Override
    public List<GenreDto> getAll() {
        return repo.findAll()
                .stream()
                .map(GenreDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> getBooksById(String id) {
        return bookRepo.getAllByGenreId(id)
                .stream()
                .map(BookDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
