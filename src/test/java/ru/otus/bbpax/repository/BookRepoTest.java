package ru.otus.bbpax.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
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
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.repository.mapper.AuthorMapper;
import ru.otus.bbpax.repository.mapper.BookMapper;
import ru.otus.bbpax.repository.mapper.GenreMapper;

import java.math.BigDecimal;
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
public class BookRepoTest {

    @Autowired
    private BookRepo repo;

    @Autowired
    private NamedParameterJdbcOperations jdbc;

    private Author author;
    private Genre genre;

    @Before
    public void setUp() {
        genre = new Genre(
                1L,
                "ТестИмя"
        );
        jdbc.update(
                "insert into genre (name) values (:name)",
                new GenreMapper().mapSource(genre)
        );

        author = new Author(
                1L,
                "ТестИмя",
                "ТестФамилия",
                "ТестСтрана"
        );
        jdbc.update(
                "insert into author (name, surname, country) values (:name, :surname, :country)",
                new AuthorMapper().mapSource(author)
        );
    }

    @Test
    public void testCreate() throws Exception {
        Book expected = new Book(
                1L,
                "ТестИмя",
                2019,
                "ТестИздательство",
                BigDecimal.valueOf(1599.99),
                genre,
                author
        );
        repo.create(expected);

        List<Book> books = jdbc.query("select book.id, book.name, publication_date, publishing_office, price, author_id, a.name as author_name, a.surname as author_surname, a.country as author_country, genre_id, g.name as genre_name"
                                + " from book"
                                + " join author as a on author_id = a.id"
                                + " join genre as g on genre_id = g.id",
                new BookMapper());
        assertEquals(1, books.size());
        Book saved = books.get(0);
        assertEquals(expected, saved);
    }

    @Test
    public void testCreateWithAnyId() throws Exception {
        Book wrong = new Book(
                100500L,
                "ТестИмя",
                2019,
                "ТестИздательство",
                BigDecimal.valueOf(1599.99),
                genre,
                author
        );
        repo.create(wrong);

        List<Book> books = jdbc.query("select book.id, book.name, publication_date, publishing_office, price, author_id, a.name as author_name, a.surname as author_surname, a.country as author_country, genre_id, g.name as genre_name"
                                + " from book"
                                + " join author as a on author_id = a.id"
                                + " join genre as g on genre_id = g.id",
                new BookMapper());
        assertEquals(1, books.size());
        Book saved = books.get(0);

        assertNotEquals(wrong, saved);
        assertNotEquals(wrong.getId(), saved.getId());
        assertEquals(wrong.getName(), saved.getName());
        assertEquals(wrong.getPrice(), saved.getPrice());
        assertEquals(wrong.getPublicationDate(), saved.getPublicationDate());
        assertEquals(wrong.getPublishingOffice(), saved.getPublishingOffice());
        assertEquals(wrong.getGenre().getId(), saved.getGenre().getId());
        assertEquals(wrong.getAuthor().getId(), saved.getAuthor().getId());
    }

    @Test
    public void testUpdate() throws Exception {
        Book was = new Book(
                1L,
                "ТестИмя",
                2019,
                "ТестИздательство",
                BigDecimal.valueOf(1599.99),
                genre,
                author
        );
        Book is = new Book(
                1L,
                "ТестИмяNEW",
                2019,
                "ТестИздательствоNEW",
                BigDecimal.valueOf(100499.99),
                genre,
                author
        );
        jdbc.update(
                "insert into book (name, publication_date, publishing_office, price, author_id, genre_id)"
                        + " values (:name, :publication_date, :publishing_office, :price, :author_id, :genre_id)",
                new BookMapper().mapSource(was)
        );

        repo.update(is);

        List<Book> books = jdbc.query("select book.id, book.name, publication_date, publishing_office, price, author_id, a.name as author_name, a.surname as author_surname, a.country as author_country, genre_id, g.name as genre_name"
                                + " from book"
                                + " join author as a on author_id = a.id"
                                + " join genre as g on genre_id = g.id",
                new BookMapper());
        assertEquals(1, books.size());
        Book saved = books.get(0);
        assertNotEquals(was, saved);
        assertEquals(is, saved);
    }

    @Test
    public void testFindById() throws Exception {
        Book expected = new Book(
                1L,
                "ТестИмя",
                2019,
                "ТестИздательство",
                BigDecimal.valueOf(1599.99),
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
        jdbc.update(
                "insert into book (name, publication_date, publishing_office, price, author_id, genre_id)"
                        + " values (:name, :publication_date, :publishing_office, :price, :author_id, :genre_id)",
                new BookMapper().mapSource(expected)
        );
        jdbc.update(
                "insert into book (name, publication_date, publishing_office, price, author_id, genre_id)"
                        + " values (:name, :publication_date, :publishing_office, :price, :author_id, :genre_id)",
                new BookMapper().mapSource(notExpected)
        );

        //test not found

        Optional<Book> book = repo.findById(0L);
        assertFalse(book.isPresent());

        book = repo.findById(1L);

        List<Book> books = jdbc.query("select book.id, book.name, publication_date, publishing_office, price, author_id, a.name as author_name, a.surname as author_surname, a.country as author_country, genre_id, g.name as genre_name"
                                + " from book"
                                + " join author as a on author_id = a.id"
                                + " join genre as g on genre_id = g.id",
                new BookMapper());
        books.forEach(author1 -> log.info("is: {}", author1));

        assertTrue(book.isPresent());
        assertEquals(expected, book.get());
        assertNotEquals(notExpected, book.get());
    }
}
