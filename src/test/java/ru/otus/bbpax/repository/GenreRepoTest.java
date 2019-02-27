package ru.otus.bbpax.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.bbpax.entity.Genre;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class GenreRepoTest {

    @Autowired
    private GenreRepo repo;

    @Autowired
    private TestEntityManager manager;

    private TypedQuery<Genre> allQuery;
    private TypedQuery<Long> countQuery;

    @Before
    @SuppressWarnings("JpaQlInspection")
    public void setUp() throws Exception {
        allQuery = manager.getEntityManager()
                .createQuery("select g from Genre g", Genre.class);
        countQuery = manager.getEntityManager()
                .createQuery("select count(g) from Genre g", Long.class);
    }

    @Test
    public void testCreate() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Genre expected = new Genre(
                "ТестИмя"
        );

        repo.save(expected);


        assertEquals(initCount + 1, allQuery.getResultList().size());

        Genre saved = manager.find(Genre.class, 1L);
        assertEquals(expected, saved);

        assertNull(manager.find(Genre.class, 2L));
    }

    @Test
    public void testUpdate() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Genre was = new Genre(
                100000L,
                "Novel"
        );
        Genre is = new Genre(
                100000L,
                "ТестИмяNEW"
        );
        repo.save(is);

        Long actualCount = Integer.toUnsignedLong(allQuery.getResultList().size());
        assertEquals(initCount, actualCount);

        Genre saved = manager.find(Genre.class, 100000L);

        assertNotEquals(was, saved);
        testEquals(is, saved);
    }

    @Test
    public void testFindById() throws Exception {
        Genre expected = new Genre(
                100001L, "Drama"
        );
        Genre notExpected = new Genre(
                100002L, "Science fiction"
        );

        //test not found

        Optional<Genre> genre = repo.findById(0L);
        assertFalse(genre.isPresent());

        genre = repo.findById(100001L);

        assertTrue(genre.isPresent());
        testEquals(expected, genre.get());
        assertNotEquals(notExpected, genre.get());
    }

    @Test
    public void testFindAll() throws Exception {
        Long initCount = countQuery.getSingleResult();

        List<Genre> all = repo.findAll();
        assertEquals(initCount.intValue(), all.size());

        assertEquals(allQuery.getResultList(), all);
    }

    @Test
    public void testDeleteById() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Genre genre = manager.find(Genre.class, 100003L);
        assertNotNull(genre);
        repo.deleteById(100003L);
        assertEquals(initCount - 1, countQuery.getSingleResult().longValue());
    }

    @Test
    public void testFindByName() throws Exception {
        Genre expected = new Genre(
                100001L, "Drama"
        );
        Genre notExpected = new Genre(
                100002L, "Science fiction"
        );

        Optional<Genre> genre = repo.findByName(expected.getName());
        assertTrue(genre.isPresent());
        testEquals(expected, genre.get());
        assertNotEquals(notExpected, genre.get());

        genre = repo.findByName("Not existed");
        Assertions.assertFalse(genre.isPresent());
    }

    private void testEquals(Genre expected, Genre actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }
}
