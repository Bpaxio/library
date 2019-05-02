package ru.otus.bbpax.service;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.bbpax.service.model.CommentDto;

import java.util.List;

@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public interface CommentService {

    @PreAuthorize("hasAnyRole('USER', 'ADMIN') and not hasRole('LIAR')")
    CommentDto create(String username, String text, String bookId);

    CommentDto update(String id, String text);

    List<CommentDto> getCommentsFor(String bookId);

    List<CommentDto> getCommentsBy(String username);

    List<CommentDto> getAll();

    CommentDto getComment(String id);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteById(String id);
}
