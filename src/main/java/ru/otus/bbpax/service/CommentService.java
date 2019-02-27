package ru.otus.bbpax.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.bbpax.controller.model.CommentView;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.repository.BookRepo;
import ru.otus.bbpax.repository.CommentRepo;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepo repo;
    private final BookRepo bookRepo;

    public void create(String username, String text, Long bookId) {
        bookRepo.findById(bookId)
                .ifPresent(book -> repo.create(new Comment(username, text, book)));
    }

    public void update(Long id, String text) {
        repo.update(id, text);
    }

    public List<CommentView> getCommentsFor(Long bookId) {
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
        return repo.getAll()
                .stream()
                .map(CommentView::fromEntity)
                .collect(Collectors.toList());
    }
}
