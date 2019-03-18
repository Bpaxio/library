package ru.otus.bbpax.configuration.changelog.data.builder;

import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentDataBuilder implements DataBuilder<Comment> {

    private final List<Comment> comments;

    private Book book1;
    private Book book2;

    public CommentDataBuilder() {
        comments = new ArrayList<>();
    }

    public CommentDataBuilder withFirstBook(Book book) {
        this.book1 = book;
        return this;
    }

    public CommentDataBuilder withSecondBook(Book book) {
        this.book2 = book;
        return this;
    }

    @Override
    public DataBuilder<Comment> defaultData() {
        return this.makeCommentFor(book1, new Comment(
                "6c77bb3f57cfe05a39abc17a",
                "TestCommentator0",
                LocalDateTime.parse("2019-02-27T19:15:23.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment6",
                book1
        ))
        .makeCommentFor(book2, new Comment(
                "1c77bb3f57cfe05a39abc17a",
                "TestCommentator1",
                LocalDateTime.parse("2019-02-27T14:09:23.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment1",
                book2
        ))
        .makeCommentFor(book2, new Comment(
                "2c77bb3f57cfe05a39abc17a",
                "TestCommentator1",
                LocalDateTime.parse("2019-02-27T14:09:27.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment2",
                book2
        ))
        .makeCommentFor(book2, new Comment(
                "3c77bb3f57cfe05a39abc17a",
                "TestCommentator2",
                LocalDateTime.parse("2019-02-27T14:09:23.376", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment3",
                book2

        ))
        .makeCommentFor(book2, new Comment(
                "4c77bb3f57cfe05a39abc17a",
                "TestCommentator2",
                LocalDateTime.parse("2019-02-27T14:09:29.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment4",
                book2
        ))
        .makeCommentFor(book2, new Comment(
                "5c77bb3f57cfe05a39abc17a",
                "TestCommentator1",
                LocalDateTime.parse("2019-02-27T14:15:23.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment5",
                book2
        ));
    }

    @Override
    public DataBuilder<Comment> with(Comment entity) {
        comments.add(entity);
        return this;
    }

    @Override
    public List<Comment> build() {
        return comments;
    }

    public CommentDataBuilder makeCommentFor(Book book, Comment comment) {
        if (Objects.isNull(book.getComments())) {
            book.setComments(new ArrayList<>());
        }
        book.getComments().add(comment);
        comments.add(comment);
        return this;
    }
}
