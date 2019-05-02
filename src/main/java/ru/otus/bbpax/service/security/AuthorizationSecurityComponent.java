package ru.otus.bbpax.service.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.repository.AuthorRepo;
import ru.otus.bbpax.repository.CommentRepo;
import ru.otus.bbpax.service.error.NotFoundException;
import ru.otus.bbpax.service.model.BookDto;

import javax.annotation.Nonnull;
import java.util.Objects;

@Slf4j
@Component("ComponentOwner")
@AllArgsConstructor
public class AuthorizationSecurityComponent {
    private final AuthorRepo authorRepo;
    private final CommentRepo commentRepo;

    public boolean isBookOwner(@Nonnull UserDetails principal, @Nonnull BookDto bookDto) {
        Author author = authorRepo.findById(bookDto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("author", bookDto.getAuthorId()));

        log.info("user[{}] IS EQUAL TO author.firstname[{}] = {}",
                principal.getUsername(), author.getName(),
                Objects.equals(principal.getUsername(), author.getName()));

        return Objects.equals(principal.getUsername(), author.getName());
    }

    public boolean isCommentOwner(@Nonnull UserDetails principal, @Nonnull String commentId) {
         Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new NotFoundException("comment", commentId));

        log.info("user[{}] IS EQUAL TO comment.username[{}] = {}",
                principal.getUsername(), comment.getUsername(),
                Objects.equals(principal.getUsername(), comment.getUsername()));

        return Objects.equals(principal.getUsername(), comment.getUsername());
    }
}
