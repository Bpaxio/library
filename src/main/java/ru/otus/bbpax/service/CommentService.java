package ru.otus.bbpax.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.bbpax.controller.model.CommentView;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.repository.BookRepo;
import ru.otus.bbpax.repository.CommentRepo;
import ru.otus.bbpax.service.error.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepo repo;
    private final BookRepo bookRepo;

    @Transactional
    public void create(String username, String text, String bookId) {
        Optional<Book> book = bookRepo.findById(bookId);
        if (!book.isPresent()) {
            throw new NotFoundException("Book", bookId);
        }
        repo.save(new Comment(username, text, book.get()));
    }

    @Transactional
    public void update(String id, String text) {
        repo.update(id, text);
    }

    @Transactional
    public List<CommentView> getCommentsFor(String bookId) {
        return repo.findAllByBookId(bookId)
                .stream()
                .map(CommentView::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CommentView> getCommentsBy(String username) {
        return repo.findAllByUsername(username)
                .stream()
                .map(CommentView::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CommentView> getAll() {
        return repo.findAll()
                .stream()
                .map(CommentView::fromEntity)
                .collect(Collectors.toList());
    }
}
