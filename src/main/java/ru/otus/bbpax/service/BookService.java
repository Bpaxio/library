package ru.otus.bbpax.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.bbpax.service.model.BookView;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.repository.AuthorRepo;
import ru.otus.bbpax.repository.BookRepo;
import ru.otus.bbpax.repository.GenreRepo;
import ru.otus.bbpax.service.error.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepo repo;
    private final AuthorRepo authorRepo;
    private final GenreRepo genreRepo;

    @Transactional
    public void create(BookView book) {
        Book bookBone = book.toEntity();

        authorRepo.findByNameAndSurname(
                bookBone.getAuthor().getName(),
                bookBone.getAuthor().getSurname()
        )
                .ifPresent(bookBone::setAuthor);

        genreRepo.findByName(bookBone.getGenre().getName())
                .ifPresent(bookBone::setGenre);

        repo.save(bookBone);
    }

    @Transactional
    public void update(BookView book) {
        repo.save(book.toEntity());
    }


    public BookView getBookById(String id) {
        Optional<Book> result = repo.findById(id);
        return BookView.fromEntity(
                result.orElseThrow(() -> new NotFoundException("Book", id))
        );
    }

    public List<BookView> getAll() {
        return repo.findAll()
                .stream()
                .map(BookView::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
