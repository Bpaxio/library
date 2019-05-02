package ru.otus.bbpax.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.bbpax.rest.WithMockAdmin;
import ru.otus.bbpax.service.model.AuthorDto;
import ru.otus.bbpax.service.model.BookDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser
class AuthorServiceTest {
    @Autowired
    private AuthorService service;

    private AuthorDto author() {
        return new AuthorDto("2c77bb3f57cfe05a39abc17a","Name", "Surname", "Country");
    }


    @Test
    @WithMockAdmin
    void create() {
        service.create(author());
    }

    @Test
    void createWithUser() {
        assertThrows(AccessDeniedException.class, () -> service.create(author()));
    }

    @Test
    @WithMockAdmin
    void update() {
        service.update(author());
    }

    @Test
    void updateWithUser() {
        assertThrows(AccessDeniedException.class, () -> service.update(author()));
    }

    @Test
    void getAuthorById() {
        AuthorDto authorById = service.getAuthorById(author().getId());
    }

    @Test
    void getAll() {
        List<AuthorDto> authors = service.getAll();
        assertThat(authors).isNotNull().isNotEmpty();
    }

    @Test
    void getBooksById() {
        List<BookDto> booksById = service.getBooksById(author().getId());
        assertThat(booksById).isNotNull();
    }

    @Test
    @WithMockAdmin
    void deleteById() {
        service.deleteById(author().getId());
    }

    @Test
    void deleteByIdWithUser() {
        assertThrows(AccessDeniedException.class, () -> service.deleteById(author().getId()));
    }
}