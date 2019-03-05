package ru.otus.bbpax.configuration;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.entity.Genre;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@ChangeLog
public class DatabaseChangelog {
    private final static Map<String, List<Comment>> bookComments;
    static {
        bookComments = new HashMap<>();
        bookComments.put(
                "2c77bb3f57cfe05a39abc17a", Collections.emptyList());
        bookComments.put(
                "4c77bb3f57cfe05a39abc17a", Collections.emptyList());
        bookComments.put(
                "5c77bb3f57cfe05a39abc17a", Collections.emptyList());
        bookComments.put(
                "1c77bb3f57cfe05a39abc17a",
                Collections.singletonList(
                        new Comment(
                                "6c77bb3f57cfe05a39abc17a",
                                "TestCommentator0",
                                LocalDateTime.parse("2019-02-27T19:15:23.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                                "testComment6",
                                null
                        )
                )
        );
        bookComments.put(
                "3c77bb3f57cfe05a39abc17a",
                Arrays.asList(
                        new Comment(
                                "1c77bb3f57cfe05a39abc17a",
                "TestCommentator1",
                LocalDateTime.parse("2019-02-27T14:09:23.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment1",
                                null
                        ),
                        new Comment(
                                "2c77bb3f57cfe05a39abc17a",
                "TestCommentator1",
                LocalDateTime.parse("2019-02-27T14:09:27.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment2",
                                null
                        ),
                        new Comment(
                                "3c77bb3f57cfe05a39abc17a",
                "TestCommentator2",
                LocalDateTime.parse("2019-02-27T14:09:23.376", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment3",
                                null
                        ),
                        new Comment(
                                "4c77bb3f57cfe05a39abc17a",
                "TestCommentator2",
                LocalDateTime.parse("2019-02-27T14:09:29.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment4",
                                null
                        ),
                        new Comment(
                                "5c77bb3f57cfe05a39abc17a",
                "TestCommentator1",
                LocalDateTime.parse("2019-02-27T14:15:23.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment5",
                                null
                        )
                )
        );
    }

    @ChangeSet(order = "001", id = "addAuthors", author = "bpaxio")
    public void addAuthors(MongoTemplate template) {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(
                "1c77bb3f57cfe05a39abc17a",
                "AuthorTest",
                "DoeTest",
                "CountryTest"
        ));
        authors.add(new Author(
                "2c77bb3f57cfe05a39abc17a",
                "Author1",
                "Doe1",
                "USA"
        ));
        authors.add(new Author(
                "3c77bb3f57cfe05a39abc17a",
                "Author2",
                "Doe2",
                "GB"
        ));
        authors.add(new Author(
                "4c77bb3f57cfe05a39abc17a",
                "TestName",
                "TestSurname",
                "TestCountry"
        ));
        template.insertAll(authors);
    }

    @ChangeSet(order = "002", id = "addGenres", author = "bpaxio")
    public void addGenres(MongoTemplate template) {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(
                "1c77bb3f57cfe05a39abc17a",
                "Novel"
        ));
        genres.add(new Genre(
                "2c77bb3f57cfe05a39abc17a",
                "Drama"
        ));
        genres.add(new Genre(
                "3c77bb3f57cfe05a39abc17a",
                "Science fiction"
        ));
        genres.add(new Genre(
                "4c77bb3f57cfe05a39abc17a",
                "TestGenre"
        ));
        template.insertAll(genres);
    }

    @ChangeSet(order = "003", id = "addBooks", author = "bpaxio")
    public void addBooks(MongoTemplate template) {
        List<Book> books = new ArrayList<>();
        Genre novel = template.findById("1c77bb3f57cfe05a39abc17a", Genre.class);
        Genre drama = template.findById("2c77bb3f57cfe05a39abc17a", Genre.class);
        Author authorTest = template.findById("1c77bb3f57cfe05a39abc17a", Author.class);

        Book book1 = new Book("1c77bb3f57cfe05a39abc17a",
                "Novel of AuthorTest",
                1999,
                "testOffice",
                BigDecimal.valueOf(999.99),
                novel,
                template.findById("3c77bb3f57cfe05a39abc17a", Author.class),
                bookComments.get("1c77bb3f57cfe05a39abc17a"));
        bookComments.get("1c77bb3f57cfe05a39abc17a").stream()
                .filter(Objects::nonNull)
                .forEach(comment -> comment.setBook(book1));
        books.add(book1);

        Book book2 = new Book("2c77bb3f57cfe05a39abc17a",
                "Drama of AuthorTest",
                2000,
                "testOffice",
                BigDecimal.valueOf(959.99),
                drama,
                authorTest,
                bookComments.get("2c77bb3f57cfe05a39abc17a"));
        bookComments.get("2c77bb3f57cfe05a39abc17a")
                .stream()
                .filter(Objects::nonNull)
                .forEach(comment -> comment.setBook(book2));
        books.add(book2);

        Book book3 = new Book("3c77bb3f57cfe05a39abc17a",
                "Again Novel of AuthorTest",
                1998,
                "testOffice",
                BigDecimal.valueOf(899.99),
                novel,
                authorTest,
                bookComments.get("3c77bb3f57cfe05a39abc17a"));
        bookComments.get("3c77bb3f57cfe05a39abc17a").stream()
                .filter(Objects::nonNull)
                .forEach(comment -> comment.setBook(book3));
        books.add(book3);

        Book book4 = new Book("4c77bb3f57cfe05a39abc17a",
                "Science fiction of AuthorTest",
                1997,
                "testOffice",
                BigDecimal.valueOf(859.99),
                drama,
                authorTest,
                bookComments.get("4c77bb3f57cfe05a39abc17a"));
        bookComments.get("4c77bb3f57cfe05a39abc17a").stream()
                .filter(Objects::nonNull)
                .forEach(comment -> comment.setBook(book4));
        books.add(book4);

        Book book5 = new Book("5c77bb3f57cfe05a39abc17a",
                "Drama of AuthorTest",
                1996,
                "testOffice",
                BigDecimal.valueOf(799.99),
                novel,
                authorTest,
                bookComments.get("5c77bb3f57cfe05a39abc17a"));
        bookComments.get("5c77bb3f57cfe05a39abc17a")
                .stream()
                .filter(Objects::nonNull)
                .forEach(comment -> comment.setBook(book5));
        books.add(book5);

        template.insert(Book.class).all(books);
    }

    @ChangeSet(order = "004", id = "addComments", author = "bpaxio")
    public void addComments(MongoTemplate template) {
        bookComments.values().forEach(template::insertAll);
    }
}
