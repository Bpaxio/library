package ru.otus.bbpax.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Genre;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:application-test.yml")
@Import(value = MongoBeeConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class BookRepoTest {

    @Autowired
    private BookRepo repo;

    private Author author;
    private Genre genre;

    @BeforeEach
    public void setUp() throws Exception {
        genre = new Genre(
                "1c77bb3f57cfe05a39abc17a",
                "Novel"
        );

        author = new Author(
                "1c77bb3f57cfe05a39abc17a",
                "AuthorTest",
                "DoeTest",
                "CountryTest"
        );
    }

    @Test
    public void testCreate(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Book.class)).count();
        Book expected = new Book(
                "ТестИмя",
                2019,
                "ТестИздательство",
                BigDecimal.valueOf(1599.99),
                genre,
                author
        );
        repo.save(expected);

        assertEquals(initCount + 1, template.getCollection(template.getCollectionName(Book.class)).count());

        Book saved = template.findById(expected.getId(), Book.class);
        assertEquals(expected, saved);
    }

    @Test
    public void testUpdate(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Book.class)).count();
        Book was = new Book(
                "1c77bb3f57cfe05a39abc17a",
                "Novel of AuthorTest",
                1999,
                "testOffice",
                BigDecimal.valueOf(999.99),
                genre,
                author
        );
        Book is = new Book(
                "1c77bb3f57cfe05a39abc17a",
                "ТестИмяNEW",
                2019,
                "ТестИздательствоNEW",
                BigDecimal.valueOf(100499.99),
                genre,
                author
        );
        repo.save(is);

        long actualCount = template.getCollection(template.getCollectionName(Book.class)).count();
        assertEquals(initCount, actualCount);

        Book saved = template.findById(was.getId(), Book.class);
        assertNotEquals(was, saved);

        testEquals(is, saved);
    }

    @Test
    public void testFindById() throws Exception {
        Book expected = new Book(
                "3c77bb3f57cfe05a39abc17a",
                "Again Novel of AuthorTest",
                1998,
                "testOffice",
                BigDecimal.valueOf(899.99),
                genre,
                author
        );
        Book notExpected = new Book(
                "4c77bb3f57cfe05a39abc17a",
                "Science fiction of AuthorTest",
                1997,
                "testOffice",
                BigDecimal.valueOf(859.99),
                genre,
                author
        );
        //test not found

        Optional<Book> book = repo.findById("2212c77bb3f57cfe05a39abc17a");
        assertFalse(book.isPresent());

        book = repo.findById("3c77bb3f57cfe05a39abc17a");

        assertTrue(book.isPresent());
        testEquals(expected, book.get());
        assertNotEquals(notExpected, book.get());
    }

    @Test
    public void testFindAll(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Book.class)).count();

        List<Book> all = repo.findAll();

        assertEquals(initCount, all.size());
        assertEquals(template.findAll(Book.class), all);
    }

    @Test
    public void testDeleteById(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Book.class)).count();
        Book book = template.findById("4c77bb3f57cfe05a39abc17a", Book.class);
        assertNotNull(book);
        repo.deleteById("4c77bb3f57cfe05a39abc17a");
        assertEquals(initCount - 1, template.getCollection(template.getCollectionName(Book.class)).count());
    }

    private void testEquals(Book expected, Book actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPublishingOffice(), actual.getPublishingOffice());
        assertEquals(expected.getPublicationDate(), actual.getPublicationDate());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getAuthor().getId(), actual.getAuthor().getId());
        assertEquals(expected.getGenre().getId(), actual.getGenre().getId());
    }
}
