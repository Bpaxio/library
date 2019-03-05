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
public class GenreRepoTest {

    @Autowired
    private GenreRepo repo;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void testCreate(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Genre.class)).count();
        Genre expected = new Genre(
                "ТестИмя"
        );

        repo.save(expected);

        assertEquals(initCount + 1, template.getCollection(template.getCollectionName(Genre.class)).count());

        Genre saved = template.findById(expected.getId(), Genre.class);
        assertEquals(expected, saved);
    }

    @Test
    public void testUpdate(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Genre.class)).count();
        Genre was = new Genre("1c77bb3f57cfe05a39abc17a", "Novel");
        Genre is = new Genre("1c77bb3f57cfe05a39abc17a", "ТестИмяNEW");
        repo.save(is);

        long actualCount = template.getCollection(template.getCollectionName(Genre.class)).count();
        assertEquals(initCount, actualCount);

        Genre saved = template.findById(was.getId(), Genre.class);
        assertNotEquals(was, saved);

        testEquals(is, saved);
    }

    @Test
    public void testFindById(@Autowired MongoTemplate template) throws Exception {
        Genre expected = new Genre("2c77bb3f57cfe05a39abc17a", "Drama");
        Genre notExpected = new Genre("3c77bb3f57cfe05a39abc17a", "Science fiction");

        //test not found

        Optional<Genre> genre = repo.findById("100500c77bb3f57cfe05a39abc17a");
        assertFalse(genre.isPresent());

        genre = repo.findById("2c77bb3f57cfe05a39abc17a");

        assertTrue(genre.isPresent());
        testEquals(expected, genre.get());
        assertNotEquals(notExpected, genre.get());
    }

    @Test
    public void testFindAll(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Genre.class)).count();

        List<Genre> all = repo.findAll();

        assertEquals(initCount, all.size());
        assertEquals(template.findAll(Genre.class), all);
    }

    @Test
    public void testDeleteById(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Genre.class)).count();
        Genre genre = template.findById("3c77bb3f57cfe05a39abc17a", Genre.class);
        assertNotNull(genre);
        repo.deleteById("3c77bb3f57cfe05a39abc17a");
        assertEquals(initCount - 1, template.getCollection(template.getCollectionName(Genre.class)).count());

    }

    @Test
    public void testFindByName(@Autowired MongoTemplate template) throws Exception {
        Genre expected = new Genre("2c77bb3f57cfe05a39abc17a", "Drama");
        Genre notExpected = new Genre("3c77bb3f57cfe05a39abc17a", "Science fiction");

        Optional<Genre> genre = repo.findByName(expected.getName());
        assertTrue(genre.isPresent());
        testEquals(expected, genre.get());
        assertNotEquals(notExpected, genre.get());

        genre = repo.findByName("Not existed");
        assertFalse(genre.isPresent());
    }

    private void testEquals(Genre expected, Genre actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }
}
