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
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class CommentRepoTest {

    @Autowired
    private CommentRepo repo;

    private Book book;

    @BeforeEach
    public void setUp(@Autowired MongoTemplate template) throws Exception {
        book = template.findById("1c77bb3f57cfe05a39abc17a", Book.class);
    }

    @Test
    public void testCreate(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Comment.class)).count();
        Comment expected = new Comment(
                "CreateUserName",
                "some text",
                book
        );
        repo.save(expected);

        assertEquals(initCount + 1, template.getCollection(template.getCollectionName(Comment.class)).count());

        Comment saved = template.findById(expected.getId(), Comment.class);
        assertEquals(expected, saved);
    }

    @Test
    public void testUpdate(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Comment.class)).count();
        Comment was = new Comment(
                "6c77bb3f57cfe05a39abc17a",
                "TestCommentator0",
                LocalDateTime.parse("2019-02-27T19:15:23.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment6",
                book
        );
        Comment is = new Comment(
                "6c77bb3f57cfe05a39abc17a",
                "NewTestCommentator1",
                LocalDateTime.parse("2019-02-28T14:09:27.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "some text",
                book
        );
        repo.save(is);

        long actualCount = template.getCollection(template.getCollectionName(Comment.class)).count();
        assertEquals(initCount, actualCount);

        Comment saved = template.findById(was.getId(), Comment.class);
        assertNotEquals(was, saved);

        testEquals(is, saved);
    }

    @Test
    public void testUpdateMsgOnly(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Comment.class)).count();
        Comment was = new Comment(
                "6c77bb3f57cfe05a39abc17a",
                "TestCommentator0",
                LocalDateTime.parse("2019-02-27T19:15:23.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment6",
                book
        );
        Comment is = new Comment(
                "6c77bb3f57cfe05a39abc17a",
                "TestCommentator0",
                LocalDateTime.parse("2019-02-27T19:15:23.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "New message text",
                book
        );
        repo.update(is.getId(), is.getMessage());

        long actualCount = template.getCollection(template.getCollectionName(Comment.class)).count();
        assertEquals(initCount, actualCount);

        Comment saved = template.findById(is.getId(), Comment.class);
        assertNotEquals(was, saved);

        testEquals(is, saved);
    }

    @Test
    public void testFindById() throws Exception {
        Comment expected = new Comment(
                "6c77bb3f57cfe05a39abc17a",
                "TestCommentator0",
                LocalDateTime.parse("2019-02-27T19:15:23.356", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment6",
                book
        );
        Comment notExpected = new Comment(
                "3c77bb3f57cfe05a39abc17a",
                "TestCommentator2",
                LocalDateTime.parse("2019-02-27T14:09:23.376", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                "testComment3",
                book
        );
        //test not found

        Optional<Comment> comment = repo.findById("2212c77bb3f57cfe05a39abc17a");
        assertFalse(comment.isPresent());

        comment = repo.findById("6c77bb3f57cfe05a39abc17a");

        assertTrue(comment.isPresent());
        testEquals(expected, comment.get());
        assertNotEquals(notExpected, comment.get());
    }

    @Test
    public void testFindAllByBookId() throws Exception {
        assertEquals(5, repo.findAllByBookId("3c77bb3f57cfe05a39abc17a").size());
        assertEquals(1, repo.findAllByBookId("1c77bb3f57cfe05a39abc17a").size());
    }

    @Test
    public void testFindAllByUsername() throws Exception {
        assertEquals(3, repo.findAllByUsername("TestCommentator1").size());
    }

    @Test
    public void testGetAll(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Comment.class)).count();

        List<Comment> all = repo.findAll();

        assertEquals(initCount, all.size());
        assertEquals(template.findAll(Comment.class), all);
    }

    @Test
    public void testDeleteById(@Autowired MongoTemplate template) throws Exception {
        long initCount = template.getCollection(template.getCollectionName(Comment.class)).count();
        Comment comment = template.findById("4c77bb3f57cfe05a39abc17a", Comment.class);
        assertNotNull(comment);
        repo.deleteById("4c77bb3f57cfe05a39abc17a");
        assertEquals(initCount - 1, template.getCollection(template.getCollectionName(Comment.class)).count());

    }

    private void testEquals(Comment expected, Comment actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCreated(), actual.getCreated());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getBook().getId(), actual.getBook().getId());
    }
}
