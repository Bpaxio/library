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
public class AuthorRepoTest {

    @Autowired
    private AuthorRepo repo;

    @BeforeEach
    public void setUp(@Autowired MongoTemplate template) throws Exception {
        List<Genre> allGenre = template.findAll(Genre.class);
        List<Author> allAuthor = template.findAll(Author.class);
        List<Book> allBook = template.findAll(Book.class);
        log.info("all: \ncount-{}:{};\ncount-{}:{};\ncount-{}:{};",
                allGenre.size(), allGenre,
                allAuthor.size(), allAuthor,
                allBook.size(), allBook);
    }

    @Test
    public void testCreate(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Author.class)).count();
        Author expected = new Author(
                "ТестИмя",
                "ТестФамилия",
                "ТестСтрана"
        );

        repo.save(expected);

        assertEquals(initCount + 1, template.getCollection(template.getCollectionName(Author.class)).count());

        Author saved = template.findById(expected.getId(), Author.class);
        assertEquals(expected, saved);
    }

    @Test
    public void testUpdate(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Author.class)).count();
        Author was = new Author(
                "1c77bb3f57cfe05a39abc17a",
                "AuthorTest",
                "DoeTest",
                "CountryTest"
        );
        Author is = new Author(
                "1c77bb3f57cfe05a39abc17a",
                "ТестИмяNEW",
                "ТестФамилияNEW",
                "ТестСтранаNEW"
        );
        repo.save(is);

        long actualCount = template.getCollection(template.getCollectionName(Author.class)).count();
        assertEquals(initCount, actualCount);

        Author saved = template.findById(was.getId(), Author.class);
        assertNotEquals(was, saved);

        testEquals(is, saved);
    }

    @Test
    public void testFindById() throws Exception {
        Author expected = new Author(
                "4c77bb3f57cfe05a39abc17a",
                "TestName",
                "TestSurname",
                "TestCountry"
        );
        Author notExpected = new Author(
                "3c77bb3f57cfe05a39abc17a",
                "Author2",
                "Doe2",
                "GB"
        );

        //test not found

        Optional<Author> author = repo.findById("2212c77bb3f57cfe05a39abc17a");
        assertFalse(author.isPresent());

        author = repo.findById("4c77bb3f57cfe05a39abc17a");

        assertTrue(author.isPresent());
        testEquals(expected, author.get());
        assertNotEquals(notExpected, author.get());
    }

    @Test
    public void testFindAll(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Author.class)).count();

        List<Author> all = repo.findAll();

        assertEquals(initCount, all.size());
        assertEquals(template.findAll(Author.class), all);
    }

    @Test
    public void testDeleteById(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Author.class)).count();
        Author author = template.findById("4c77bb3f57cfe05a39abc17a", Author.class);
        assertNotNull(author);
        repo.deleteById("4c77bb3f57cfe05a39abc17a");
        assertEquals(initCount - 1, template.getCollection(template.getCollectionName(Author.class)).count());
    }

    @Test
    public void testFindByNameAndSurname() throws Exception {
        Author expected = new Author(
                "4c77bb3f57cfe05a39abc17a",
                "TestName",
                "TestSurname",
                "TestCountry"
        );
        Author notExpected = new Author(
                "3c77bb3f57cfe05a39abc17a",
                "Author2",
                "Doe2",
                "GB"
        );

        Optional<Author> author = repo.findByNameAndSurname(expected.getName(), expected.getSurname());
        assertTrue(author.isPresent());
        testEquals(expected, author.get());
        assertNotEquals(notExpected, author.get());

        author = repo.findByNameAndSurname("Not existed", "Unbelievable");
        assertFalse(author.isPresent());
    }

    private void testEquals(Author expected, Author actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
    }

}
