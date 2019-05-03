package ru.otus.bbpax.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.bbpax.entity.Genre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.otus.bbpax.service.model.TestVariables.GENRE_ID;
import static ru.otus.bbpax.service.model.TestVariables.GENRE_NAME;

class GenreDtoTest {
    private GenreDto dto;
    private Genre entity;

    @BeforeEach
    void setUp() {
        dto = new GenreDto(GENRE_ID, GENRE_NAME);
        entity = new Genre(GENRE_ID, GENRE_NAME);
    }

    @Test
    @DisplayName("создает dto с теми же значениями полей, что у entity или null.")
    void fromEntity() {
        assertEquals(dto, GenreDto.fromEntity(entity));

        assertNull(GenreDto.fromEntity(null));
    }

    @Test
    @DisplayName("создает entity с теми же значениями полей, что у dto.")
    void toEntity() {
        assertEquals(entity, dto.toEntity());
    }
}