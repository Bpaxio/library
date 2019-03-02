package ru.otus.bbpax.configuration;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.DBRef;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDateTime;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.entity.Genre;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.otus.bbpax.entity.EntityTypes.AUTHOR;
import static ru.otus.bbpax.entity.EntityTypes.COMMENT;
import static ru.otus.bbpax.entity.EntityTypes.GENRE;

@Slf4j
@ChangeLog
public class DatabaseChangelog {
    private final static Map<String, List<Comment>> bookComments;
    static {
        bookComments = new HashMap<>();
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
    public void addAuthors(MongoDatabase db) {
        log.info("db NAME: {}", db.getName());
        MongoCollection<Document> authors = db.getCollection("authors");
        List<Document> docs = new ArrayList<>();
        docs.add(authorDocumentWith(
                "1c77bb3f57cfe05a39abc17a",
                "AuthorTest",
                "DoeTest",
                "CountryTest"
        ));
        docs.add(authorDocumentWith(
                "2c77bb3f57cfe05a39abc17a",
                "Author1",
                "Doe1",
                "USA"
        ));
        docs.add(authorDocumentWith(
                "3c77bb3f57cfe05a39abc17a",
                "Author2",
                "Doe2",
                "GB"
        ));
        docs.add(authorDocumentWith(
                "4c77bb3f57cfe05a39abc17a",
                "TestName",
                "TestSurname",
                "TestCountry"
        ));
        authors.insertMany(docs);
    }

    private Document authorDocumentWith(String id, String name, String surname, String country) {
        return new Document()
                .append("_id", new ObjectId(id))
                .append("name", name)
                .append("surname", surname)
                .append("country", country)
                .append("_class", AUTHOR);
    }

    @ChangeSet(order = "002", id = "addGenres", author = "bpaxio")
    public void addGenres(MongoDatabase db) {
        MongoCollection<Document> genres = db.getCollection("genres");
        List<Document> docs = new ArrayList<>();
        docs.add(genreDocumentWith(
                "1c77bb3f57cfe05a39abc17a",
                "Novel"
        ));
        docs.add(genreDocumentWith(
                "2c77bb3f57cfe05a39abc17a",
                "Drama"
        ));
        docs.add(genreDocumentWith(
                "3c77bb3f57cfe05a39abc17a",
                "Science fiction"
        ));
        docs.add(genreDocumentWith(
                "4c77bb3f57cfe05a39abc17a",
                "TestGenre"
        ));
        genres.insertMany(docs);
    }

    private Document genreDocumentWith(String id, String name) {
        return new Document()
                .append("_id", new ObjectId(id))
                .append("name", name)
                .append("_class", GENRE);
    }

    @ChangeSet(order = "003", id = "addBooks", author = "bpaxio")
    public void addBooks(MongoTemplate template) {
        List<Book> books = new ArrayList<>();
        Genre novel = template.findById("1c77bb3f57cfe05a39abc17a", Genre.class);
        Genre drama = template.findById("2c77bb3f57cfe05a39abc17a", Genre.class);
        Author authorTest = template.findById("1c77bb3f57cfe05a39abc17a", Author.class);

        books.add(new Book("1c77bb3f57cfe05a39abc17a",
                "Novel of AuthorTest",
                1999,
                "testOffice",
                BigDecimal.valueOf(999.99),
                novel,
                template.findById("3c77bb3f57cfe05a39abc17a", Author.class),
                bookComments.get("1c77bb3f57cfe05a39abc17a")));

        books.add(new Book("2c77bb3f57cfe05a39abc17a",
                "Drama of AuthorTest",
                2000,
                "testOffice",
                BigDecimal.valueOf(959.99),
                drama,
                authorTest,
                bookComments.get("2c77bb3f57cfe05a39abc17a")));

        books.add(new Book("3c77bb3f57cfe05a39abc17a",
                "Again Novel of AuthorTest",
                1998,
                "testOffice",
                BigDecimal.valueOf(899.99),
                novel,
                authorTest,
                bookComments.get("3c77bb3f57cfe05a39abc17a")));

        books.add(new Book("4c77bb3f57cfe05a39abc17a",
                "Science fiction of AuthorTest",
                1997,
                "testOffice",
                BigDecimal.valueOf(859.99),
                drama,
                authorTest,
                bookComments.get("4c77bb3f57cfe05a39abc17a")));

        books.add(new Book("5c77bb3f57cfe05a39abc17a",
                "Drama of AuthorTest",
                1996,
                "testOffice",
                BigDecimal.valueOf(799.99),
                novel,
                authorTest,
                bookComments.get("5c77bb3f57cfe05a39abc17a")));
        template.insert(Book.class).all(books);
    }

    @ChangeSet(order = "004", id = "addComments", author = "bpaxio")
    public void addComments(MongoDatabase db) {
        MongoCollection<Document> comments = db.getCollection("comments");
        List<Document> docs = new ArrayList<>();
        bookComments.entrySet().stream()
                .map(entry -> toDocList(db.getName(), entry))
                .forEach(docs::addAll);
//        bookComments.forEach((bookId, commentList) ->
//                commentList.forEach(comment ->
//                        docs.add(
//                                commentDocumentWith(
//                                        new DBRef(
//                                                db.getName(),
//                                                "books",
//                                                bookId
//                                        ),
//                                        comment
//                                )
//                        )
//                )
//        );
        comments.insertMany(docs);
    }

    private List<Document> toDocList(String dbName, Map.Entry<String, List<Comment>> entry) {
        return entry.getValue()
                .stream()
                .map(comment -> commentDocumentWith(
                        new DBRef(dbName, "books", entry.getKey()
                        ),
                        comment
                        )
                )
                .collect(Collectors.toList());
    }

    private Document commentDocumentWith(DBRef bookId, Comment comment) {
        BsonDateTime time = new BsonDateTime(
                comment.getCreated()
                        .toInstant(ZoneOffset.UTC)
                        .toEpochMilli()
        );
        return new Document()
                .append("_id", new ObjectId(comment.getId()))
                .append("username", comment.getUsername())
                .append("created", time)
                .append("message", comment.getMessage())
                .append("book", bookId)
                .append("_class", COMMENT);
    }
}
