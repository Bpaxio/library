package ru.otus.bbpax.service.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.repository.AuthorRepo;
import ru.otus.bbpax.service.AuthorService;
import ru.otus.bbpax.service.model.AuthorDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@NoArgsConstructor
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepo repo;

    public AuthorDto create(AuthorDto authorDto) {
        Author author = new Author(authorDto.getName(), authorDto.getSurname(), authorDto.getCountry());
        return AuthorDto.fromEntity(
                repo.save(author)
        );
    }

    public AuthorDto update(AuthorDto authorDto) {
        return AuthorDto.fromEntity(
                repo.save(authorDto.toEntity())
        );
    }

    public AuthorDto getAuthorById(String id) {
        Optional<Author> result = repo.findById(id);
        return result.map(AuthorDto::fromEntity)
                .orElse(null);
    }

    public List<AuthorDto> getAll() {
        return repo.findAll()
                .stream()
                .map(AuthorDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
