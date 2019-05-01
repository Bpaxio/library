package ru.otus.bbpax.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Genre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.otus.bbpax.service.model.TestVariables.AUTHOR_ID;
import static ru.otus.bbpax.service.model.TestVariables.AUTHOR_NAME;
import static ru.otus.bbpax.service.model.TestVariables.AUTHOR_SURNAME;
import static ru.otus.bbpax.service.model.TestVariables.BOOK_ID;
import static ru.otus.bbpax.service.model.TestVariables.BOOK_NAME;
import static ru.otus.bbpax.service.model.TestVariables.BOOK_PRICE;
import static ru.otus.bbpax.service.model.TestVariables.BOOK_PUBLICATION_YEAR;
import static ru.otus.bbpax.service.model.TestVariables.BOOK_PUBLISHING_OFFICE;
import static ru.otus.bbpax.service.model.TestVariables.GENRE_ID;
import static ru.otus.bbpax.service.model.TestVariables.GENRE_NAME;

class BookDtoTest {
    private BookDto bookDto;
    private Book book;

    @BeforeEach
    void setUp() {
        bookDto = new BookDto(BOOK_ID,
                BOOK_NAME,
                BOOK_PUBLICATION_YEAR,
                BOOK_PUBLISHING_OFFICE,
                BOOK_PRICE,
                GENRE_ID,
                AUTHOR_ID);

        Author author = new Author();
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        author.setSurname(AUTHOR_SURNAME);
        Genre genre = new Genre();
        genre.setId(GENRE_ID);
        genre.setName(GENRE_NAME);
        book = new Book(BOOK_ID,
                BOOK_NAME,
                BOOK_PUBLICATION_YEAR,
                BOOK_PUBLISHING_OFFICE,
                BOOK_PRICE,
                genre,
                author);
    }

    @Test
    @DisplayName("создает dto с теми же значениями полей, что у entity или null.")
    void fromEntity() {
        assertEquals(bookDto, BookDto.fromEntity(book));
        assertNull(BookDto.fromEntity(null));
    }

    @Test
    @DisplayName("создает entity с теми же значениями полей, что у dto.")
    void toEntity() {
        Book result = bookDto.toEntity();
        assertEquals(this.book.getId(), result.getId());
        assertEquals(this.book.getGenre().getId(), result.getGenre().getId());
        assertEquals(this.book.getAuthor().getId(), result.getAuthor().getId());
        assertEquals(this.book.getPrice(), result.getPrice());
        assertEquals(this.book.getPublicationDate(), result.getPublicationDate());
        assertEquals(this.book.getPublishingOffice(), result.getPublishingOffice());
        assertEquals(this.book.getName(), result.getName());
    }
}