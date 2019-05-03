package ru.otus.bbpax.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.repository.AuthorRepo;
import ru.otus.bbpax.repository.BookRepo;
import ru.otus.bbpax.service.AuthorService;
import ru.otus.bbpax.service.model.AuthorDto;
import ru.otus.bbpax.service.model.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepo repo;
    private final BookRepo bookRepo;

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

    @Override
    public List<BookDto> getBooksById(String id) {
        return bookRepo.getAllByAuthorId(id)
                .stream()
                .map(BookDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
