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

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.bbpax.entity.security.Roles.AUTHOR;
import static ru.otus.bbpax.entity.security.Roles.USER;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser
class BookServiceTest {
    @Autowired
    private BookService service;

    private BookDto book() {
        return new BookDto(
                "1c77bb3f57cfe05a39abc17a",
                "SUPER_BOOK",
                2019,
                "The Test Office",
                BigDecimal.valueOf(2000),
                "1c77bb3f57cfe05a39abc17a",
                "1c77bb3f57cfe05a39abc17a");
    }

    @Test
    @WithMockAdmin
    void createBook() throws Exception {
        service.create(book());
    }

    @Test
    @WithMockUser(username = "AuthorTest", roles = {USER, AUTHOR})
    void createBookWithAuthor() throws Exception {
        service.create(book());
    }

    @Test
    @WithMockUser(username = "WrongAuthorTest", roles = {USER, AUTHOR})
    void createBookWithWrongAuthor() throws Exception {
        assertThrows(AccessDeniedException.class,
                () -> service.create(book()));
    }

    @Test
    void createBookWithUser() throws Exception {
        assertThrows(AccessDeniedException.class,
                () -> service.create(book()));
    }

    @Test
    @WithMockAdmin
    void updateBook() throws Exception {
        service.update(book());
    }

    @Test
    @WithMockUser(username = "AuthorTest", roles = {USER, AUTHOR})
    void updateBookWithAuthor() throws Exception {
        service.update(book());
    }

    @Test
    @WithMockUser(username = "WrongAuthorTest", roles = {USER, AUTHOR})
    void updateBookWithWrongAuthor() throws Exception {
        assertThrows(AccessDeniedException.class,
                () -> service.update(book()));
    }

    @Test
    void updateBookWithUser() throws Exception {
        assertThrows(AccessDeniedException.class,
                () -> service.update(book()));
    }

    @Test
    @WithMockAdmin
    void getBook() throws Exception {
        service.getBookById(book().getId());
    }

    @Test
    void getBookWithUser() throws Exception {
        service.getBookById(book().getId());
    }

    @Test
    void getBooks() throws Exception {
        List<BookDto> all = service.getAll();
        assertThat(all).isNotEmpty();
    }

    @Test
    @WithMockAdmin
    void deleteBookById() throws Exception {
        service.deleteById(book().getId());
    }

    @Test
    void deleteBookByIdWithUser() throws Exception {
        assertThrows(AccessDeniedException.class,
                () -> service.deleteById(book().getId()));
    }
}