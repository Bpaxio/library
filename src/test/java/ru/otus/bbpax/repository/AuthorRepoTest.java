package ru.otus.bbpax.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.repository.mapper.AuthorMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {RepoConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class AuthorRepoTest {

    @Autowired
    private AuthorRepo repo;

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    @Test
    public void testCreate() throws Exception {
        Author expected = new Author(
                1L,
                "ТестИмя",
                "ТестФамилия",
                "ТестСтрана"
        );
        repo.create(expected);

        List<Author> authors = jdbc.query("select * from author",
                new AuthorMapper());
        authors.forEach(author1 -> log.info("is: {}", author1));
        assertEquals(1, authors.size());
        Author saved = authors.get(0);
        assertEquals(expected, saved);
    }

    @Test
    public void testCreateWithAnyId() throws Exception {
        Author wrong = new Author(
                100500L,
                "ТестИмя",
                "ТестФамилия",
                "ТестСтрана"
        );
        repo.create(wrong);

        List<Author> authors = jdbc.query("select * from author",
                new AuthorMapper());
        assertEquals(1, authors.size());
        Author saved = authors.get(0);

        assertNotEquals(wrong, saved);
        assertNotEquals(wrong.getId(), saved.getId());
        assertEquals(wrong.getName(), saved.getName());
        assertEquals(wrong.getSurname(), saved.getSurname());
        assertEquals(wrong.getCountry(), saved.getCountry());
    }

    @Test
    public void testUpdate() throws Exception {
        Author was = new Author(
                1L,
                "ТестИмя",
                "ТестФамилия",
                "ТестСтрана"
        );
        Author is = new Author(
                1L,
                "ТестИмяNEW",
                "ТестФамилияNEW",
                "ТестСтранаNEW"
        );
        jdbc.update(
                "insert into author (name, surname, country) values (:name, :surname, :country)",
                new AuthorMapper().mapSource(was)
        );

        repo.update(is);

        List<Author> authors = jdbc.query("select * from author",
                new AuthorMapper());
        assertEquals(1, authors.size());
        Author saved = authors.get(0);
        assertNotEquals(was, saved);
        assertEquals(is, saved);
    }

    @Test
    public void testFindById() throws Exception {
        Author expected = new Author(
                1L,
                "ТестИмя",
                "ТестФамилия",
                "ТестСтрана"
        );
        Author notExpected = new Author(
                2L,
                "ТестИмя_wrong",
                "ТестФамилия_wrong",
                "ТестСтрана_wrong"
        );
        jdbc.update(
                "insert into author (name, surname, country) values (:name, :surname, :country)",
                new AuthorMapper().mapSource(expected)
        );
        jdbc.update(
                "insert into author (name, surname, country) values (:name, :surname, :country)",
                new AuthorMapper().mapSource(notExpected)
        );

        //test not found

        Optional<Author> author = repo.findById(0L);
        assertFalse(author.isPresent());

        author = repo.findById(1L);

        List<Author> authors = jdbc.query("select * from author",
                new AuthorMapper());
        authors.forEach(author1 -> log.info("is: {}", author1));

        assertTrue(author.isPresent());
        assertEquals(expected, author.get());
        assertNotEquals(notExpected, author.get());
    }

}
