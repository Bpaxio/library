package ru.otus.bbpax.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Genre;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
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
public class BookRepoTest {

    @Autowired
    private BookRepo repo;

    @Autowired
    private TestEntityManager manager;

    private TypedQuery<Book> allQuery;
    private TypedQuery<Long> countQuery;

    private Author author;
    private Genre genre;

    @Before
    @SuppressWarnings("JpaQlInspection")
    public void setUp() throws Exception {
        allQuery = manager.getEntityManager()
                .createQuery("select b from Book b", Book.class);
        countQuery = manager.getEntityManager()
                .createQuery("select count(b) from Book b", Long.class);

        genre = new Genre(
                100000L,
                "Novel"
        );

        author = new Author(
                100000L,
                "AuthorTest",
                "DoeTest",
                "CountryTest"
        );
    }

    @Test
    public void testCreate() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Book expected = new Book(
                "ТестИмя",
                2019,
                "ТестИздательство",
                BigDecimal.valueOf(1599.99),
                genre,
                author
        );
        repo.save(expected);

        assertEquals(initCount + 1, allQuery.getResultList().size());

        Book saved = manager.find(Book.class, 1L);
        assertEquals(expected, saved);

        assertNull(manager.find(Book.class, 2L));
    }

    @Test
    public void testUpdate() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Book was = new Book(
                100002L,
                "Novel of TestAuthor",
                1998,
                "testOffice",
                BigDecimal.valueOf(899.99),
                genre,
                author
        );
        Book is = new Book(
                100002L,
                "ТестИмяNEW",
                2019,
                "ТестИздательствоNEW",
                BigDecimal.valueOf(100499.99),
                genre,
                author
        );
        repo.save(is);

        Long actualCount = Integer.toUnsignedLong(allQuery.getResultList().size());
        assertEquals(initCount, actualCount);

        Book saved = manager.find(Book.class, 100002L);

        assertNotEquals(was, saved);
        testEquals(is, saved);
    }

    @Test
    public void testFindById() throws Exception {
        Book expected = new Book(
                100002L,
                "Novel of TestAuthor",
                1998,
                "testOffice",
                BigDecimal.valueOf(899.99),
                genre,
                author
        );
        Book notExpected = new Book(
                2L,
                "ТестИмя_wrong",
                2019,
                "ТестИздательство_wrong",
                BigDecimal.valueOf(100499.99),
                genre,
                author
        );
        //test not found

        Optional<Book> book = repo.findById(0L);
        assertFalse(book.isPresent());

        book = repo.findById(100002L);

        assertTrue(book.isPresent());
        testEquals(expected, book.get());
        assertNotEquals(notExpected, book.get());
    }

    @Test
    public void testGetAll() throws Exception {
        Long initCount = countQuery.getSingleResult();

        List<Book> all = repo.findAll();
        assertEquals(initCount.intValue(), all.size());

        assertEquals(allQuery.getResultList(), all);
    }

    @Test
    public void testDeleteById() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Book book = manager.find(Book.class, 100003L);
        assertNotNull(book);
        repo.deleteById(100003L);
        assertEquals(initCount - 1, countQuery.getSingleResult().longValue());
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
