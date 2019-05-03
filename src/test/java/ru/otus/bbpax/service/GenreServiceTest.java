package ru.otus.bbpax.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.bbpax.rest.WithMockAdmin;
import ru.otus.bbpax.service.model.GenreDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser
class GenreServiceTest {
    @Autowired
    private GenreService service;

    private GenreDto genre() {
        return new GenreDto("2c77bb3f57cfe05a39abc17a","Novel");
    }


    @Test
    @WithMockAdmin
    void createGenre() throws Exception {
        service.create(genre());
    }

    @Test
    void createGenreWithUser() throws Exception {
        assertThrows(AccessDeniedException.class,
                () -> service.create(genre()));
    }

    @Test
    @WithMockAdmin
    void updateGenre() throws Exception {
        service.update(genre());
    }

    @Test
    void updateGenreWithUser() throws Exception {
        assertThrows(AccessDeniedException.class,
                () -> service.update(genre()));
    }

    @Test
    @WithMockAdmin
    void getGenre() throws Exception {
        service.getGenreById(genre().getId());
    }

    @Test
    void getGenreWithUser() throws Exception {
        service.getGenreById(genre().getId());
    }

    @Test
    void getGenres() throws Exception {
        List<GenreDto> all = service.getAll();
        assertThat(all).isNotEmpty();
    }

    @Test
    @WithMockAdmin
    void deleteGenreById() throws Exception {
        service.deleteById(genre().getId());
    }

    @Test
    void deleteGenreByIdWithUser() throws Exception {
        assertThrows(AccessDeniedException.class,
                () -> service.deleteById(genre().getId()));
    }
}