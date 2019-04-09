package ru.otus.bbpax.shell.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.service.model.GenreView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.otus.bbpax.shell.model.TestVariables.GENRE_ID;
import static ru.otus.bbpax.shell.model.TestVariables.GENRE_NAME;

class GenreViewTest {
    private GenreView view;
    private Genre entity;

    @BeforeEach
    void setUp() {
        view = new GenreView(GENRE_ID, GENRE_NAME);
        entity = new Genre(GENRE_ID, GENRE_NAME);
    }

    @Test
    @DisplayName("создает view с теми же значениями полей, что у entity или null.")
    void fromEntity() {
        assertEquals(view, GenreView.fromEntity(entity));

        assertNull(GenreView.fromEntity(null));
    }

    @Test
    @DisplayName("создает entity с теми же значениями полей, что у view.")
    void toEntity() {
        assertEquals(entity, view.toEntity());
    }
}