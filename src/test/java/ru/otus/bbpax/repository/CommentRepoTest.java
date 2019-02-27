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
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.repository.impl.CommentRepoImpl;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@Import(CommentRepoImpl.class)
@ActiveProfiles("test")
public class CommentRepoTest {

    @Autowired
    private CommentRepo repo;

    @Autowired
    private TestEntityManager manager;

    private TypedQuery<Comment> allQuery;
    private TypedQuery<Long> countQuery;

    private Author author;
    private Genre genre;
    private Book book;

    @Before
    @SuppressWarnings("JpaQlInspection")
    public void setUp() throws Exception {
        allQuery = manager.getEntityManager()
                .createQuery("select c from Comment c", Comment.class);
        countQuery = manager.getEntityManager()
                .createQuery("select count(c) from Comment c", Long.class);

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
        book = new Book(
                100004L,
                "Drama of TestAuthor",
                1996,
                "testOffice",
                BigDecimal.valueOf(799.99),
                genre,
                author
        );
    }

    @Test
    public void testCreate() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Comment expected = new Comment(
                "CreateUserName",
                "some text",
                book
        );
        repo.create(expected);

        assertEquals(initCount + 1, allQuery.getResultList().size());

        Comment saved = manager.find(Comment.class, 1L);
        assertEquals(expected, saved);

        assertNull(manager.find(Comment.class, 2L));
    }

    @Test
    public void testUpdate() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Comment was = new Comment(
                100001L,
                "TestCommentator1",
                LocalDateTime.parse("2019-02-27T14:09:27.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment",
                book
        );
        Comment is = new Comment(
                100001L,
                "NewTestCommentator1",
                LocalDateTime.parse("2019-02-28T14:09:27.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "some text",
                book
        );
        repo.update(is);

        Long actualCount = Integer.toUnsignedLong(allQuery.getResultList().size());
        assertEquals(initCount, actualCount);

        Comment saved = manager.find(Comment.class, 100001L);

        assertNotEquals(was, saved);
        testEquals(is, saved);
    }

    @Test
    public void testUpdateMsgOnly() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Comment was = new Comment(
                100001L,
                "TestCommentator1",
                LocalDateTime.parse("2019-02-27T14:09:27.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment",
                book
        );
        Comment is = new Comment(
                100001L,
                "TestCommentator1",
                LocalDateTime.parse("2019-02-27T14:09:27.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "some text",
                book
        );
        repo.update(is.getId(), is.getMessage());

        Long actualCount = Integer.toUnsignedLong(allQuery.getResultList().size());
        assertEquals(initCount, actualCount);

        Comment saved = manager.find(Comment.class, 100001L);

        assertNotEquals(was, saved);
        testEquals(is, saved);
    }

    @Test
    public void testFindById() throws Exception {
        Comment expected = new Comment(
                100002L,
                "TestCommentator2",
                LocalDateTime.parse("2019-02-27T14:09:23.376", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment3",
                book
        );
        Comment notExpected = new Comment(
                100003L,
                "TestCommentator2",
                LocalDateTime.parse("2019-02-27T14:09:29.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment4",
                book
        );
        //test not found

        Optional<Comment> comment = repo.findById(0L);
        assertFalse(comment.isPresent());

        comment = repo.findById(100002L);

        assertTrue(comment.isPresent());
        testEquals(expected, comment.get());
        assertNotEquals(notExpected, comment.get());
    }

    @Test
    public void testFindAllByBookId() throws Exception {
        assertEquals(5, repo.findAllByBookId(100004L).size());
    }

    @Test
    public void testFindAllByUsername() throws Exception {
        assertEquals(3, repo.findAllByUsername("TestCommentator1").size());
    }

    @Test
    public void testGetAll() throws Exception {
        Long initCount = countQuery.getSingleResult();

        List<Comment> all = repo.getAll();
        assertEquals(initCount.intValue(), all.size());

        assertEquals(allQuery.getResultList(), all);
    }

    @Test
    public void testDeleteById() throws Exception {
        Long initCount = countQuery.getSingleResult();
        Comment comment = manager.find(Comment.class, 100003L);
        assertNotNull(comment);
        repo.deleteById(100003L);
        assertEquals(initCount - 1, countQuery.getSingleResult().longValue());
    }

    private void testEquals(Comment expected, Comment actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCreated(), actual.getCreated());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getBook().getId(), actual.getBook().getId());
    }
}
