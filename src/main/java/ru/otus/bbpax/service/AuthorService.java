package ru.otus.bbpax.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.bbpax.service.model.AuthorView;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.repository.AuthorRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class AuthorService {
    private final AuthorRepo repo;

    public void create(AuthorView author) {
        repo.save(author.toEntity());
    }

    public void update(AuthorView author) {
        repo.save(author.toEntity());
    }

    public AuthorView getAuthorById(String id) {
        Optional<Author> result = repo.findById(id);
        return result.map(AuthorView::fromEntity)
                .orElse(null);
    }

    public List<AuthorView> getAll() {
        return repo.findAll()
                .stream()
                .map(AuthorView::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
