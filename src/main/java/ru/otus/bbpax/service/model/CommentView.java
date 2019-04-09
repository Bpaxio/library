package ru.otus.bbpax.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.bbpax.entity.Comment;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentView {

    private String id;
    private String username;
    private LocalDateTime created;
    private String message;
    private String bookId;

    public static CommentView fromEntity(Comment comment) {
        return Objects.isNull(comment)
                ? null
                : new CommentView(
                        comment.getId(),
                        comment.getUsername(),
                        comment.getCreated(),
                        comment.getMessage(),
                        comment.getBook().getId());
    }
}
