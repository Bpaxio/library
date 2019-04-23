package ru.otus.bbpax.service.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.bbpax.configuration.serialization.CustomLocalDateTimeDeserializer;
import ru.otus.bbpax.configuration.serialization.CustomLocalDateTimeSerializer;
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
public class CommentDto {

    private String id;
    private String username;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime created;
    private String message;
    private String bookId;

    public static CommentDto fromEntity(Comment comment) {
        return Objects.isNull(comment)
                ? null
                : new CommentDto(
                        comment.getId(),
                        comment.getUsername(),
                        comment.getCreated(),
                        comment.getMessage(),
                        comment.getBook().getId());
    }
}
