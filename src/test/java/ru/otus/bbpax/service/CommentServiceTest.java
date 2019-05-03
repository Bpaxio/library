package ru.otus.bbpax.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.bbpax.rest.WithMockAdmin;
import ru.otus.bbpax.service.model.BookDto;
import ru.otus.bbpax.service.model.CommentDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.bbpax.entity.security.Roles.LIAR;
import static ru.otus.bbpax.entity.security.Roles.USER;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser
class CommentServiceTest {
    @Autowired
    private CommentService service;

    private CommentDto comment() {
        return new CommentDto(
                "2c77bb3f57cfe05a39abc17a",
                "Name of commentator",
                LocalDateTime.parse("2019-04-21T16:24:03.353"),
                "message",
                "2c77bb3f57cfe05a39abc17a"
        );
    }

    @Test
    void create() {
        CommentDto comment = comment();
        service.create(comment.getUsername(), comment.getMessage(), comment.getBookId());
    }

    @Test
    @WithMockUser(roles = {USER, LIAR})
    void createWithLiar() {
        CommentDto comment = comment();
        assertThrows(AccessDeniedException.class,
                () -> service.create(comment.getUsername(), comment.getMessage(), comment.getBookId()));
    }

    @Test
    @WithMockAdmin
    void update() {
        CommentDto comment = comment();
        service.update(comment.getId(), comment.getMessage());
    }

    @Test
    void updateWithUser() {
        CommentDto comment = comment();
        service.update(comment.getId(), comment.getMessage());
    }

    @Test
    void getCommentFor() {
        service.getCommentsFor(comment().getBookId());
    }

    @Test
    void getAll() {
        List<CommentDto> authors = service.getAll();
        assertThat(authors).isNotNull().isNotEmpty();
    }

    @Test
    void getCommentsBy() {
        service.getCommentsBy(comment().getId());
    }

    @Test
    @WithMockAdmin
    void deleteById() {
        service.deleteById(comment().getId());
    }

    @Test
    void deleteByIdWithUser() {
        assertThrows(AccessDeniedException.class, () -> service.deleteById(comment().getId()));
    }
}