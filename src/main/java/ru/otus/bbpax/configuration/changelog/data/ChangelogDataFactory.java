package ru.otus.bbpax.configuration.changelog.data;

import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.bbpax.configuration.changelog.data.builder.AuthorDataBuilder;
import ru.otus.bbpax.configuration.changelog.data.builder.BookDataBuilder;
import ru.otus.bbpax.configuration.changelog.data.builder.CommentDataBoundedBuilder;
import ru.otus.bbpax.configuration.changelog.data.builder.CommentDataBuilder;
import ru.otus.bbpax.configuration.changelog.data.builder.GenreDataBuilder;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.entity.Genre;

import java.util.List;

public class ChangelogDataFactory {

    public static List<Author> defaultAuthors() {
        return new AuthorDataBuilder().defaultData().build();
    }

    public static List<Genre> defaultGenres() {
        return new GenreDataBuilder().defaultData().build();
    }

    public static List<Book> defaultBooks(MongoTemplate template) {
        return new BookDataBuilder()
                .withNovel(template.findById("1c77bb3f57cfe05a39abc17a", Genre.class))
                .withDrama(template.findById("2c77bb3f57cfe05a39abc17a", Genre.class))
                .withTestAuthor(template.findById("1c77bb3f57cfe05a39abc17a", Author.class))
                .withAuthor2(template.findById("3c77bb3f57cfe05a39abc17a", Author.class))
                .defaultData().build();
    }

    public static List<Comment> defaultComments(Book book1, Book book2) {
        return new CommentDataBuilder()
                .withFirstBook(book1)
                .withSecondBook(book2)
                .defaultData().build();
    }

    public static List<Comment> customCommentsForBook(Book book, List<Comment> comments) {
        CommentDataBoundedBuilder builder = new CommentDataBoundedBuilder().forBook(book);
        comments.forEach(builder::makeComment);
        return builder.build();
    }
}
