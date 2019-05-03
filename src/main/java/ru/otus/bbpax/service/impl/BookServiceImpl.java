package ru.otus.bbpax.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.repository.AuthorRepo;
import ru.otus.bbpax.repository.BookRepo;
import ru.otus.bbpax.repository.GenreRepo;
import ru.otus.bbpax.service.BookService;
import ru.otus.bbpax.service.error.NotFoundException;
import ru.otus.bbpax.service.model.BookDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {
    private final BookRepo repo;
    private final AuthorRepo authorRepo;
    private final GenreRepo genreRepo;

    @Transactional
    public BookDto create(BookDto book) {
        Book bookBone = book.toNewEntity();
        fillData(bookBone);
        log.info("{}", bookBone);
        return BookDto.fromEntity(
                repo.save(bookBone)
        );
    }

    @Transactional
    public BookDto update(BookDto book) {
        Book target = book.toEntity();
        fillData(target);
        return BookDto.fromEntity(
                repo.save(target)
        );
    }

    private void fillData(Book bookBone) {
        authorRepo.findById(bookBone.getAuthor().getId())
                .ifPresent(bookBone::setAuthor);

        genreRepo.findById(bookBone.getGenre().getId())
                .ifPresent(bookBone::setGenre);
    }


    public BookDto getBookById(String id) {
        Optional<Book> result = repo.findById(id);
        return BookDto.fromEntity(
                result.orElseThrow(() -> new NotFoundException("Book", id))
        );
    }

    public List<BookDto> getBooksByAuthor(String authorId) {
        return repo.getAllByAuthorId(authorId)
                .stream()
                .map(BookDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BookDto> getBooksByGenre(String genreId) {
        return repo.getAllByGenreId(genreId)
                .stream()
                .map(BookDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BookDto> getAll() {
        return repo.findAll()
                .stream()
                .map(BookDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
