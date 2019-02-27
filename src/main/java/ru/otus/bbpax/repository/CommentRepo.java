package ru.otus.bbpax.repository;

import ru.otus.bbpax.entity.Comment;

import java.util.List;

public interface CommentRepo extends Repo<Comment, Long> {
    void update(Long id, String message);

    List<Comment> findAllByBookId(Long bookId);

    List<Comment> findAllByUsername(String username);
}
