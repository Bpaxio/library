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
    public Book create(BookView book) {
        Book bookBone = book.toEntity();

        fillData(bookBone);
        log.info("{}", bookBone);
        return repo.save(bookBone);
    }

    @Transactional
    public void update(BookView book) {
        Book target = book.toEntity();
        fillData(target);
        repo.save(target);
    }

    private void fillData(Book bookBone) {
        authorRepo.findByNameAndSurname(
                bookBone.getAuthor().getName(),
                bookBone.getAuthor().getSurname()
        )
                .ifPresent(bookBone::setAuthor);

        genreRepo.findByName(bookBone.getGenre().getName())
                .ifPresent(bookBone::setGenre);
    }


    public BookView getBookById(String id) {
        Optional<Book> result = repo.findById(id);
        return BookView.fromEntity(
                result.orElseThrow(() -> new NotFoundException("Book", id))
        );
    }

    public List<BookView> getBooksByAuthor(String authorId) {
        return repo.getAllByAuthorId(authorId)
                .stream()
                .map(BookView::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BookView> getBooksByGenre(String genreId) {
        return repo.getAllByGenreId(genreId)
                .stream()
                .map(BookView::fromEntity)
                .collect(Collectors.toList());
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
