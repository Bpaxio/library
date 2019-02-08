package ru.otus.bbpax.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.repository.impl.GenreRepoImpl;
import ru.otus.bbpax.repository.mapper.GenreMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RepoConfig.class, GenreRepoImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class GenreRepoTest {

    @Autowired
    private GenreRepo repo;

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @Autowired
    private EmbeddedDatabase database;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() throws Exception {
        database.shutdown();
    }

    @Test
    public void testCreate() throws Exception {
        Genre expected = new Genre(
                1L,
                "ТестИмя"
        );
        repo.create(expected);

        List<Genre> genres = jdbc.query("select * from genre",
                new GenreMapper());
        genres.forEach(genre1 -> log.info("is: {}", genre1));
        assertEquals(1, genres.size());
        Genre saved = genres.get(0);
        assertEquals(expected, saved);
    }

    @Test
    public void testCreateWithAnyId() throws Exception {
        Genre wrong = new Genre(
                100500L,
                "ТестИмя"
        );
        repo.create(wrong);

        List<Genre> genres = jdbc.query("select * from genre",
                new GenreMapper());
        assertEquals(1, genres.size());
        Genre saved = genres.get(0);

        assertNotEquals(wrong, saved);
        assertNotEquals(wrong.getId(), saved.getId());
        assertEquals(wrong.getName(), saved.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        Genre was = new Genre(
                1L,
                "ТестИмя"
        );
        Genre is = new Genre(
                1L,
                "ТестИмяNEW"
        );
        jdbc.update(
                "insert into genre (name) values (:name)",
                new GenreMapper().mapSource(was)
        );

        repo.update(is);

        List<Genre> genres = jdbc.query("select * from genre",
                new GenreMapper());
        assertEquals(1, genres.size());
        Genre saved = genres.get(0);
        assertNotEquals(was, saved);
        assertEquals(is, saved);
    }

    @Test
    public void testFindById() throws Exception {
        Genre expected = new Genre(
                1L,
                "ТестИмя"
        );
        Genre notExpected = new Genre(
                2L,
                "ТестИмя_wrong"
        );
        jdbc.update(
                "insert into genre (name) values (:name)",
                new GenreMapper().mapSource(expected)
        );
        jdbc.update(
                "insert into genre (name) values (:name)",
                new GenreMapper().mapSource(notExpected)
        );

        //test not found

        Optional<Genre> genre = repo.findById(0L);
        assertFalse(genre.isPresent());

        genre = repo.findById(1L);

        List<Genre> genres = jdbc.query("select * from genre",
                new GenreMapper());
        genres.forEach(genre1 -> log.info("is: {}", genre1));

        assertTrue(genre.isPresent());
        assertEquals(expected, genre.get());
        assertNotEquals(notExpected, genre.get());
    }
}
