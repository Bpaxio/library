package ru.otus.bbpax.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.repository.impl.AuthorRepoImpl;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(AuthorRepoImpl.class)
@ActiveProfiles("test")
public class AuthorRepoTest {

    @Autowired
    private AuthorRepo repo;

    @Autowired
    private TestEntityManager manager;

    private TypedQuery<Author> allQuery;
    private TypedQuery<Long> countQuery;

    @Before
    @SuppressWarnings("JpaQlInspection")
    public void setUp() throws Exception {
        allQuery = manager.getEntityManager()
                .createQuery("select a from Author a", Author.class);
        countQuery = manager.getEntityManager()
                .createQuery("select count(a) from Author a", Long.class);
    }

    @Test
    public void testCreate() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Author expected = new Author(
                "ТестИмя",
                "ТестФамилия",
                "ТестСтрана"
        );

        repo.create(expected);


        assertEquals(initCount + 1, allQuery.getResultList().size());

        Author saved = manager.find(Author.class, 1L);
        assertEquals(expected, saved);

        assertNull(manager.find(Author.class, 2L));
    }

    @Test
    public void testUpdate() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Author was = new Author(
                100000L,
                "AuthorTest",
                "DoeTest",
                "CountryTest"
        );
        Author is = new Author(
                100000L,
                "ТестИмяNEW",
                "ТестФамилияNEW",
                "ТестСтранаNEW"
        );
        repo.update(is);

        Long actualCount = Integer.toUnsignedLong(allQuery.getResultList().size());
        assertEquals(initCount, actualCount);

        Author saved = manager.find(Author.class, 100000L);
        assertNotEquals(was, saved);

        testEquals(is, saved);
    }

    @Test
    public void testFindById() throws Exception {
        Author expected = new Author(
                100003L,
                "TestName",
                "TestSurname",
                "TestCountry"
        );
        Author notExpected = new Author(
                100002L,
                "Author2",
                "Doe2",
                "GB"
        );

        //test not found

        Optional<Author> author = repo.findById(0L);
        assertFalse(author.isPresent());

        author = repo.findById(100003L);

        assertTrue(author.isPresent());
        testEquals(expected, author.get());
        assertNotEquals(notExpected, author.get());
    }

    @Test
    public void testGetAll() throws Exception {
        Long initCount = countQuery.getSingleResult();

        List<Author> all = repo.getAll();
        assertEquals(initCount.intValue(), all.size());

        assertEquals(allQuery.getResultList(), all);
    }

    @Test
    public void testDeleteById() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Author author = manager.find(Author.class, 100003L);
        assertNotNull(author);
        repo.deleteById(100003L);
        assertEquals(initCount - 1, countQuery.getSingleResult().longValue());
    }

    @Test
    public void testFindByFullName() throws Exception {
        Author expected = new Author(
                100003L,
                "TestName",
                "TestSurname",
                "TestCountry"
        );
        Author notExpected = new Author(
                100002L,
                "Author2",
                "Doe2",
                "GB"
        );

        Optional<Author> author = repo.findByFullName(expected.getName(), expected.getSurname());
        assertTrue(author.isPresent());
        testEquals(expected, author.get());
        assertNotEquals(notExpected, author.get());

        author = repo.findByFullName("Not existed", "Unbelievable");
        assertFalse(author.isPresent());
    }

    private void testEquals(Author expected, Author actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
    }

}
