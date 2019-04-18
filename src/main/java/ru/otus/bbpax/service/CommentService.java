package ru.otus.bbpax.service;

import ru.otus.bbpax.service.model.CommentDto;

import java.util.List;

public interface CommentService {

    void create(String username, String text, String bookId);

    void update(String id, String text);

    List<CommentDto> getCommentsFor(String bookId);

    List<CommentDto> getCommentsBy(String username);

    List<CommentDto> getAll();

    CommentDto getComment(String id);

    void deleteById(String id);
}
