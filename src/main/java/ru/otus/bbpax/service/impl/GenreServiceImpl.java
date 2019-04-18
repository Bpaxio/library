package ru.otus.bbpax.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.repository.GenreRepo;
import ru.otus.bbpax.service.GenreService;
import ru.otus.bbpax.service.model.GenreDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepo repo;

    public Genre create(GenreDto genre) {
        return repo.save(genre.toEntity());
    }

    public void update(GenreDto genre) {
        repo.save(genre.toEntity());
    }

    public GenreDto getGenreById(String id) {
        Optional<Genre> result = repo.findById(id);
        return result.map(GenreDto::fromEntity)
                .orElse(null);
    }

    public List<GenreDto> getAll() {
        return repo.findAll()
                .stream()
                .map(GenreDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
