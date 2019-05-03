package ru.otus.bbpax.service.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.bbpax.entity.Author;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.otus.bbpax.service.model.TestVariables.AUTHOR_COUNTRY;
import static ru.otus.bbpax.service.model.TestVariables.AUTHOR_ID;
import static ru.otus.bbpax.service.model.TestVariables.AUTHOR_NAME;
import static ru.otus.bbpax.service.model.TestVariables.AUTHOR_SURNAME;

class AuthorDtoTest {
    private AuthorDto dto = new AuthorDto(AUTHOR_ID, AUTHOR_NAME, AUTHOR_SURNAME, AUTHOR_COUNTRY);
    private Author entity = new Author(AUTHOR_ID, AUTHOR_NAME, AUTHOR_SURNAME, AUTHOR_COUNTRY);

    @Test
    @DisplayName("создает dto с теми же значениями полей, что у entity или null.")
    void fromEntity() {
        assertEquals(dto, AuthorDto.fromEntity(entity));

        assertNull(AuthorDto.fromEntity(null));
    }

    @Test
    @DisplayName("создает entity с теми же значениями полей, что у dto.")
    void toEntity() {
        assertEquals(entity, dto.toEntity());
    }
}