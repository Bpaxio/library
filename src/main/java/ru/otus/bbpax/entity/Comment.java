package ru.otus.bbpax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import static ru.otus.bbpax.entity.EntityTypes.COMMENT;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "book")
@Document(collection = "comments")
@TypeAlias(COMMENT)
public class Comment {

    @Id
    private String id;

    private String username;

    private LocalDateTime created;

    private String message;

    @DBRef(db = "library")
    private Book book;

    public Comment(String username, String message, Book book) {
        this.username = username;
        this.message = message;
        this.book = book;
        this.created = LocalDateTime.now();
    }
}
