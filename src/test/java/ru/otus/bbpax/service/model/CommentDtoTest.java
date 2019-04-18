package ru.otus.bbpax.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.entity.Genre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.otus.bbpax.service.model.TestVariables.AUTHOR_NAME;
import static ru.otus.bbpax.service.model.TestVariables.AUTHOR_SURNAME;
import static ru.otus.bbpax.service.model.TestVariables.BOOK_ID;
import static ru.otus.bbpax.service.model.TestVariables.BOOK_NAME;
import static ru.otus.bbpax.service.model.TestVariables.BOOK_PRICE;
import static ru.otus.bbpax.service.model.TestVariables.BOOK_PUBLICATION_YEAR;
import static ru.otus.bbpax.service.model.TestVariables.BOOK_PUBLISHING_OFFICE;
import static ru.otus.bbpax.service.model.TestVariables.COMMENT_CREATED;
import static ru.otus.bbpax.service.model.TestVariables.COMMENT_ID;
import static ru.otus.bbpax.service.model.TestVariables.COMMENT_MESSAGE;
import static ru.otus.bbpax.service.model.TestVariables.COMMENT_USERNAME;
import static ru.otus.bbpax.service.model.TestVariables.GENRE_NAME;

class CommentDtoTest {
    private CommentDto dto;
    private Comment entity;

    @BeforeEach
    void setUp() {
        Author author = new Author();
        author.setName(AUTHOR_NAME);
        author.setSurname(AUTHOR_SURNAME);
        Genre genre = new Genre();
        genre.setName(GENRE_NAME);
        Book book = new Book(BOOK_ID,
                BOOK_NAME,
                BOOK_PUBLICATION_YEAR,
                BOOK_PUBLISHING_OFFICE,
                BOOK_PRICE,
                genre,
                author);

        dto = new CommentDto(
                COMMENT_ID,
                COMMENT_USERNAME,
                COMMENT_CREATED,
                COMMENT_MESSAGE,
                BOOK_ID
        );
        entity = new Comment(COMMENT_ID,
                COMMENT_USERNAME,
                COMMENT_CREATED,
                COMMENT_MESSAGE,
                book
        );
    }

    @Test
    @DisplayName("создает dto с теми же значениями полей, что у entity или null.")
    void fromEntity() {
        assertEquals(dto, CommentDto.fromEntity(entity));
        assertNull(BookDto.fromEntity(null));
    }
}