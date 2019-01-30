package ru.otus.bbpax.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.bbpax.controller.model.GenreView;
import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.repository.GenreRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class GenreService {
    private final GenreRepo repo;

    public void create(GenreView genre) {
        repo.create(genre.toEntity());
    }

    public void update(GenreView genre) {
        repo.update(genre.toEntity());
    }

    public GenreView getGenreById(Long id) {
        Optional<Genre> result = repo.findById(id);
        return result.map(GenreView::fromEntity)
                .orElse(null);
    }

    public List<GenreView> getAll() {
        return repo.getAll()
                .stream()
                .map(GenreView::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
